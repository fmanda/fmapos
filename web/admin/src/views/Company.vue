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
			title="Input Company"
			:visible.sync="dialogVisible"
			size="tiny"
			>
			<el-input placeholder="Company Name" v-model="form.name"></el-input>
			<el-input type="textarea" placeholder="Address" v-model="form.address" style="margin-bottom:10px"></el-input>
			<el-input placeholder="Contact Person" v-model="form.contact_person"></el-input>
			<el-input type="tel" placeholder="Phone" v-model="form.phone"></el-input>
			<el-input type="email"placeholder="Email Address" v-model="form.email"></el-input>

			<span slot="footer" class="dialog-footer">
				<el-button type="primary" @click="saveData">Confirm</el-button>
				<el-button @click="dialogVisible = false">Cancel</el-button>
			</span>
		</el-dialog>
		<!-- <h1>{{items}}</h1> -->
		<!-- <el-row type="flex"> -->
			<el-input placeholder="Filter Keyword" v-model="keyword">
				<el-select v-model="selectedfield" placeholder="Filter By" slot="prepend" style="width:180px">
					<el-option
						v-for="item in fields"
						:key="item.fieldname"
						:label="item.caption"
						:value="item.fieldname">
					</el-option>
				</el-select>
				<el-button slot="append" icon="search" @click="onSearchClick">Search Data</el-button>
			</el-input>
		<!-- </el-row> -->

		<!-- grid -->

		<el-table :data="items"	stripe style="width:100%" border>
			<el-table-column type="expand">
				<template scope="scope">
					<!-- <p>{{scope.row.name}}</p>
					<p>{{scope.row.address}}</p>
					<p>{{scope.row.phone}}</p> -->
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
			<!-- <el-table-column prop="contact_person" label="Contact Person" width="200"></el-table-column>
			<el-table-column prop="phone" label="Phone" width="150"></el-table-column>
			<el-table-column prop="address" label="Address"></el-table-column> -->
		</el-table>
		<el-row type="flex">
			<!-- <el-col :span="12"> -->
				<el-button size="small" icon="plus" type="primary" @click="loadByID(0)" style="margin-top:10px">Tambah</el-button>
			<!-- </el-col> -->
			<!-- <el-col :span="12"> -->
				<span style="margin-left:10px">
					<el-pagination
						@size-change="onSizeChanged"
						@current-change="onPageChanged"
						:page-sizes="pagesizes"
						:page-size="pagesize"
						layout="sizes, prev, pager, next, jumper, total"
						:total="totalrecord"
						style = "margin-top:10px">
					</el-pagination>
				</span>
			<!-- </el-col> -->
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
				items : [],
				fields : [
					{fieldname : 'name', caption : 'Company Name', width: 200},
					{fieldname : 'contact_person', caption : 'Contact Person', width: 200},
					{fieldname : 'phone', caption : 'Phone Number', width: 200},
					{fieldname : 'address', caption : 'Address', width: null},
				],
				selectedfield : 'name',
				form : {
					id: 0,
					name : '',
					address : '',
					phone : '',
					contact_person : '',
					email : '',
					// items : []
				},
				error : {
					status : false,
					title : 'Error Occured',
					description : 'Error description'
				},
				currentpage : 1,
				totalrecord : 1,
				pagesize : 10,
				pagesizes : [10,20,50,100],
				keyword : ''
		  	}

		},
		beforeMount(){
			this.refreshData(true)
		},
		methods:{
			refreshData(reset){
				if (reset) {
					this.currentpage = 1;
				}
				var url = CONFIG.rest_url + '/company/' + this.pagesize + '/' + this.currentpage + '/';
				var vm = this;

				if (this.keyword != '') url += this.selectedfield + '/' + this.keyword;

				axios.get(url)
				.then(function(response) {
					vm.items = response.data.data;
					vm.totalrecord = parseInt(response.data.totalrecord);
				})
				.catch(function(error) {
					vm.showErrorMessage(error);
				});
			},
			onPageChanged(val){
				this.currentpage = val;
				this.refreshData(false);
			},
			onSizeChanged(val){
				this.pagesize = val;
				this.refreshData(false);
			},
			onSearchClick(){
				this.refreshData(true);
			},
			loadByID(id){
				if (id == 0){
					this.form.id = 0;
					this.form.name = '';
					this.form.address = '';
					this.form.phone = '';
					this.form.contact_person = '';
					this.form.email = '';
					this.dialogVisible = true;
					return;
				}
				var vm = this;
				axios.get(CONFIG.rest_url + '/company/' + id)
				.then(function(response) {
					vm.form = response.data;
					vm.dialogVisible = true;
				})
				.catch(function(error) {
					vm.showErrorMessage(error);
				});
			},
			saveData(){
				var vm = this;
				axios.post(CONFIG.rest_url + '/company', vm.form)
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
				axios.delete(CONFIG.rest_url + '/company/' + id)
				.then(function(response) {
					vm.$message('Data berhasil dihapus');
					vm.refreshData(false);
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
