<template>
	<div>
		<div style="margin-bottom:30px; padding-bottom:10px; overflow: hidden; border-bottom:1px solid #A9A9A9;">
			<span style="font-size: 18px;color: #8492a6;line-height: 40px">Update Data Customer</span>
			<span style="float:right">
			<el-button @click="saveData" type="primary">
				<i class="fa fa-check"/>Simpan Data
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
		<el-form label-position="right"  label-width="150px" :model="form" ref="form" :rules="rules">
			<el-form-item label="Pilih Unit/Cabang" prop="unit_id">
				<el-select v-model="form.unit_id" filterable placeholder="Select">
					<el-option v-for="item in units" :key="item.id"	:label="item.name" :value="item.id">
					</el-option>
				</el-select>
			</el-form-item>

			<el-form-item label="Kode Customer" prop="code">
				<el-input v-model="form.code"></el-input>
			</el-form-item>
			<el-form-item label="Nama Customer" prop="name">
				<el-input v-model="form.name"></el-input>
			</el-form-item>
			<el-form-item label="Alamat">
				<el-input type="textarea" v-model="form.address"></el-input>
			</el-form-item>
			<el-form-item label="Kategori">
				<el-select v-model="form.category" filterable allow-create placeholder="Select">
					<el-option v-for="item in categories" :key="item.category"	:label="item.category" :value="item.category">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="No. Telepon">
				<el-input type="tel" v-model="form.phone"></el-input>
			</el-form-item>
			<el-form-item label="Email Address">
				<el-input type="email" v-model="form.email"></el-input>
			</el-form-item>
		</el-form>



	</div>
</template>

<script>
	import axios from 'axios';
	var CONFIG = require('Config');

	export default {
		data () {
			return{
				selectedCompany : {id : 0},
				units : [],
				categories : [],
				form : {
					id: 0,
					unit_id: null,
					category : '',
					code: '',
					name : '',
					address : '',
					phone : '',
					email : ''
				},
				error : {
					status : false,
					title : 'Error Occured',
					description : 'Error description'
				},
				rules : {
					unit_id: [{ required: true, message: 'Please Select Unit' }],
					code: [{ required: true, message: 'Please Input Customer Code' }],
					name: [{ required: true, message: 'Please Input Customer Name' }],
				}
			}
		},
		beforeMount(){
			var user = sessionStorage.getItem('user');
			if (user) {
				user = JSON.parse(user);
				this.selectedCompany = user.company;
			}
			//units
			var vm = this;
			axios.get(CONFIG.rest_url + '/unitsof/' + this.selectedCompany.id).then(function(response) {
				vm.units = response.data;
			})
			.catch(function(error) {
				vm.showErrorMessage(error);
			});
			axios.get(CONFIG.rest_url + '/customercategoryof/' + this.selectedCompany.id).then(function(response) {
				if (response.data)
					vm.categories = response.data;
			})
			.catch(function(error) {
				vm.showErrorMessage(error);
			});

			this.loadByID(this.$route.params.id);
		},
		methods:{
			loadByID(id){
				if (id == 0){
					this.form.id = 0;
					this.form.company_id = this.selectedCompany.id;
					this.form.unit_id = null;
					this.form.code = '';
					this.form.name = '';
					this.form.category = '';
					this.form.address = '';
					this.form.phone = '';
					this.form.email = '';
					return;
				}
				var vm = this;
				axios.get(CONFIG.rest_url + '/customer/' + id)
				.then(function(response) {
					vm.form = response.data;

					if (vm.form.company_id != vm.selectedCompany.id){
						vm.$message.error('This Company has no access for this customer id');
						vm.$router.push({
						    path: '/customer'
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
						axios.post(CONFIG.rest_url + '/customer', vm.form)
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
				    path: '/customer'
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
		}
	}

</script>

<style scoped>
	.el-form {
		width: 500px;
	}
	.el-form-item{
		margin-bottom: 20px;
	}
	.footer{
		margin-left: 150px;
	}
</style>
