<template>
  <div>
    <Menu class="menu">
      <div class="camera-control">
        <div>
          <el-button round style="width: 300px; height: 70px; font-size: 19px; font-family: PingFang SC"
                     @click="toggleCamera">打开 / 关闭摄像头</el-button>
          <el-button round style="width: 300px; height: 70px; font-size: 19px; font-family: PingFang SC"
                     @click="clearSigCanvas">清除手指轨迹</el-button>
        </div>
        <div class="container" v-show="cameraVisible">
          <video class="input_video"></video>
          <canvas class="output_canvas" width="440px" height="310px"
                  style="transform: scaleX(-1) !important;"></canvas>
        </div>
      </div>
    </Menu>
    <div class="canvas-container">
      <canvas class="signature_canvas" style="transform: scaleX(-1) !important;"></canvas>
    </div>
  </div>
</template>

<script>
  import Menu from "@/components/menu.vue";
  import { Camera } from '@mediapipe/camera_utils/camera_utils.js';
  import { drawConnectors, drawLandmarks } from '@mediapipe/drawing_utils/drawing_utils.js';
  import { Hands, HAND_CONNECTIONS } from '@mediapipe/hands/hands.js';

  export default {
    name: "addsig",
    components: {
      Menu,
    },
    data() {
      return {
        videoElement: null,
        canvasElement: null,
        canvasCtx: null,
        hands: null,
        camera: null,
        cameraVisible: false,
        sigCanvas: null,
        sigCanvasCtx: null,
      };
    },
    methods: {
      initHands() {
        this.cameraVisible = false;
        this.videoElement = document.getElementsByClassName('input_video')[0];
        this.canvasElement = document.getElementsByClassName('output_canvas')[0];
        this.canvasCtx = this.canvasElement.getContext('2d');
        this.sigCanvas = document.getElementsByClassName('signature_canvas')[0];
        this.sigCanvasCtx = this.sigCanvas.getContext('2d');
        this.hands = new Hands({
          locateFile: (file) => {
            return `https://cdn.jsdelivr.net/npm/@mediapipe/hands/${file}`;
          }
        });
        this.hands.setOptions({
          maxNumHands: 2,
          modelComplexity: 1,
          minDetectionConfidence: 0.5,
          minTrackingConfidence: 0.5
        });
        this.hands.onResults(this.onResults);
      },
      startHandDetection() {
        this.camera = new Camera(this.videoElement, {
          onFrame: async () => {
            await this.hands.send({ image: this.videoElement });
          },
          width: 440,
          height: 310
        });
        this.camera.start();
      },
      onResults(results) {
        this.canvasCtx.save();
        this.canvasCtx.clearRect(0, 0, this.canvasElement.width, this.canvasElement.height);
        this.canvasCtx.drawImage(
          results.image, 0, 0, this.canvasElement.width, this.canvasElement.height);

        if (results.multiHandLandmarks) {
          for (const landmarks of results.multiHandLandmarks) {
            drawConnectors(this.canvasCtx, landmarks, HAND_CONNECTIONS, { color: '#00FF00', lineWidth: 5 });
            drawLandmarks(this.canvasCtx, landmarks, { color: '#FF0000', lineWidth: 2 });

            if (landmarks[8] && landmarks[12]) {
              const distance = Math.sqrt(Math.pow(landmarks[8].x - landmarks[12].x, 2)
                + Math.pow(landmarks[8].y - landmarks[12].y, 2));
              const threshold = 0.1;

              if (distance > threshold) {
                const xC = landmarks[8].x;
                const yC = landmarks[8].y;
                const x = xC * this.sigCanvas.width;
                const y = yC * this.sigCanvas.height;
                this.drawFingerPoint(x, y); // 直接绘制手指位置
              }
            }
          }
        }
        this.canvasCtx.restore();
      },
      drawFingerPoint(x, y) {
        this.sigCanvasCtx.fillStyle = '#00000c';
        this.sigCanvasCtx.beginPath();
        this.sigCanvasCtx.arc(x, y, 4, 0, Math.PI * 2);
        this.sigCanvasCtx.fill();
      },
      toggleCamera() {
        this.cameraVisible = !this.cameraVisible;
        if (this.cameraVisible) {
          this.startHandDetection();
        } else {
          this.stopHandDetection();
        }
      },
      stopHandDetection() {
        if (this.camera) {
          this.camera.stop();
          this.camera = null;
        }
      },
      clearSigCanvas() {
        this.sigCanvasCtx.clearRect(0, 0, this.sigCanvas.width, this.sigCanvas.height);
      }
    },
    mounted() {
      this.initHands();
    }
  }
</script>


<style scoped>
  .input_video {
    display: none;
  }
  .menu {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }
  .camera-control {
    margin-left: 60px; /* 与菜单栏的间距 */
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    padding: 0 10px; /* 按钮和摄像头之间的内边距 */
    height: 320px;
  }
  .container {
    width: calc(100% - 400px); /* 摄像头下方画布宽度为除菜单外的屏幕宽度 */
    margin-bottom: 5px; /* 摄像头与下方画布之间的垂直间距 */
  }
  .canvas-container {
    position: static;
    margin-left: 280px; /* 与菜单栏的间距 */
  }
  .signature_canvas {
    width: calc(100% - 400px); /* 画布宽度为除菜单外的屏幕宽度减去按钮宽度 */
    height: 370px; /* 设置画布高度 */
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    box-sizing: border-box;
    border-radius: 4px;
    border: 2px dashed #cccccc;
    position: absolute;
    bottom: 0;
    left: 280px; /* 左边与菜单栏对齐 */
  }
</style>
