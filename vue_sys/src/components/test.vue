<template>
  <div class="main-container">
    <el-button @click="uploadFile">哈哈哈</el-button>

    <div ref="canvasCont" class="canvas-container">
      <canvas ref="myCanvas" class="pdf-container"></canvas>
    </div>

    <div class="pagination-wrapper">
      <button @click="clickPre">上一页</button>
      <span>第{{ pageNo }} / {{ pdfPageNumber }}页</span>
      <button @click="clickNext">下一页</button>
    </div>
  </div>
</template>

<script>
  const pdfJS = require("pdfjs-dist");

  pdfJS.GlobalWorkerOptions.workerSrc = require("pdfjs-dist/build/pdf.worker.entry");

  export default {
    name: "test",
    data() {
      return {
        pageNo: null, // 当前页
        pdfPageNumber: null,  // 总页数
        renderingPage: false,
        pdfData: null, // PDF的base64
        scale: 2, // 缩放值
        width: "",
        height: "",
        base64String: "",
      };
    },
    methods: {
      uploadFile() {
        console.log(this.base64String);
        // 引入pdf.js的字体
        let CMAP_URL = "https://unpkg.com/pdfjs-dist@2.0.943/cmaps/";
        //读取base64的pdf流文件
        this.pdfData = pdfJS.getDocument({
          data: atob(this.base64String), // PDF base64编码
          cMapUrl: CMAP_URL,
          cMapPacked: true,
        });
        // 渲染
        this.renderPage(1);
      },

      // 根据页码渲染相应的PDF
      renderPage(num) {
        this.renderingPage = true;
        this.pdfData.promise.then((pdf) => {
          // pdf的总页数
          this.pdfPageNumber = pdf.numPages;

          // 得到pdf具体的页
          pdf.getPage(num).then((page) => {
            // 获取DOM中为预览PDF准备好的canvasDOM对象
            let canvas = this.$refs.myCanvas;
            let ctx = canvas.getContext("2d");

            // 获取页面比率
            // let ratio = this._getRatio(ctx);
            let ratio = 1;

            // 根据页面宽度和视口宽度的比率就是内容区的放大比率
            // canvasCont是那个外框的ref
            // dialogWidth表示装PDF的容器的宽度
            let dialogWidth = this.$refs["canvasCont"].offsetWidth;

            // page.view[2]表示pdf页面原本的宽度
            let pageWidth = page.view[2] * ratio;
            let scale = dialogWidth / pageWidth;
            let viewport = page.getViewport({ scale });

            // 记录内容区宽高，后期添加水印时需要
            this.width = viewport.width * ratio;
            this.height = viewport.height * ratio;

            canvas.width = this.width;
            canvas.height = this.height;

            // 缩放比率
            ctx.setTransform(ratio, 0, 0, ratio, 0, 0);

            let renderContext = {
              canvasContext: ctx,
              viewport: viewport,
            };
            page.render(renderContext).promise.then(() => {
              this.renderingPage = false;
              this.pageNo = num;

              // 添加水印
              this._renderImage();
            });
          });
        });
      },
      // 计算角度
      _getRatio(ctx) {
        let dpr = window.devicePixelRatio || 1;
        let bsr =
          ctx.webkitBackingStorePixelRatio ||
          ctx.mozBackingStorePixelRatio ||
          ctx.msBackingStorePixelRatio ||
          ctx.oBackingStorePixelRatio ||
          ctx.backingStorePixelRatio ||
          1;
        return dpr / bsr;
      },

      // 在画布上渲染图片
      _renderImage() {
        let canvas = this.$refs.myCanvas;
        let ctx = canvas.getContext("2d");

        // 创建一个新的Image对象
        let image = new Image();
        // 设置图片的src为你想要添加的图片的 base64 编码字符串
        image.src = "";
        // 等待图片加载完成
        image.onload = () => {
          // 在画布上绘制图片
          // ctx.drawImage(image, 0, 0, canvas.width, canvas.height);
          ctx.drawImage(image, canvas.width - image.width / 3, canvas.height - image.height / 3, image.width / 3, image.height / 3);
        };
      },

      clickPre() {
        // 当前页面未被渲染 && 还有上一页
        if (!this.renderingPage && this.pageNo && this.pageNo > 1) {
          this.renderPage(this.pageNo - 1); // 渲染上一页
        }
      },
      clickNext() {
        // 当前页面未被渲染 && 还有下一页
        if (
          !this.renderingPage && this.pdfPageNumber &&
          this.pageNo && this.pageNo < this.pdfPageNumber
        ) {
          this.renderPage(this.pageNo + 1); // 渲染下一页
        }
      },
    },
  };
</script>

<style scoped>
  .main-container {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .canvas-container {
    width: 600px;
    height: 776px;
    border: 1px dashed black;
    position: relative;
    display: flex;
    justify-content: center;
  }
  .pdf-container {
    width: 100%;
    height: 100%;
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
