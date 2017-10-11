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

		<el-input placeholder="Filter Keyword" v-model="keyword">
			<el-select v-model="selectedfield" placeholder="Filter By" slot="prepend" style="width:180px">
				<div v-for="item in fields">
					<el-option v-if="!item.hiddenfilter"
						:key="item.fieldname"
						:label="item.caption"
						:value="item.fieldname">
					</el-option>
				</div>

			</el-select>
			<el-button slot="append" icon="search" @click="onSearchClick">Search Data</el-button>
		</el-input>

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
		</el-table>
		<el-row type="flex">
				<el-button size="small" icon="plus" type="primary" @click="handleNew()" style="margin-top:10px">Tambah</el-button>
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
				selectedCompany : {id : 0},
				items : [],
				fields : [
					{fieldname : 'code', caption : 'Kode', width: 120},
					{fieldname : 'name', caption : 'Nama', width: 200},
					{fieldname : 'category', caption : 'Category', width: 120},
					{fieldname : 'unit_name', caption : 'Unit Name', width: 150}, //hiddenfilter : true},
					{fieldname : 'address', caption : 'Address', width: null},
				],
				error : {
					status : false,
					title : 'Error Occured',
					description : 'Error description'
				},
				selectedfield : 'name',
				currentpage : 1,
				totalrecord : 1,
				pagesize : 10,
				pagesizes : [10,20,50,100],
				keyword : ''
		  	}

		},
		beforeMount(){
			var user = sessionStorage.getItem('user');
			if (user) {
				user = JSON.parse(user);
				this.selectedCompany = user.company;
			}
			this.refreshData(true);
		},
		methods:{
			refreshData(reset){
				if (reset) {
					this.currentpage = 1;
				}
				var url = CONFIG.rest_url + '/customerof/' + this.selectedCompany.id
						+ '/' + this.pagesize + '/' + this.currentpage + '/';
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
				this.$router.push({
				    path: '/customer/' + item.id
				})
			},
			handleNew(){
				this.$router.push({
				    path: '/customer/0' 
				})
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
				axios.delete(CONFIG.rest_url + '/customer/' + id)
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
	.el-input {
		margin-bottom: 10px;
	}
	.el-table{
		font-size: 12px;
	}
</style>
