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
		<el-dialog
			title="Input Units"
			:visible.sync="dialogVisible"
			size="tiny"
			>
			<el-input placeholder="Unit Name" v-model="form.name"></el-input>
			<el-input type="textarea" placeholder="Address" v-model="form.address" style="margin-bottom:10px"></el-input>
			<el-input type="tel" placeholder="Phone" v-model="form.phone"></el-input>

			<span slot="footer" class="dialog-footer">
				<el-button type="primary" @click="saveData">Confirm</el-button>
				<el-button @click="dialogVisible = false">Cancel</el-button>
			</span>
		</el-dialog>


		<el-table :data="items"	stripe style="width:100%" border>
			<el-table-column type="expand">
				<template scope="scope">
					<el-button size="small" icon="edit" @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
					<el-button size="small" icon="delete" type="danger" @click="handleDelete(scope.$index, scope.row)">Delete</el-button>
				</template>
			</el-table-column>
			<el-table-column
				v-for="item in fields"
				sortable
				:key="item.fieldname"
				:prop="item.fieldname"
				:label="item.caption"
				:width="item.width">
			</el-table-column>
			<!-- <el-table-column
				label="Operations"
				width="200">
				<template scope="scope">
					<el-button size="small" icon="edit" @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
					<el-button size="small" icon="delete" type="danger" @click="handleDelete(scope.$index, scope.row)">Delete</el-button>
				</template>
			</el-table-column> -->
		</el-table>

		<el-row type="flex">
			<el-button size="small" icon="plus" type="primary" @click="loadByID(0)" style="margin-top:10px">Tambah</el-button>
		</el-row>
	</div>
</template>

<script>
	import axios from 'axios';
	var CONFIG = require('../../config.json');

	export default {
		data () {
			return {
				dialogVisible : false,
				selectedCompany : {id : 0},
				items : [],
				fields : [
					{fieldname : 'name', caption : 'Unit Name', width: 200},
					{fieldname : 'phone', caption : 'Phone Number', width: 200},
					{fieldname : 'address', caption : 'Address', width: null}
				],
				form : {
					id: 0,
					company_id: 0,
					name : '',
					address : '',
					phone : ''
				},
				error : {
					status : false,
					title : 'Error Occured',
					description : 'Error description'
				}
		  	}

		},
		beforeMount(){
			var user = sessionStorage.getItem('user');
			if (user) {
				user = JSON.parse(user);
				this.selectedCompany = user.company;
			}
			this.refreshData();
		},
		methods:{
			refreshData(){
				var url = CONFIG.rest_url + '/unitsof/' + this.selectedCompany.id;
				var vm = this;
				axios.get(url).then(function(response) {
					vm.items = response.data;
				})
				.catch(function(error) {
					vm.showErrorMessage(error);
				});
			},
			loadByID(id){
				if (id == 0){
					this.form.id = 0;
					this.form.company_id = this.selectedCompany.id;
					this.form.name = '';
					this.form.address = '';
					this.form.phone = '';
					this.dialogVisible = true;
					return;
				}

				var vm = this;
				axios.get(CONFIG.rest_url + '/units/' + id).then(function(response) {

					vm.form = response.data;
					//override company id
					vm.form.company_id = vm.selectedCompany.id;
					vm.dialogVisible = true;
				})
				.catch(function(error) {
					vm.showErrorMessage(error);
				});
			},
			saveData(){
				var vm = this;
				axios.post(CONFIG.rest_url + '/units', vm.form)
				.then(function(response) {
					vm.$message('Data berhasil diupdate');
					vm.refreshData(false);
				})
				.catch(function(error) {
					vm.showErrorMessage(error);
				});
				vm.dialogVisible = false;
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
			handleEdit(index, item){
				this.loadByID(item.id);
			},
			handleDelete(index, item){
				var vm = this;
				this.$confirm('Anda yaking menghapus data ini?', 'Warning', {
		        	confirmButtonText: 'Ya',
			        cancelButtonText: 'Batal',
			        type: 'warning'
		        }).then(()=>{
					this.deleteData(item);
				}).catch(()=>{
				});
			},
			deleteData(item){
				var id = item.id;
				var vm = this;
				axios.delete(CONFIG.rest_url + '/units/' + id)
				.then(function(response) {
					vm.$message('Data berhasil dihapus');
					vm.refreshData();
				})
				.catch(function(error) {
					vm.showErrorMessage(error);
				});
				vm.dialogVisible = false;
			}
		}
	}
</script>

<style scoped>
	.el-row {
		margin-bottom: 5px;
	}
	.el-input{
		margin-bottom: 10px;
	}
	.el-table{
		font-size: 12px;
	}
</style>
