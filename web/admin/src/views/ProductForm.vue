<template>
	<div>
		<div style="margin-bottom:30px; padding-bottom:10px; overflow: hidden; border-bottom:1px solid #A9A9A9;">
			<span style="font-size: 18px;color: #8492a6;line-height: 40px">Update Data Product</span>
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
		<el-form label-position="right" label-width="150px" :model="form" ref="form" :rules="rules">
			<el-form-item label="SKU" prop="sku">
				<el-input v-model="form.sku" style="width:230px"></el-input>
			</el-form-item>
			<el-form-item label="Product Name" prop="name">
				<el-input v-model="form.name"></el-input>
			</el-form-item>
			<el-form-item label="Photos" prop="img_url">
				<el-upload
					class="avatar-uploader"
					:action="imgPOSTURL"
					:on-success="handlePictureSuccess"
					:before-upload="beforePictureUpload">
					<img v-if="imageUrl" :src="imageUrl" class="avatar">
					<i v-else class="el-icon-plus avatar-uploader-icon"></i>
				</el-upload>

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

		<div style="margin-left:150px;width:500px">
			<table class="el-table"
				cellspacing="0" border="0" cellpadding="0">
				<thead><tr>
					<th style="padding:10px">Nama Modifier</th>
					<th style="padding:10px" align="right" width="150px">Price</th>
					<th style="padding:10px" align="right" width="50px"></th>
				</tr></thead>
				<tbody>
					<tr v-for="item in form.modifiers">
						<td style="padding:5px"><el-input v-model="item.name"></el-input></td>
						<td style="padding:5px"><currencyinput v-model="item.price" style="width:150px"></currencyinput></td>
						<td>
							<el-button @click="delModifier(item)" type="text" style="float:right;margin-right:10px">
								Delete
							</el-button>
						</td>
					</tr>
				</tbody>
			</table>
			<br />
			<el-button @click="addModifier"><i class="fa fa-plus"/> Tambah Modifier</el-button>
		</div>

		<br />


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
				imgPOSTURL : CONFIG.rest_url + '/upload',
				selectedCompany : {id : 0},
				uoms : [],
				categories : [],
				imageUrl: '',
				form : {
					id: 0,
					category : '',
					barcode : '',
					sku: '',
					name : '',
					uom : '',
					price : 0,
					tax : 0,
					img: '',
					units : [],
					modifiers : []
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
			this.initCompany();
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

					if (vm.form.company_id != vm.selectedCompany.id){
						vm.$message.error('This Company has no access for this product id');
						vm.$router.push({
						    path: '/product'
						})
					}
					if (vm.form.img != '') vm.imageUrl = CONFIG.rest_url + '/upload/' + vm.form.img;
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
			},
			addModifier(){
				this.form.modifiers.push({name:'',price:'0'});
			},
			delModifier(item){
				var idx = this.form.modifiers.indexOf(item);
				this.form.modifiers.splice(idx,1);
			},
			handlePictureSuccess(res, file) {
                this.imageUrl = res.url;
				this.form.img = res.filename;
            },
            beforePictureUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isLt1M = file.size / 1024 / 1024 < 0.5;

                if (!isJPG) {
                    this.$message.error('Product picture must be JPG format!');
                }
                if (!isLt1M) {
                    this.$message.error('Product picture size can not exceed 512 KB!');
                }
				// if (isJPG && isLt1M)
				// 	this.imageUrl = URL.createObjectURL(file.raw);
                return isJPG && isLt1M;
            }
		}
	}

</script>

<style scoped>
	.el-form {
		width: 650px;
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

<style>
	.avatar-uploader .el-upload {
	    border: 1px dashed #d9d9d9;
	    border-radius: 6px;
	    cursor: pointer;
	    position: relative;
	    overflow: hidden;
	  }
	  .avatar-uploader .el-upload:hover {
	    border-color: #20a0ff;
	  }
	  .avatar-uploader-icon {
	    font-size: 28px;
	    color: #8c939d;
	    width: 178px;
	    height: 178px;
	    line-height: 178px;
	    text-align: center;
	  }
	  .avatar {
	    width: 178px;
	    height: 178px;
	    display: block;
	  }
</style>
