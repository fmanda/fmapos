<template>
	<div>
		<div style="margin-bottom:30px; padding-bottom:10px; overflow: hidden; border-bottom:1px solid #A9A9A9;">
			<span style="font-size: 18px;color: #8492a6;line-height: 40px">Update Data Order</span>
			<span style="float:right">
			<el-button @click="saveData" type="primary">
				<i class="fa fa-check"/>
				Simpan Data
			</el-button>
			<el-button @click="back()" ><i class="fa fa-times"/> Batal</el-button>
			</span>
		</div>

		<el-alert v-if="error.status"
			v-bind:title="error.title"
			type="error"
			v-bind:description="error.description"
			show-icon
			style = "margin-bottom:10px"
			@close="error.status = false"
			>
		</el-alert>
		<el-form label-position="top" :model="form" ref="form" :rules="rules">

			<el-row>
				<el-col :span="8">
					<el-form-item label="Unit / Cabang" prop="unit_id">
						<el-select v-model="form.unit_id" filterable placeholder="Select" style="width:80%">
							<el-option v-for="item in units" :key="item.id"	:label="item.name" :value="item.id">
							</el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="Order No" prop="orderno">
						<el-input v-model="form.orderno" style="width:80%"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="Order Date" prop="orderdate">
						<el-date-picker
							v-model="form.orderdate"
							placeholder="Pick a Date"
						>
						</el-date-picker>
					</el-form-item>
				</el-col>
			</el-row>

			<el-form-item label="Notes">
				<el-input v-model="form.notes" style="width:85%"></el-input>
			</el-form-item>
		</el-form>

		<div style="width:800px">
			<table class="el-table"
				cellspacing="0" border="0" cellpadding="0">
				<thead><tr>
					<th style="padding:10px">Product</th>
					<th style="padding:10px" align="right" width="100px">Price</th>
					<th style="padding:10px" align="right" width="100px">QTY</th>
					<th style="padding:10px" align="right" width="100px">Total</th>
					<th style="padding:10px" align="right" width="30"></th>
				</tr></thead>
				<tbody>
					<tr v-for="item in form.items">
						<td style="padding:5px">
							<el-select v-model="item.product_id" filterable placeholder="Select" style="width:100%">
								<el-option v-for="item in products" :key="item.id"	:label="item.name" :value="item.id">
								</el-option>
							</el-select>
						</td>
						<td style="padding:5px"><currencyinput v-model="item.price" style="width:100px"></currencyinput></td>
						<td style="padding:5px"><currencyinput v-model="item.qty" style="width:100px"></currencyinput></td>
						<td style="padding:5px"><currencyinput v-model="item.total" style="width:100px"></currencyinput></td>
						<td>
							<el-button @click="delItem(item)" type="text" style="margin-right:5px">
								Delete
							</el-button>
						</td>
					</tr>
				</tbody>
			</table>
			<br />
			<el-button @click="addItem"><i class="fa fa-plus"/> Tambah Item</el-button>
		</div>

		<br />


	</div>
</template>

<script>
	import axios from 'axios';
	import currencyinput from '../components/CurrencyInput.vue'
	var CONFIG = require('Config');

	export default {
		components: {
			currencyinput
		},
		data () {
			return{
				selectedCompany : {id : 0},
				units : [],
				products : [],
				imageUrl: '',
				form : {
					id: 0,
					unit_id: '',
					orderno : '',
					orderdate : '',
					amount : 0,
					tax : 0,
					items : []
				},
				error : {
					status : false,
					title : 'Error Occured',
					description : 'Error description'
				},
				rules : {
					orderno: [{ required: true, message: 'Please Input Order No' }],
					orderdate: [{ required: true, message: 'Please Input Order Date' }],
				}
			}
		},
		beforeMount(){
			this.initCompany();
			var vm=this;

			var vm = this;
			axios.get(CONFIG.rest_url + '/unitsof/' + this.selectedCompany.id).then(function(response) {
				vm.units = response.data;
			})
			.catch(function(error) {
				vm.showErrorMessage(error);
			});

			var vm = this;
			axios.get(CONFIG.rest_url + '/productof/' + this.selectedCompany.id).then(function(response) {
				vm.products = response.data;
			})

			vm.loadByID(vm.$route.params.id);

		},
		methods:{
			initCompany(){
				var user = sessionStorage.getItem('user');
				if (user) {
					user = JSON.parse(user);
					this.selectedCompany = user.company;
				}
			},
			loadByID(id){
				if (id == 0){
					this.form.id = 0;
					this.form.company_id = this.selectedCompany.id;
					this.form.orderno = '';
					this.form.orderdate = new Date();
					this.form.amount = 0;
					this.form.tax = 0;
					return;
				}
				var vm = this;
				axios.get(CONFIG.rest_url + '/order/' + id).then(function(response) {
					vm.form = response.data;


					if (vm.form.company_id != vm.selectedCompany.id){
						vm.$message.error('This Company has no access for this order id');
						vm.$router.push({
						    path: '/order'
						})
					}

				})
				.catch(function(error) {
					vm.showErrorMessage(error);
				});
			},
			saveData(){
				this.$refs.form.validate((valid) => {
					if (valid) {
						var vm = this;
						axios.post(CONFIG.rest_url + '/order', vm.form)
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
				    path: '/order'
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
			addItem(){
				this.form.items.push({product_id: null,name:'',price:'0',qty:'1',total:'0'});
			},
			delItem(item){
				var idx = this.form.items.indexOf(item);
				this.form.items.splice(idx,1);
			}

		}
	}

</script>

<style scoped>
	/*.el-form {
		width: 650px;
	}*/
	/*.el-form-item {
		margin-bottom: 20px;*/
	/*}*/
	/*.el-form-item{
		margin-bottom: 10px;
	}*/
	/*.productform{
		margin-bottom: 20px;
		margin-left: 150px;
	}*/
</style>
