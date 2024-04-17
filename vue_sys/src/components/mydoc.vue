<template>
  <div>
    <Menu>
      <el-table
        :data="tableData"
        style="width: 100%">
        <el-table-column
          label="文件名称"
          style="width: 25%">
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
        <el-table-column
          label="文件类型"
          style="width: 13%">
          <template slot-scope="scope">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.doc_type }}</el-tag>
              </div>
          </template>
        </el-table-column>
        <el-table-column
          label="文件状态"
          style="width: 13%">
          <template slot-scope="scope">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.doc_status === false ? '未签名' : '已签名' }}</el-tag>
              </div>
          </template>
        </el-table-column>
        <el-table-column
          label="文件下载次数"
          style="width: 13%">
          <template slot-scope="scope">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.download_count }}</el-tag>
              </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" style="width: 30%">
          <template slot-scope="scope">
            <div class="button-group">
              <!--@click="handleEdit(scope.$index, scope.row)"-->

            <!--<el-button-->
              <!--size="mini"-->
              <!--@click="handleEdit(scope.$index, scope.row)">预览</el-button>-->
            <!--</div>-->
            <!--<div class="button-group-single">-->
              <!--<el-button-->
                <!--size="mini"-->
                <!--@click="handleEdit(scope.$index, scope.row)">下载</el-button>-->
              <el-button
                size="mini"
                type="success"
                @click="handleDownload(scope.$index, scope.row)">下载</el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </Menu>
  </div>
</template>

<script>
import axios from 'axios';
import Menu from "@/components/menu.vue";
import PDF from 'vue-pdf';
    export default {
        name: "mydoc",
        components: {
          Menu,
          PDF,
        },
        data() {
            return {
              tableData: [],
            }
        },
        created() {
            this.selectFileList();
        },
        methods: {
          selectFileList() {
            axios.get('/api/doc/selectAllByUserId').then(response => {
              this.tableData = response.data;
            }).catch(error => {
              console.error('Failed to fetch file list:', error);
            });
          },
          handleDelete(index, row) {
            this.$confirm('确定删除该文件?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              // 调用后端接口删除文件
              axios.get('/api/doc/deleteDocByDocId',
                { params: { doc_id: row.doc_id, doc_path: row.doc_path }}).then(response => {});
              window.location.reload();
              this.$message({ type: 'success', message: '删除成功!' });
            }).catch(() => { this.$message({ type: 'info', message: '已取消删除' });
            });
          },

          // 下载PDF文件
          handleDownload(index, row) {
            axios.get('/api/doc/downloadDoc', {
              params: { doc_name: row.doc_name },
              responseType: 'blob' // 指定响应类型为 Blob
            })
              .then(response => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', `${row.doc_name}.pdf`); // 使用行数据中的文档名称
                document.body.appendChild(link);
                link.click();
                this.$message({ type: 'success', message: '下载成功!' });
              })
              .catch(error => {
                console.error('下载PDF出错：', error);
                this.$message({ type: 'error', message: '下载失败，请重试!' });
              });
          }
        }
    }
</script>

<style scoped>
  .custom-popover {
    width: 500px; /* 设置弹出框的宽度 */
  }
  .button-group {
    display: flex; /* 设置为弹性布局 */
    margin-bottom: 5px; /* 设置按钮组之间的垂直间距 */
  }
</style>
