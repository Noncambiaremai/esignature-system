<template>
  <div>
    <Menu>
      <div class="container">
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
        mounted() {
          const videoElement = document.getElementsByClassName('input_video')[0];
          const canvasElement = document.getElementsByClassName('output_canvas')[0];
          const canvasCtx = canvasElement.getContext('2d');

          function onResults(results) {
            canvasCtx.save();
            canvasCtx.clearRect(0, 0, canvasElement.width, canvasElement.height);
            canvasCtx.drawImage(
              results.image, 0, 0, canvasElement.width, canvasElement.height);
            if (results.multiHandLandmarks) {
              for (const landmarks of results.multiHandLandmarks) {
                drawConnectors(canvasCtx, landmarks, HAND_CONNECTIONS,
                  {color: '#00FF00', lineWidth: 5});
                drawLandmarks(canvasCtx, landmarks, {color: '#FF0000', lineWidth: 2});
              }
            }
            canvasCtx.restore();
          }

          const hands = new Hands({locateFile: (file) => {
              return `https://cdn.jsdelivr.net/npm/@mediapipe/hands/${file}`;
            }});
          hands.setOptions({
            maxNumHands: 2,
            modelComplexity: 1,
            minDetectionConfidence: 0.5,
            minTrackingConfidence: 0.5
          });
          hands.onResults(onResults);

          const camera = new Camera(videoElement, {
            onFrame: async () => {
              await hands.send({image: videoElement});
            },
            width: 1280,
            height: 720
          });
          camera.start();

        }
    }
</script>

<style scoped>
  .input_video {
    display: none;
  }
</style>
