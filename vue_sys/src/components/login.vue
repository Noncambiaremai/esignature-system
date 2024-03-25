<template>
  <div class="login">
    <el-form ref="loginForm" class="login-form">
      <h3 class="title">欢迎登录手势识别电子签名系统</h3>
      <el-form-item prop="userId">
        <el-input placeholder="手机号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="userPassword">
        <el-input placeholder="密码">
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>

      <el-form-item style="width:100%;">
        <el-button size="medium" type="primary" style="width: 150px" >登 录</el-button>
        <el-button size="medium" type="primary" style="width: 150px" @click="toenroll">注 册</el-button>

        <el-button size="medium" type="primary"
                   @click="gotomenu">go to menu</el-button>
        <!--<el-button @click="callbackend">call back end</el-button>-->
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
    name: "login",
    methods: {
      // 注册按钮跳转
      toenroll(){
        this.$router.push({name: 'test'});
      },
      gotomenu() {
        this.$router.push({name: 'menu'});
      }
      }
            // callbackend() {
            //   axios.get('/api/hello/index').then(response => {
            //     console.log(response.data);
            //   });
            // }
    }
</script>

<style>
  .login {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
    background-image: url("../assets/login-bg.jpg");
    background-size: cover;
    background-position: center;
    position: fixed;
    top: 0;
    left: 0;
  }

  .title {
    margin: 0px auto 30px auto;
    text-align: center;
    color: #707070;
  }

  .login-form {
    border-radius: 6px;
    background: #ffffff;
    width: 400px;
    padding: 25px 25px 5px 25px;
  }

  .enroll-form {
    border-radius: 6px;
    background: #ffffff;
    width: 400px;
    padding: 25px 25px 5px 25px;
  }

  .login-form .el-input input {
    height: 38px;
  }
  .login-form .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }

  .login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
  }

  .login-code {
    width: 33%;
    height: 38px;
    float: right;
  }
  .login-code img {
    cursor: pointer;
    vertical-align: middle;
  }

  .el-login-footer {
    height: 40px;
    line-height: 40px;
    position: fixed;
    bottom: 0;
    width: 100%;
    text-align: center;
    color: #fff;
    font-family: Arial;
    font-size: 12px;
    letter-spacing: 1px;
  }

  .login-code-img {
    height: 38px;
  }
</style>
