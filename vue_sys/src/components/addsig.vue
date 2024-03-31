<template>
  <div>
    <Menu class="menu">
      <div class="camera-control">
        <div>
          <el-button round style="width: 300px; height: 70px; font-size: 19px; font-family: PingFang SC"
                     @click="toggleCamera">打开 / 关闭摄像头</el-button>
          <!--<el-button round style="width: 300px; height: 70px; font-size: 19px; font-family: PingFang SC"-->
                     <!--@click="clearSigCanvas">清空画布</el-button>-->
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

        // 用于绘制手指关键点的画布
        canvasElement: null,
        canvasCtx: null,

        hands: null,
        camera: null,
        cameraVisible: false,

        // 用于绘制手指轨迹的画布
        sigCanvas: null,
        sigCanvasCtx: null,
        fingerPath: [], // 用于存储手指轨迹的数组
        fingerPoint: [], // 用于存储食指点数组

        sampleInterval: 0.5, // 修改采样间隔，表示每隔多少帧记录一次手指位置
        frameCounter: 0, // 用于计数当前帧数

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
          }

          // 检查是否需要记录手指位置
          if (this.frameCounter === 0) {
            if (results.multiHandLandmarks[0] && results.multiHandLandmarks[0][8]
             && results.multiHandLandmarks[0][12]) {

              // 计算食指和中指之间的距离
              const distance = Math.sqrt(Math.pow(results.multiHandLandmarks[0][8].x - results.multiHandLandmarks[0][12].x, 2)
                + Math.pow(results.multiHandLandmarks[0][8].y - results.multiHandLandmarks[0][12].y, 2));
              // 设置距离阈值
              const threshold = 0.1; // 根据实际情况调整阈值

              if (distance > threshold) { // 两手指距离分开 也就是可以留下轨迹的时候
                const xC = results.multiHandLandmarks[0][8].x;
                const yC = results.multiHandLandmarks[0][8].y;
                this.fingerPath.push({ x: xC * this.sigCanvas.width, y: yC * this.sigCanvas.height });
                this.drawFingerPath(); // 在新的画布上绘制手指轨迹
              }

              this.fingerPoint.push({ x: results.multiHandLandmarks[0][8].x * this.sigCanvas.width, y: results.multiHandLandmarks[0][8].y * this.sigCanvas.height });
              this.drawFingerPoint();
            }
          }

          // 更新帧数计数器
          this.frameCounter = (this.frameCounter + 1) % this.sampleInterval;
        }
        this.canvasCtx.restore();
      },

      // 直线
      // drawFingerPath() {
      //   this.sigCanvasCtx.clearRect(0, 0, this.sigCanvas.width, this.sigCanvas.height);
      //   this.sigCanvasCtx.strokeStyle = '#00000c';
      //   this.sigCanvasCtx.lineWidth = 4;
      //   this.sigCanvasCtx.beginPath();
      //   const firstPoint = this.fingerPath[0];
      //   this.sigCanvasCtx.moveTo(firstPoint.x, firstPoint.y);
      //   for (let i = 1; i < this.fingerPath.length; i++) {
      //     const currentPoint = this.fingerPath[i];
      //     const previousPoint = this.fingerPath[i - 1];
      //     const controlPointX = (currentPoint.x + previousPoint.x) / 2;
      //     const controlPointY = (currentPoint.y + previousPoint.y) / 2;
      //     this.sigCanvasCtx.quadraticCurveTo(previousPoint.x, previousPoint.y, controlPointX, controlPointY);
      //   }
      //   this.sigCanvasCtx.stroke();
      // },

      // 点
      // drawFingerPath() {
      //   this.sigCanvasCtx.clearRect(0, 0, this.sigCanvas.width, this.sigCanvas.height);
      //   this.sigCanvasCtx.fillStyle = '#00000c';
      //   this.sigCanvasCtx.lineWidth = 4;
      //
      //   for (let i = 0; i < this.fingerPath.length; i++) {
      //     const { x, y } = this.fingerPath[i];
      //     this.sigCanvasCtx.beginPath();
      //     this.sigCanvasCtx.arc(x, y, 3, 0, Math.PI * 2); // 使用 arc 方法绘制圆形
      //     this.sigCanvasCtx.fill(); // 填充圆形
      //   }
      // },

      drawFingerPath() {
        // 清除画布
        this.sigCanvasCtx.clearRect(0, 0, this.sigCanvas.width, this.sigCanvas.height);

        // 绘制手指轨迹
        this.sigCanvasCtx.fillStyle = '#00000c'; // 设置轨迹点的颜色
        for (let i = 0; i < this.fingerPath.length; i++) {
          const { x, y } = this.fingerPath[i];
          this.sigCanvasCtx.beginPath();
          this.sigCanvasCtx.arc(x, y, 4, 0, Math.PI * 2); // 绘制一个小点
          this.sigCanvasCtx.fill();
        }
      },

      drawFingerPoint() {
        // 绘制手指的投影位置
        if (this.fingerPoint.length > 0) {
          const { x, y } = this.fingerPoint[this.fingerPoint.length - 1]; // 获取最后一个手指位置
          this.sigCanvasCtx.beginPath();
          this.sigCanvasCtx.arc(x, y, 5, 0, Math.PI * 2); // 绘制一个大点来表示手指的当前位置
          this.sigCanvasCtx.fillStyle = 'red'; // 设置手指投影位置的颜色
          this.sigCanvasCtx.globalAlpha = Math.abs(Math.sin(Date.now() / 500)); // 根据时间变换透明度
          this.sigCanvasCtx.fill();
          this.sigCanvasCtx.globalAlpha = 1; // 恢复透明度

          // 每隔一段时间重绘一次
          requestAnimationFrame(this.drawFingerPoint.bind(this));
        }
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
