<template>
  <div class="login">
    <el-form ref="enrollForm" class="enroll-form">
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

      <el-form-item label="摄像头">
        <video ref="videoElement" class="input_video"></video>
        <canvas ref="canvasElement"  class="output_canvas" width="850px" height="480px"></canvas>
      </el-form-item>


      <el-form-item style="width:100%;">
        <el-button size="medium" type="primary" style="width: 150px">注 册</el-button>
        <el-button size="medium" type="primary" style="width: 150px" @click="goback">返 回</el-button>
      </el-form-item>
    </el-form>


  </div>
</template>

<script>
  import { FaceMesh, FACEMESH_TESSELATION, FACEMESH_RIGHT_EYE,
    FACEMESH_RIGHT_EYEBROW, FACEMESH_RIGHT_IRIS, FACEMESH_LEFT_EYE,
    FACEMESH_LEFT_EYEBROW, FACEMESH_LEFT_IRIS, FACEMESH_FACE_OVAL,
    FACEMESH_LIPS } from '@mediapipe/face_mesh/face_mesh.js';
  import { Camera } from '@mediapipe/camera_utils/camera_utils.js';
  import { drawConnectors } from '@mediapipe/drawing_utils/drawing_utils.js';

  export default {
    name: "test",
    data() {
      return {
        cameraVisible: false,
        videoElement: null,
        canvasElement: null,
        canvasCtx: null,
        faceMesh: null,
        camera: null
      };
    },
    methods: {
      initFaceMesh() {
        this.videoElement = document.getElementsByClassName('input_video')[0];
        this.canvasElement = document.getElementsByClassName('output_canvas')[0];
        this.canvasCtx = this.canvasElement.getContext('2d');

        this.faceMesh = new FaceMesh({locateFile: (file) => {
            return `https://cdn.jsdelivr.net/npm/@mediapipe/face_mesh/${file}`;
          }});
        this.faceMesh.setOptions({
          maxNumFaces: 1,
          refineLandmarks: true,
          minDetectionConfidence: 0.5,
          minTrackingConfidence: 0.5
        });

        this.faceMesh.onResults(this.onResults);
      },
      startFaceDetection() {
        this.camera = new Camera(this.videoElement, {
          onFrame: async () => {
            await this.faceMesh.send({image: this.videoElement});
          },
          width: 850,
          height: 480
        });
        this.camera.start();
      },
      onResults(results) {
        this.canvasCtx.save();
        this.canvasCtx.clearRect(0, 0, this.canvasElement.width, this.canvasElement.height);
        this.canvasCtx.drawImage(
          results.image, 0, 0, this.canvasElement.width, this.canvasElement.height);

        if (results.multiFaceLandmarks) {
          for (const landmarks of results.multiFaceLandmarks) {
            drawConnectors(this.canvasCtx, landmarks, FACEMESH_TESSELATION,
              {color: '#C0C0C070', lineWidth: 1});
            drawConnectors(this.canvasCtx, landmarks, FACEMESH_RIGHT_EYE, {color: '#FF3030'});
            drawConnectors(this.canvasCtx, landmarks, FACEMESH_RIGHT_EYEBROW, {color: '#FF3030'});
            drawConnectors(this.canvasCtx, landmarks, FACEMESH_RIGHT_IRIS, {color: '#FF3030'});
            drawConnectors(this.canvasCtx, landmarks, FACEMESH_LEFT_EYE, {color: '#30FF30'});
            drawConnectors(this.canvasCtx, landmarks, FACEMESH_LEFT_EYEBROW, {color: '#30FF30'});
            drawConnectors(this.canvasCtx, landmarks, FACEMESH_LEFT_IRIS, {color: '#30FF30'});
            drawConnectors(this.canvasCtx, landmarks, FACEMESH_FACE_OVAL, {color: '#E0E0E0'});
            drawConnectors(this.canvasCtx, landmarks, FACEMESH_LIPS, {color: '#E0E0E0'});
          }
        }
        this.canvasCtx.restore();
      },
      goback() {
        this.$router.push({name: 'login'});
      }
    },
    mounted() {
      this.initFaceMesh();
      this.cameraVisible = false;

      this.startFaceDetection();
    },
  }
</script>

<style>
  .input_video {
    display: none;
  }
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
