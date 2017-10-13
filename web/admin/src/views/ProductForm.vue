<template>
	<div>
		<el-alert v-if="error.status"
			v-bind:title="error.title"
			type="error"
			v-bind:description="error.description"
			show-icon
			style = "margin-bottom:10px"
			@close="error.status = false"
			>
		</el-alert>
		<el-form label-position="right" label-width="150px" :model="form" ref="form" :rules="rules">
			<el-form-item label="SKU" prop="sku">
				<el-input v-model="form.sku"></el-input>
			</el-form-item>
			<el-form-item label="Product Name" prop="name">
				<el-input v-model="form.name"></el-input>
			</el-form-item>
			<el-form-item label="Category">
				<el-select v-model="form.category" filterable allow-create placeholder="Select">
					<el-option v-for="item in categories" :key="item.category"	:label="item.category" :value="item.category">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="UOM">
				<el-select v-model="form.uom" filterable allow-create placeholder="Select">
					<el-option v-for="item in uoms" :key="item.uom"	:label="item.uom" :value="item.uom">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="Unit Price">
				<currencyinput v-model="form.price" style="width:150px"></currencyinput>
				<!-- <el-input type="number" v-model="form.price" style="width:120px"></el-input> -->
			</el-form-item>
			<el-form-item label="Tax (%)">
				<currencyinput v-model="form.tax" style="width:100px"></currencyinput>
				<!-- <el-input type="number" v-model="form.tax"></el-input> -->
			</el-form-item>
		</el-form>

		<el-table
			ref = "unittable"
			:data="units"
			stripe
			style="width:500px"
			class="productform"
			@selection-change="handleSelectionChange">
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column prop="name" label="Active Unit / Cabang"></el-table-column>
		</el-table>
		<!-- <currencyinput v-model="form.price"></currencyinput> -->

		<span class="productform">
			<el-button type="primary" @click="saveData">Confirm</el-button>
			<el-button @click="back()">Cancel</el-button>
		</span>

	</div>
</template>

<script>
	import axios from 'axios';
	import currencyinput from '../components/CurrencyInput.vue'
	var CONFIG = require('../../config.json');

	export default {
		components: {
			currencyinput
		},
		data () {
			return{
				selectedCompany : {id : 0},
				uoms : [],
				categories : [],
				form : {
					id: 0,
					category : '',
					barcode : '',
					sku: '',
					name : '',
					uom : '',
					price : 0,
					tax : 0,
					units : []
				},
				units : [],
				selectedunits : [],
				error : {
					status : false,
					title : 'Error Occured',
					description : 'Error description'
				},
				rules : {
					sku: [{ required: true, message: 'Please Input SKU' }],
					name: [{ required: true, message: 'Please Input Product Name' }],
				}
			}
		},
		beforeMount(){
			var user = sessionStorage.getItem('user');
			if (user) {
				user = JSON.parse(user);
				this.selectedCompany = user.company;
			}
			var vm=this;

			axios.get(CONFIG.rest_url + '/productcategoryof/' + this.selectedCompany.id).then(function(response) {
				if (response.data)	vm.categories = response.data;
			})
			axios.get(CONFIG.rest_url + '/productuomof/' + this.selectedCompany.id).then(function(response) {
				if (response.data)	vm.uoms = response.data;
			})

			axios.get(CONFIG.rest_url + '/unitsof/' + this.selectedCompany.id).then(function(response) {
				if (response.data)	{
					vm.units = response.data;
					vm.loadByID(vm.$route.params.id);
				}
			})

		},
		methods:{
			loadByID(id){
				if (id == 0){
					this.form.id = 0;
					this.form.company_id = this.selectedCompany.id;
					this.form.sku = '';
					this.form.barcode = '';
					this.form.name = '';
					this.form.category = '';
					this.form.uom = '';
					this.form.price = 0;
					this.form.tax = 0;
					return;
				}
				var vm = this;
				axios.get(CONFIG.rest_url + '/product/' + id).then(function(response) {
					vm.form = response.data;
					vm.form.company_id = vm.selectedCompany.id;
					vm.fetchunits();
				})
				.catch(function(error) {
					vm.showErrorMessage(error);
				});
			},
			fetchunits(){
				var temp = [];

				//because toggleRowSelection overwrite form.units
				for (var i=0; i<this.form.units.length; i++) temp.push(this.form.units[i]);
				console.log(temp);
				for (var i=0; i<temp.length; i++){
					for (var j=0; j<this.units.length; j++){
						if (temp[i].unit_id == this.units[j].id){
							this.$refs.unittable.toggleRowSelection(this.units[j])
							break;
						}
					}
				}
			},
			saveData(){
				this.$refs.form.validate((valid) => {
					if (valid) {
						var vm = this;
						axios.post(CONFIG.rest_url + '/product', vm.form)
						.then(function(response) {
							vm.$message('Data berhasil diupdate');
							vm.back();
						})
						.catch(function(error) {
							vm.showErrorMessage(error);
						});
					}
				});
			},
			back(){
				this.$router.push({
				    path: '/product'
				})
			},
			showErrorMessage(error){
				this.error.status = true;
				this.error.title = error.message;
				if (error.response!=undefined){
					this.error.description = error.response.data;
				}else{
					this.error.description = error;
				}
			},
			handleSelectionChange(val){
				this.form.units = [];
				for (var i=0; i<val.length; i++){
					this.form.units.push({"unit_id" : val[i].id});
				}
			}
		}
	}

</script>

<style scoped>
	.el-form {
		width: 500px;
	}
	.el-form-item {
		margin-bottom: 20px;
	}
	/*.el-form-item{
		margin-bottom: 10px;
	}*/
	.productform{
		margin-bottom: 20px;
		margin-left: 150px;
	}
</style>
