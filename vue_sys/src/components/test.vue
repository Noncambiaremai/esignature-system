<template>
  <div class="container">
    <video ref="videoElement" class="input_video"></video>
    <canvas class="output_canvas" width="660px" height="480px"></canvas>
  </div>
</template>

<script>
  // 在这里导入 Mediapipe 相关的库
  // import * as mpCameraUtils from '@mediapipe/camera_utils/camera_utils.js';
  // import * as mpControlUtils from '@mediapipe/control_utils/control_utils.js';
  // import * as mpDrawingUtils from '@mediapipe/drawing_utils/drawing_utils.js';
  // import * as mpFaceMesh from '@mediapipe/face_mesh/face_mesh.js';
  import { FaceMesh, FACEMESH_TESSELATION, FACEMESH_RIGHT_EYE,
    FACEMESH_RIGHT_EYEBROW, FACEMESH_RIGHT_IRIS, FACEMESH_LEFT_EYE,
    FACEMESH_LEFT_EYEBROW, FACEMESH_LEFT_IRIS, FACEMESH_FACE_OVAL,
    FACEMESH_LIPS } from '@mediapipe/face_mesh/face_mesh.js';
  import { Camera } from '@mediapipe/camera_utils/camera_utils.js';
  import { drawConnectors } from '@mediapipe/drawing_utils/drawing_utils.js';

    export default {
        name: "test",
        methods: {
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

          if (results.multiFaceLandmarks) {
            for (const landmarks of results.multiFaceLandmarks) {
              drawConnectors(canvasCtx, landmarks, FACEMESH_TESSELATION,
                {color: '#C0C0C070', lineWidth: 1});
              drawConnectors(canvasCtx, landmarks, FACEMESH_RIGHT_EYE, {color: '#FF3030'});
              drawConnectors(canvasCtx, landmarks, FACEMESH_RIGHT_EYEBROW, {color: '#FF3030'});
              drawConnectors(canvasCtx, landmarks, FACEMESH_RIGHT_IRIS, {color: '#FF3030'});
              drawConnectors(canvasCtx, landmarks, FACEMESH_LEFT_EYE, {color: '#30FF30'});
              drawConnectors(canvasCtx, landmarks, FACEMESH_LEFT_EYEBROW, {color: '#30FF30'});
              drawConnectors(canvasCtx, landmarks, FACEMESH_LEFT_IRIS, {color: '#30FF30'});
              drawConnectors(canvasCtx, landmarks, FACEMESH_FACE_OVAL, {color: '#E0E0E0'});
              drawConnectors(canvasCtx, landmarks, FACEMESH_LIPS, {color: '#E0E0E0'});
            }
          }
          canvasCtx.restore();
        }

        const faceMesh = new FaceMesh({locateFile: (file) => {
            return `https://cdn.jsdelivr.net/npm/@mediapipe/face_mesh/${file}`;
          }});
        faceMesh.setOptions({
          maxNumFaces: 1,
          refineLandmarks: true,
          minDetectionConfidence: 0.5,
          minTrackingConfidence: 0.5
        });

        console.log("huhuhu");

        faceMesh.onResults(onResults);

        const camera = new Camera(videoElement, {
          onFrame: async () => {
            await faceMesh.send({image: videoElement});
          },
          width: 1280,
          height: 720
        });
        camera.start();

      }
    }
</script>

<style scoped>
  .input_video, .output_canvas {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
</style>
