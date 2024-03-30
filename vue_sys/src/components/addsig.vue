<template>
  <div>
    <Menu class="menu">
      <div class="camera-control">
        <el-button style="width: 400px; height: 100px; font-size: 19px; font-family: PingFang SC"
                   @click="toggleCamera">打开 / 关闭摄像头</el-button>
        <div class="container" v-show="cameraVisible">
          <video class="input_video"></video>
          <canvas class="output_canvas" width="440px" height="310px"></canvas>
        </div>
      </div>
    </Menu>
    <div class="canvas-container">
      <canvas class="signature_canvas"></canvas>
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
      };
    },
    methods: {
      initHands() {
        this.videoElement = document.getElementsByClassName('input_video')[0];
        this.canvasElement = document.getElementsByClassName('output_canvas')[0];
        this.canvasCtx = this.canvasElement.getContext('2d');

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
          }
        }
        this.canvasCtx.restore();
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
    width: calc(100% - 350px); /* 画布宽度为除菜单外的屏幕宽度减去按钮宽度 */
    height: 350px; /* 设置画布高度 */
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    box-sizing: border-box;
    border-radius: 4px;
    border: 2px dashed #cccccc;
    position: absolute;
    bottom: 0;
    left: 280px; /* 左边与菜单栏对齐 */
  }
</style>
