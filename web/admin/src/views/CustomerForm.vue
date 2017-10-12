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
		<el-form label-position="right"  label-width="150px" :model="form" :ref="form ":rules="rules">
			<el-form-item label="Pilih Unit/Cabang" prop="unit_id">
				<el-select v-model="form.unit_id" filterable placeholder="Select" style="margin-bottom:10px">
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
				<el-input v-model="form.category"></el-input>
			</el-form-item>
			<el-form-item label="No. Telepon">
				<el-input type="tel" v-model="form.phone"></el-input>
			</el-form-item>
			<el-form-item label="Email Address">
				<el-input type="email" v-model="form.email"></el-input>
			</el-form-item>
		</el-form>


		<span class="footer">
			<el-button type="primary" @click="saveData">Confirm</el-button>
			<el-button @click="back()">Cancel</el-button>
		</span>
	</div>
</template>

<script>
	import axios from 'axios';
	var CONFIG = require('../../config.json');

	export default {
		data () {
			return{
				selectedCompany : {id : 0},
				units : [],
				form : {
					id: 0,
					unit_id: null,
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

			var url = CONFIG.rest_url + '/unitsof/' + this.selectedCompany.id;
			var vm = this;
			axios.get(url).then(function(response) {
				vm.units = response.data;
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
					vm.form.company_id = vm.selectedCompany.id;
				})
				.catch(function(error) {
					vm.showErrorMessage(error);
				});
			},
			saveData(){
				var vm = this;
				axios.post(CONFIG.rest_url + '/customer', vm.form)
				.then(function(response) {
					vm.$message('Data berhasil diupdate');
					vm.back();
				})
				.catch(function(error) {
					vm.showErrorMessage(error);
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
				if (error.response=undefined){
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
	/*.el-form-item{
		margin-bottom: 10px;
	}*/
	.footer{
		margin-left: 150px;
	}
</style>
