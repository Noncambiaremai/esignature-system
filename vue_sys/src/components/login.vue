<template>
  <div class="login">
    <el-form v-if="enrollflag" ref="enrollForm" class="enroll-form">
      <h3 class="title">新用户注册</h3>
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

      <el-form-item>
        <el-button style="width: 400px" @click="turnCamera">打开 / 关闭摄像头</el-button>
      </el-form-item>
      
      <el-form-item v-if="cameraVisible" label="摄像头">
        <video ref="video" style="width: 100%; height: 100%;" autoplay></video>
        <!--<canvas ref="canvas" width="640" height="480"></canvas>-->
      </el-form-item>


      <el-form-item style="width:100%;">
        <el-button size="medium" type="primary" style="width: 150px">注 册</el-button>
        <el-button size="medium" type="primary" style="width: 150px" @click="goback">返 回</el-button>
      </el-form-item>
    </el-form>

    <el-form v-if="!enrollflag" ref="loginForm" class="login-form">
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
        <el-button size="medium" type="primary" style="width: 150px" @click="tologin">登 录</el-button>
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
import * as tf from '@tensorflow/tfjs';
import * as mediapipe from '@mediapipe/face_mesh';

export default {
    name: "login",
    data() {
      return {
        enrollflag: false,
        cameraVisible: false,
        video: null,
        stream: null,
        canvas: null,
        ctx: null,
        faceMesh: null,
      };
    },
    created(){
      this.enrollflag = false;
    },

    methods: {
      // 注册按钮跳转
      toenroll(){
        this.enrollflag = true;
      },
      tologin(){
        this.$router.push({name: 'test'});
      },

      goback(){
        this.enrollflag = false;
      },

      gotomenu() {
        this.$router.push({name: 'menu'});
      },

      turnCamera() {
        this.cameraVisible = !this.cameraVisible;
        if (this.cameraVisible) {
          // this.canvas = this.$refs.canvas;
          // this.ctx = this.canvas.getContext('2d');

          navigator.mediaDevices.getUserMedia({video: true})
            .then((stream) => {
              this.stream = stream;
              this.video = this.$refs.video;
              this.video.srcObject = stream;
              this.video.play();
            })
            .catch((error) => {
              console.error('Error accessing the camera: ', error);
            });

          // this.initializeMediaPipe();
          // this.detectFaces();
        }
        else {
          const tracks = this.stream.getTracks();
          tracks.forEach(track => track.stop());
          // 清空视频元素的 srcObject
          this.video.srcObject = null;
        }
      },

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
