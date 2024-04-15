<template>
    <div>
      <Menu>
        <div style="display: flex;">
          <!-- 左侧容器 -->
          <div style="width: 45%; margin-right: 15px;">
            <div style="display: flex;  flex-direction: column;">
              <div style="display: flex;">
                <el-button round style="width: 200px; height: 70px; font-size: 19px; font-family: PingFang SC; margin-bottom: 10px;"
                                            @click="startChooseSig">选择签名</el-button>
                <el-button round style="width: 200px; height: 70px; font-size: 19px; font-family: PingFang SC; margin-bottom: 10px;"
                           @click="startChooseFile">选择文件</el-button>
              </div>
              <div style="display: flex;">
                <el-button  round style="width: 200px; height: 70px; font-size: 19px; font-family: PingFang SC; margin-bottom: 10px;"
                  :title="'上一页'" @click="clickPre"> 上一页</el-button>
                <el-button  round style="width: 200px; height: 70px; font-size: 19px; font-family: PingFang SC; margin-bottom: 10px;"
                  :title="'下一页'" @click="clickNext"> 下一页</el-button>
              </div>
              <div style="display: flex;">
                <el-button  round style="width: 200px; height: 70px; font-size: 19px; font-family: PingFang SC; margin-bottom: 10px;"
                            @click="clickMerge">添加签名</el-button>
                <el-button  round style="width: 200px; height: 70px; font-size: 19px; font-family: PingFang SC; margin-bottom: 10px;"
                            @click="">保存</el-button>
              </div>

              <div class="block">
                <span class="demonstration">调整签名图片大小</span>
                <el-slider v-model="imageScale" :min="0.3" :max="1.4" :step="0.01"></el-slider>
              </div>

              <div class="image-container">
                <img v-show="imageData!=''" :src="imageData" :style="{ transform: 'scale(' + imageScale + ')' }"  alt="图片">
              </div>
            </div>
          </div>

          <!-- 右侧容器 -->
          <!--<div style="width: 66.666%; overflow: auto;  border: 2px solid #ccc; height: 730px">-->
          <div ref="canvasCont" class="canvas-container">
          <div class="page">{{pageNo}}/{{pdfPageNumber}} </div>
            <!--<PDF :src="pdfData" :page="pageNum" @progress="loadedRatio = $event" @page-loaded="pageLoaded($event)"-->
                 <!--@num-pages="pageTotalNum=$event" @error="pdfError($event)"  @link-clicked="page = $event">-->
            <!--</PDF>-->
              <canvas ref="myCanvas" class="pdf-container" @click="handleCanvasClick"></canvas>
          </div>
        </div>

        <!--点击选择签名之后的dialog-->
        <el-dialog :visible.sync="sigdialogTableVisible">
          <el-table :data="sigTableData">
            <el-table-column label="签名图片" style="width: 20%">
              <template slot-scope="scope">
                <img :src="scope.row.image" style="width: 50%; height: 25%;" alt="图片">
              </template>
            </el-table-column>
            <el-table-column
              label="签名创建时间"
              style="width: 20%">
              <template slot-scope="scope">
                <i class="el-icon-time"></i>
                <span style="margin-left: 10px">{{ new Date(scope.row.create_time).toLocaleString()}}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="签名名称"
              style="width: 20%">
              <template slot-scope="scope">
                <div slot="reference" class="name-wrapper">
                  <el-tag size="medium">{{ scope.row.sig_name }}</el-tag>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" style="width: 20%">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="success"
                  @click="chooseSig(scope.$index, scope.row)">选择</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-dialog>

        <!--点击选择文件之后的dialog-->
        <el-dialog :visible.sync="filedialogTableVisible">
          <el-table :data="tableData">
            <el-table-column label="文件名称" style="width: 35%">
              <template slot-scope="scope">
                <el-popover trigger="hover" placement="top" class="custom-popover">
                  <p>文件上传时间: {{ new Date(scope.row.update_time).toLocaleString()
                    }}</p>
                  <div slot="reference" class="name-wrapper">
                    <el-tag size="medium">{{ scope.row.doc_name }}</el-tag>
                  </div>
                </el-popover>
              </template>
            </el-table-column>
            <el-table-column label="文件类型" style="width: 13%">
              <template slot-scope="scope">
                <div slot="reference" class="name-wrapper">
                  <el-tag size="medium">{{ scope.row.doc_type }}</el-tag>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="文件状态" style="width: 13%">
              <template slot-scope="scope">
                <div slot="reference" class="name-wrapper">
                  <el-tag size="medium">{{ scope.row.doc_status === false ? '未签名' : '已签名' }}</el-tag>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" style="width: 30%">
              <template slot-scope="scope">
                <div class="button-group">
                  <el-button
                    size="mini" type="success" @click="chooseFile(scope.$index, scope.row)">选择</el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </el-dialog>
      </Menu>
    </div>
</template>

<script>
  import axios from 'axios';
  // import fabric from 'fabric';
  import Menu from "@/components/menu.vue";
  // import PDF from 'vue-pdf';
  const pdfJS = require("pdfjs-dist");

  pdfJS.GlobalWorkerOptions.workerSrc = require("pdfjs-dist/build/pdf.worker.entry");
    export default {
        name: "mergePage",
        components: {
          Menu,
        },
      data() {
          return {
            tableData: [],
            sigTableData: [],
            sigdialogTableVisible: false,
            filedialogTableVisible: false,
            imageData: "",

            // 有关PDF的data
            previewPDFDialog:false,
            pageNo: null, // 当前页
            pdfPageNumber: null,  // 总页数
            renderingPage: false,
            pdfData: null, // PDF的base64
            scale: null, // 缩放值
            width: "",
            height: "",
            pdfbase64: "",
            // url:"http://storage.xuetangx.com/public_assets/xuetangx/PDF/PlayerAPI_v1.0.6.pdf",
            clickPosition: { x: 0, y: 0 },

            imageScale: 1,
          }
      },
      created() {

      },
      methods: {
        selectSigList() {
          axios.get('/api/sig/selectAllByUserId').then(response => {
            this.sigTableData = response.data;
            console.log(this.sigTableData);
          }).catch(error => {
            console.error('Failed to fetch signature list:', error);
          });
        },

        selectFileList() {
          axios.get('/api/doc/selectAllByUserId').then(response => {
            this.tableData = response.data;
          }).catch(error => {
            console.error('Failed to fetch file list:', error);
          });
        },

        startChooseSig() {
          this.selectSigList();
          this.sigdialogTableVisible = true;
        },

        startChooseFile() {
          this.selectFileList();
          this.filedialogTableVisible = true;
        },

        chooseSig(index, row) {
          this.sigdialogTableVisible = false;
          this.imageData = row.image;
        },
        chooseFile(index, row) {
          this.filedialogTableVisible = false;
          axios.get('/api/doc/selectFileByDocPath',
            { params: { filePath: row.doc_path }}).then(response => {
            console.log(response.data.file);
            this.pdfbase64 = response.data.file;

            // 引入pdf.js的字体
            let CMAP_URL = "https://unpkg.com/pdfjs-dist@2.0.943/cmaps/";
            //读取base64的pdf流文件
            this.pdfData = pdfJS.getDocument({
              data: atob(this.pdfbase64), // PDF base64编码
              cMapUrl: CMAP_URL,
              cMapPacked: true,
            });
            // 渲染
            this.renderPage(1);
          });
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

              // 根据页面宽度和视口宽度的比率就是内容区的放大比率
              // dialogWidth表示装PDF的容器的宽度 也就是div的宽度
              let dialogWidth = this.$refs["canvasCont"].offsetWidth;

              // page.view[2]表示pdf页面原本的宽度
              let pageWidth = page.view[2];
              let scale = dialogWidth / pageWidth;
              let viewport = page.getViewport({ scale });

              // 记录内容区宽高，后期添加水印时需要
              this.width = viewport.width;
              this.height = viewport.height;
              canvas.width = this.width;
              canvas.height = this.height;

              let renderContext = {
                canvasContext: ctx,
                viewport: viewport,
              };
              page.render(renderContext).promise.then(() => {
                this.renderingPage = false;
                this.pageNo = num;

                // 添加图片
                // this._renderImage();
              });
            });
          });
        },

        handleCanvasClick(event) {
          // 获取鼠标点击的坐标
          const canvas = this.$refs.myCanvas;
          const rect = canvas.getBoundingClientRect();
          this.clickPosition.x = event.clientX - rect.left;
          this.clickPosition.y = event.clientY - rect.top;
        },

        // 在画布上渲染图片
        _renderImage(x, y) {
          let canvas = this.$refs.myCanvas;
          let ctx = canvas.getContext("2d");

          // 创建一个新的Image对象
          let image = new Image();
          // 设置图片的src为你想要添加的图片的 base64 编码字符串
          // image.src = "";
          image.src = this.imageData;
          // 等待图片加载完成
          image.onload = () => {
            // 在画布上绘制图片
            // ctx.drawImage(image, 0, 0, canvas.width, canvas.height);
            ctx.drawImage(image, x, y, image.width * this.imageScale, image.height * this.imageScale);
          };
        },

        clickPre() {
          // 当前页面未被渲染 && 还有上一页
          if (!this.renderingPage && this.pageNo && this.pageNo > 1) {
            this.renderPage(this.pageNo - 1); // 渲染上一页
          }
          //   var page = this.pageNum
          //   page = page > 1 ? page - 1 : this.pageTotalNum
          //   this.pageNum = page
        },
        clickNext() {
          // 当前页面未被渲染 && 还有下一页
          if (
            !this.renderingPage && this.pdfPageNumber &&
            this.pageNo && this.pageNo < this.pdfPageNumber
          ) {
            this.renderPage(this.pageNo + 1); // 渲染下一页
          }
          //   var page = this.pageNum
          //   page = page < this.pageTotalNum ? page + 1 : 1
          //   this.pageNum = page
        },

        clickMerge() {
          // 在这里调用_renderImage()
          // 只在一个page上加签名 不是每个page加签名
          this._renderImage(this.clickPosition.x, this.clickPosition.y);
        }
      }
    }
</script>

<style scoped>
  .canvas-container {
    width: 600px;
    height: 790px;
    border: 1px dashed black;
    position: relative;
    display: flex;
    justify-content: center;
  }
  .pdf-container {
    width: 100%;
    height: 100%;
  }
  .image-container {
    overflow: auto;
    border: 2px solid #ccc;
    height: 200px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
