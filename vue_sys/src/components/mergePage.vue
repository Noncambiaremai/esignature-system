<template>
    <div>
      <Menu>
        <div style="display: flex;">
          <!-- 左侧容器 -->
          <div style="width: 33%; margin-right: 15px;">
            <div style="display: flex;  flex-direction: column;">

              <div style="display: flex;">
                <el-button round style="width: 200px; height: 70px; font-size: 19px; font-family: PingFang SC; margin-bottom: 10px;"
                                            @click="startChooseSig">选择签名</el-button>
                <el-button round style="width: 200px; height: 70px; font-size: 19px; font-family: PingFang SC; margin-bottom: 10px;"
                           @click="startChooseFile">选择文件</el-button>



              </div>

              <div style="display: flex;">
                <el-button  round style="width: 200px; height: 70px; font-size: 19px; font-family: PingFang SC; margin-bottom: 10px;"
                  :title="'上一页'" @click.stop="prePage"> 上一页</el-button>
                <el-button  round style="width: 200px; height: 70px; font-size: 19px; font-family: PingFang SC; margin-bottom: 10px;"
                  :title="'下一页'" @click.stop="nextPage"> 下一页</el-button>
              </div>
              <div style="overflow: auto;  border: 2px solid #ccc; height: 100px">
                <img v-show="imageData!=''" :src="imageData" alt="图片" style="max-width: 100px; max-height: 100px;">
              </div>
            </div>
          </div>

          <!-- 右侧容器 -->
          <div style="width: 66.666%; overflow: auto;  border: 2px solid #ccc; height: 730px">
            <div class="page">{{pageNum}}/{{pageTotalNum}} </div>
            <PDF :src="pdfData" :page="pageNum" @progress="loadedRatio = $event" @page-loaded="pageLoaded($event)"
                 @num-pages="pageTotalNum=$event" @error="pdfError($event)"  @link-clicked="page = $event">
            </PDF>
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
  import Menu from "@/components/menu.vue";
  import PDF from 'vue-pdf';
    export default {
        name: "mergePage",
        components: {
          Menu,
          PDF,
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
            pdfData: "",
            pageNum: 1,
            pageTotalNum: 1,
            // 加载进度
            loadedRatio: 0,
            curPageNum: 0,
            url: "http://storage.xuetangx.com/public_assets/xuetangx/PDF/PlayerAPI_v1.0.6.pdf",
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
          // console.log(row.image);
          // axios.get('/api/sig/selectFileByDocPath',
          //   { params: { filePath: row.doc_path }}).then(response => {
          //   // console.log(response.data.file);
          //   // this.pdfData = response.data.file;
          // });
        },
        chooseFile(index, row) {
          this.filedialogTableVisible = false;
          axios.get('/api/doc/selectFileByDocPath',
            { params: { filePath: row.doc_path }}).then(response => {
            console.log(response.data.file);
            this.pdfData = response.data.file;
          });

        },
        // 上一页函数，
        prePage() {
          var page = this.pageNum
          page = page > 1 ? page - 1 : this.pageTotalNum
          this.pageNum = page
        },
        // 下一页函数
        nextPage() {
          var page = this.pageNum
          page = page < this.pageTotalNum ? page + 1 : 1
          this.pageNum = page
        },
        // 页面加载回调函数，其中e为当前页数
        pageLoaded(e) {
          this.curPageNum = e
        },
        // 错误时回调函数。
        pdfError(error) {
          console.error(error)
        },
      }
    }
</script>

<style scoped>

</style>
