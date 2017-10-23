<template>
	<div class="login-container">
		<el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-position="left" label-width="0px"
			class="login-box">
			<img src="../assets/login.png" style="display: block;  margin: 0 auto;">
			<h3 class="title" style="margin-top:10px">Please Login</h3>
			<el-form-item prop="account">
				<el-input type="text" v-model="ruleForm2.account" auto-complete="off" placeholder="Account"></el-input>
			</el-form-item>
			<el-form-item prop="checkPass">
				<el-input type="password" v-model="ruleForm2.checkPass" auto-complete="off" placeholder="Password"></el-input>
			</el-form-item>
				<el-checkbox v-model="checked" checked class="remember">Remember Me</el-checkbox>
			<el-form-item style="width:100%;">
				<el-button type="primary" style="width:100%;" @click.native.prevent="handleSubmit2" :loading="logining">Login</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
	import axios from 'axios';
	var CONFIG = require('../../config.json');
  	export default {
		data() {
			return {
				logining: false,
				ruleForm2: {
				account: 'fmanda',
				checkPass: 'fmanda'
			},
			rules2: {
				account: [
						{ required: true, message: 'User Required', trigger: 'blur' },
					],
				checkPass: [
						{ required: true, message: 'Password Required', trigger: 'blur' },
					]
				},
				checked: true
			};
	    },
	    methods: {
			handleReset2() {
				this.$refs.ruleForm2.resetFields();
			},
			handleSubmit2(ev) {
				var _this = this;
				this.$refs.ruleForm2.validate((valid) => {
					if (valid) {
						this.logining = true;
						//NProgress.start();
						var loginParams = { user_name: this.ruleForm2.account, password: this.ruleForm2.checkPass };
						var vm = this;
						axios.post(CONFIG.rest_url + '/login', loginParams)
						.then(function(response) {
							vm.logining = false;
							var user = response.data;
							if (user){
								console.log(user);
								vm.$message('welcome ' + user.user_name );
								sessionStorage.setItem('user', JSON.stringify(user));
								vm.$router.push({ path: '/' });
							}else{
								vm.$message({
									message: 'User / Password salah',
									type: 'error'
								});
							}
						})
						.catch(function(error) {
							vm.$message({
								message: error.message,
								type: 'error'
							});
						});
					} else {
						console.log('error submit!!');
						return false;
					}
				});
			}
		}
	}

</script>

<style lang="scss" scoped>
	.login-container {
		// background:url(http://thekitemap.com/images/feedback-img.jpg) no-repeat;
		background: #f1f1f1;
		width: 100%;
		height: 100%;
		padding-top: 70px;
		// background: #00ff00;
	}
	// .login-box {
	// 	background-color:rgba(72,72,72,0.4);
	// 	padding: 35px 35px 15px 35px;
	// 	width: 300px;
	// 	margin: 0px auto;
	// 	-moz-border-radius: 7px;
	// 	-webkit-border-radius: 7px;
	// }
  	.login-box {
    	box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);
	    -webkit-border-radius: 5px;
	    border-radius: 5px;
	    -moz-border-radius: 5px;
		// background-color:rgba(72,72,72,0.4);
	    background-clip: padding-box;
	    margin: 0px auto;
	    width: 300px;
	    padding: 35px 35px 15px 35px;
	    background: #fff;
	    border: 1px solid #eaeaea;
	    box-shadow: 0 0 25px #cac6c6;
    .title {
		margin: 0px auto 40px auto;
		text-align: center;
		color: #505458;
    }
    .remember {
		margin: 0px 0px 35px 0px;
    }
  }
</style>
