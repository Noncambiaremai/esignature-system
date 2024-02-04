<template>
  <div class="login">
    <el-form ref="loginForm" class="login-form">
      <h3 class="title">欢迎登录高校绿色通道系统</h3>
      <el-form-item prop="username">
        <el-input
          type="text"
          auto-complete="off"
          placeholder="账号"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          type="password"
          auto-complete="off"
          placeholder="密码"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>

      <el-form-item prop="role">
        <el-select placeholder="身份">
          <el-option>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
          size="medium"
          type="primary"
          style="width:100%;"
        >
          <!--loading==false-->
          <span>登 录</span>
          <span>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span></span>
    </div>

    <!--登录说明-->
    <el-dialog custom-class="openAnimAbcd" title="登录说明" :visible.sync="open" width="600px" append-to-body top="20vh!important">
      <div style="margin-top:-50px;margin-bottom: 0px">
        <el-divider></el-divider>
        <p>
          1、登录的账号：学生学号或教师工作证号，默认初始密码为账号后6位。
        </p>
        <p>
          2、若忘记密码请联系管理员（邮箱<b>admin123@ugcs.com</b>）重置密码，密码将重置为初始密码。
        </p>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
      name: "login"

    }
</script>

<style>
  .login {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    /*background-image: url("../assets/images/login-bg.jpg");*/
    background-size: cover;
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
  .login-form .el-input {
    height: 38px;
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
