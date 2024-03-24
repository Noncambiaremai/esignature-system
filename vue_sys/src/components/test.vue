<template>
  <div class="container">
    <video ref="videoElement" class="input_video"></video>
    <canvas class="output_canvas" width="850px" height="480px"></canvas>
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
      }
    },
    mounted() {
      this.initFaceMesh();
      this.startFaceDetection();
    },
  }
</script>



<style scoped>
  .input_video {
    display: none;
  }
</style>
