<template>
  <div class="login">
    <el-form ref="enrollForm" class="enroll-form">
      <h3 class="title">新用户注册</h3>
      <el-form-item prop="userId">
        <el-input v-model="userId"  placeholder="手机号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>

      </el-form-item>
      <el-form-item prop="userPassword">
        <el-input type="password" v-model="userPassword"  placeholder="密码">
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button style="border: 1px solid grey;" @click="toggleCamera">打开 / 关闭摄像头</el-button>
        <el-button style="border: 1px solid grey;" @click="takeImage">拍照录入人脸</el-button>
      </el-form-item>


      <el-form-item v-show="cameraVisible" label="摄像头">
        <div class="container" >
          <video ref="videoElement" class="input_video"></video>
          <canvas ref="canvasElement" class="output_canvas" width="400px" height="300px"></canvas>
        </div>
      </el-form-item>

      <el-form-item style="width:100%;">
        <el-button size="medium" type="primary" style="width: 150px" @click="toEnroll">注 册</el-button>
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
  import axios from 'axios';

  export default {
    name: "enroll",
    data() {
      return {
        cameraVisible: false,
        videoElement: null,
        canvasElement: null,
        canvasCtx: null,
        faceMesh: null,
        camera: null,

        userId: "",
        userPassword: "",
        faceImage: null, // 保存人脸图片的变量
        faceFeature: null, // 保存人脸特征的变量
        tempFaceFeature: null // 保存人脸特征的变量
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
          // width: 850,
          // height: 480
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
            // drawConnectors(this.canvasCtx, landmarks, FACEMESH_TESSELATION,
            //   {color: '#C0C0C070', lineWidth: 1});
            // console.log(FACEMESH_FACE_OVAL);
            // drawConnectors(this.canvasCtx, landmarks, FACEMESH_RIGHT_EYE, {color: '#FF3030'});
            // drawConnectors(this.canvasCtx, landmarks, FACEMESH_RIGHT_EYEBROW, {color: '#FF3030'});
            // // drawConnectors(this.canvasCtx, landmarks, FACEMESH_RIGHT_IRIS, {color: '#FF3030'});
            // drawConnectors(this.canvasCtx, landmarks, FACEMESH_LEFT_EYE, {color: '#30FF30'});
            // drawConnectors(this.canvasCtx, landmarks, FACEMESH_LEFT_EYEBROW, {color: '#30FF30'});
            // // drawConnectors(this.canvasCtx, landmarks, FACEMESH_LEFT_IRIS, {color: '#30FF30'});
            // drawConnectors(this.canvasCtx, landmarks, FACEMESH_FACE_OVAL, {color: '#E0E0E0'});
            // drawConnectors(this.canvasCtx, landmarks, FACEMESH_LIPS, {color: '#E0E0E0'});

            // 要绘制的关键点列表
            const landmarksList = [61, 146, 91, 181, 84, 17, 314, 405, 321, 375, 291,
              185, 40, 39, 37, 0, 267, 269, 270, 409, 78, 95, 88, 178, 87, 14, 317,
              402, 318, 324, 308, 191, 80, 81, 82, 13, 312, 311, 310, 415, 263, 249,
              390, 373, 374, 380, 381, 382, 362, 466, 388, 387, 386, 385, 384, 398,
              276, 283, 282, 295, 285, 300, 293, 334, 296, 336, 33, 7, 163, 144, 145,
              153, 154, 155, 133, 246, 161, 160, 159, 158, 157, 173, 46, 53, 52, 65,
              55, 70, 63, 105, 66, 107, 10, 338, 297, 332, 284, 251, 389, 356, 454,
              323, 361, 288, 397, 365, 379, 378, 400, 377, 152, 148, 176, 149, 150,
              136, 172, 58, 132, 93, 234, 127, 162, 21, 54, 103, 67, 109, 168, 6, 197,
              195, 5, 4, 1, 19, 94, 2, 98, 97, 326, 327, 294, 278, 344, 440, 275, 45,
              220, 115, 48, 64];

              // 绘制关键点
            for (let i = 0; i < landmarksList.length; i++) {
              const index = landmarksList[i];
              const x = landmarks[index].x * this.canvasElement.width; // 将归一化的水平坐标转换为画布坐标
              const y = landmarks[index].y * this.canvasElement.height; // 将归一化的垂直坐标转换为画布坐标
              this.canvasCtx.beginPath();
              this.canvasCtx.arc(x, y, 2, 0, 2 * Math.PI); // 画一个圆
              this.canvasCtx.fillStyle = '#6bff25';
              this.canvasCtx.fill();
              this.canvasCtx.closePath();
            }
          }
          this.tempFaceFeature = results.multiFaceLandmarks[0];
          // console.log(this.faceFeature);
        }
        this.canvasCtx.restore();
      },
      goback() {
        this.$router.push({name: 'login'});
      },
      toggleCamera() {
        this.cameraVisible = !this.cameraVisible;
        if (this.cameraVisible) {
          this.startFaceDetection();
        } else {
          this.stopFaceDetection();
        }
      },
      stopFaceDetection() {
        this.cameraVisible = false;
        if (this.camera) {
          this.camera.stop();
          this.camera = null;
        }
      },

      // 拍照录入人脸
      takeImage() {

        this.faceFeature = this.tempFaceFeature;
        console.log(this.faceFeature);
        // 把当前画布上的人脸图片截下来保存到一个变量中 this.faceImage
        // this.faceImage和this.faceFeature里面都有数据 关闭摄像头
        if (!this.canvasElement) {
          console.error('Canvas element is not available');
          return;
        }
        const canvas = this.canvasElement;
        const imgData = canvas.toDataURL('image/png');
        this.faceImage = imgData;
        this.$message({ type: 'success', message: '录入成功!' });
        this.stopFaceDetection();
      },

      // 点击注册按钮
      toEnroll() {
        // 首先判断用户输入的账号是不是手机号
        const regex = /^1[3456789]\d{9}$/;
        if (this.userId.length !== 11 || !regex.test(this.userId)) {
          this.$message.error('请输入11位正确格式手机号');
          return;
        }

        axios.get('/api/user/isAccountExists', { params: { user_id: this.userId } })
          .then(response => {
            // 已经有人注册过了
            if (response.data) {
              this.$message.error('手机号已被注册，请更换');
              this.userId = "";
            }
          });

        if (this.userPassword.length < 6) {
          this.$message.error('请输入至少6位数密码');
          return;
        }

        // 能执行到这里的表示账号和密码都符合要求
        if (this.faceImage && this.faceFeature) {
          const formData = new FormData();
          formData.append('userId', this.userId);
          formData.append('userPassword', this.userPassword);
          formData.append('faceImage', this.faceImage);
          formData.append('faceFeature', this.faceFeature);
          axios.post('/api/user/register', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          }).then(response => {
            // 处理注册成功的逻辑
            this.$message({ type: 'success', message: '注册成功!' });
          }).catch(error => {
            // 处理注册失败的逻辑
            this.$message.error('注册失败，请稍后重试');
          });
        }
        else this.$message.error('未录入人脸，不可注册');

      }

    },
    mounted() {
      this.initFaceMesh();
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
