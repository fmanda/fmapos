<template>
	<el-row class="container">
		<el-dialog
			title="Select Company"
			:visible.sync="dialogVisible"
			size="tiny"
			>
			<el-select v-model="selectedCompany" filterable placeholder="Select">
				<el-option
					v-for="company in companies"
					:key="company.id"
					:label="company.name"
					:value="company.id">
				</el-option>
			</el-select>

			<span slot="footer" class="dialog-footer">
				<el-button type="primary" @click="setCompany">Set Company</el-button>
			</span>
		</el-dialog>

		<el-col :span="24" class="header">
			<el-col :span="10" class="logo" :class="collapsed?'logo-collapse-width':'logo-width'">
				{{collapsed?'': sysName}}
			</el-col>
			<el-col :span="10">
				<div class="tools" @click.prevent="collapse">
					<i class="fa fa-align-justify"></i>
				</div>
			</el-col>

			<el-col :span="4" class="userinfo">
				<el-dropdown trigger="hover">
					<span class="el-dropdown-link userinfo-inner">
						<!-- <img :src="this.sysUserAvatar" />  -->
						{{user.name}} @ {{user.company.name}}
					</span>
					<el-dropdown-menu slot="dropdown">
						<el-dropdown-item @click.native="selectCompany">Select Company</el-dropdown-item>
						<el-dropdown-item divided @click.native="logout">Logout</el-dropdown-item>
					</el-dropdown-menu>
				</el-dropdown>
			</el-col>
		</el-col>
		<el-col :span="24" class="main">
			<aside :class="collapsed?'menu-collapsed':'menu-expanded'">
				<el-menu
					:default-active="$route.path"
					class="el-menu-vertical-demo" @open="handleopen" @close="handleclose" @select="handleselect"
					unique-opened router
					:collapse = "collapsed"
				>
					<template v-for="(item,index) in $router.options.routes" v-if="!item.hidden">
						<el-submenu :index="index+''" v-if="!item.leaf">
							<template slot="title">
								<i :class="item.iconCls"></i>
								<span slot="title">{{item.name}}</span>
							</template>
							<el-menu-item
								v-for="child in item.children" :index="child.path" :key="child.path"
								v-if="!child.hidden">
								{{child.name}}
							</el-menu-item>
						</el-submenu>
					</template>
				</el-menu>
			</aside>
			<section class="content-container">
				<div class="grid-content bg-purple-light">
					<!-- <el-col :span="24" class="breadcrumb-container">
						<strong class="title">{{$route.name}}</strong>
						<el-breadcrumb separator="/" class="breadcrumb-inner">
							<el-breadcrumb-item v-for="item in $route.matched" :key="item.path">
								{{ item.name }}
							</el-breadcrumb-item>
						</el-breadcrumb>
					</el-col> -->
					<el-col :span="24" class="content-wrapper">
						<transition name="fade" mode="out-in">
							<router-view></router-view>
						</transition>
					</el-col>
				</div>
			</section>
		</el-col>
	</el-row>
</template>

<script>
	import axios from 'axios';
	var CONFIG = require('../../config.json');
	export default {
		data() {
			return {
				debug: true,
				dialogVisible: false,
				sysName:'KUMO',
				collapsed: false,
				user: {
					name : 'fmanda',
					avatar : 'sysUserAvatar',
					company : ''
				},
				selectedCompany: '',
				form: {
					name: '',
					region: '',
					date1: '',
					date2: '',
					delivery: false,
					type: [],
					resource: '',
					desc: ''
				},
				companies : []
			}
		},
		methods: {
			onSubmit() {
				console.log('submit!');
			},
			handleopen() {
				//console.log('handleopen');
			},
			handleclose() {
				//console.log('handleclose');
			},
			handleselect: function (a, b) {
			},
			logout: function () {
				var _this = this;
				this.$confirm('Anda yakin untuk logout', 'Logout', {
					//type: 'warning'
				}).then(() => {
					sessionStorage.removeItem('user');
					_this.$router.push('/login');
				}).catch(() => {

				});


			},
			collapse:function(){
				this.collapsed=!this.collapsed;
			},
			showMenu(i,status){
				this.$refs.menuCollapsed.getElementsByClassName('submenu-hook-'+i)[0].style.display=status?'block':'none';
			},
			setCompany(){
				for (var i=0; i<this.companies.length; i++){
					if (this.companies[i].id == this.selectedCompany){
						this.user.company = this.companies[i];
						break;
					}
				}
				sessionStorage.setItem('user', JSON.stringify(this.user));
				this.loadSession();
				this.dialogVisible = false;
			},
			selectCompany(){
				this.dialogVisible = true;
			},
			initData(){
				var vm = this;
				if (!this.debug) return;
				var url = CONFIG.rest_url + '/company';
				axios.get(url).then(function(response) {
					vm.companies = response.data;
				})
				.catch(function(error) {
					console.log(error);
				});
			},
			loadSession(){
				var user = sessionStorage.getItem('user');
				if (user) {
					this.user = JSON.parse(user);
				}
			},
		},
		beforeMount(){
			this.initData();
		},
		mounted() {
			this.loadSession();

		}
	}

</script>

<style scoped lang="scss">
	@import 'src/styles/vars.scss';
	.container {
		position: absolute;
		top: 0px;
		bottom: 0px;
		width: 100%;
		.header {
			height: 60px;
			line-height: 60px;
			background: $color-primary;
			color:#fff;
			.userinfo {
				text-align: right;
				padding-right: 35px;
				float: right;
				.userinfo-inner {
					cursor: pointer;
					color:#fff;
					img {
						width: 40px;
						height: 40px;
						border-radius: 20px;
						margin: 10px 0px 10px 10px;
						float: right;
					}
				}
			}
			.logo {
				//width:230px;
				height:60px;
				font-size: 22px;
				padding-left:20px;
				padding-right:20px;
				border-color: rgba(238,241,146,0.3);
				border-right-width: 1px;
				border-right-style: solid;
				img {
					width: 40px;
					float: left;
					margin: 10px 10px 10px 18px;
				}
				.txt {
					color:#fff;
				}
			}
			.logo-width{
				width:230px;
			}
			.logo-collapse-width{
				width:64px
			}
			.tools{
				padding: 0px 23px;
				width:14px;
				height: 60px;
				line-height: 60px;
				cursor: pointer;
			}
		}
		.main {
			display: flex;
			// background: #324057;
			position: absolute;
			top: 60px;
			bottom: 0px;
			overflow: hidden;
			aside {
				flex:0 0 230px;
				width: 230px;
				.el-menu{
					height: 100%;
				}
			}
			.menu-collapsed{
				flex:0 0 60px;
				width: 60px;
			}
			.menu-expanded{
				flex:0 0 230px;
				width: 230px;
			}
			.content-container {
				// background: #f1f2f7;
				flex:1;
				// position: absolute;
				// right: 0px;
				// top: 0px;
				// bottom: 0px;
				// left: 230px;
				overflow-y: scroll;
				padding: 20px;
				.breadcrumb-container {
					margin-bottom: 15px;
					.title {
						width: 200px;
						float: left;
						color: #475669;
					}
					.breadcrumb-inner {
						float: right;
					}
				}
				.content-wrapper {
					background-color: #fff;
					box-sizing: border-box;
				}
			}
		}
	}

</style>
