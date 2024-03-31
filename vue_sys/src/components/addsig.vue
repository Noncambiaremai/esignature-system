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
        lastFingerDistance: 0, // 上一次两个手指的距离
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
              const threshold = 0.15;

              if (distance > threshold) {
                const xC = landmarks[8].x;
                const yC = landmarks[8].y;
                const x = xC * this.sigCanvas.width;
                const y = yC * this.sigCanvas.height;
                this.drawFingerPoint(x, y);
              }
              else {
                // 不画线 重新定位点
                this.lastFingerDistance = 0; // 重置上一次的距离
              }

              // 更新上一次两个手指的距离
              this.lastFingerDistance = distance;
            }
          }
        }
        this.canvasCtx.restore();
      },
      drawFingerPoint(x, y) {
        // 如果上一次的两个手指的距离大于阈值，则从上一个点绘制直线
        if (this.lastFingerDistance > 0.15) {
          this.sigCanvasCtx.strokeStyle = '#00000c';
          this.sigCanvasCtx.lineWidth = 4;

          this.sigCanvasCtx.beginPath();
          this.sigCanvasCtx.moveTo(this.lastX, this.lastY); // 从上一个点开始
          this.sigCanvasCtx.lineTo(x, y); // 到当前点
          this.sigCanvasCtx.stroke();
        }

        // 保存当前点的坐标作为下一次的起点
        this.lastX = x;
        this.lastY = y;
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
