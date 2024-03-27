<template>
  <div>
    <Menu>
      <el-button style="width: 400px" @click="toggleCamera">打开 / 关闭摄像头</el-button>
      <div class="container" v-show="cameraVisible">
        <video class="input_video"></video>
        <canvas class="output_canvas" width="400px" height="300px"></canvas>
      </div>
    </Menu>
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
          // width: 1280,
          // height: 720
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
</style>
