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
            <el-button
              size="mini" type="success" @click="dialogTableVisible = true">添加签名</el-button>
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
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog :visible.sync="dialogTableVisible">
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
                @click="handleChoose(scope.$index, scope.row)">选择</el-button>
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
    export default {
        name: "mydoc",
        components: {
          Menu,
        },
        data() {
            return {
              tableData: [],
              sigTableData: [{
                date: '2016-05-04',
                name: '王小虎',
                address: '上海市普陀区金沙江路 1518 弄'
              }, {
                date: '2016-05-03',
                name: '王小虎',
                address: '上海市普陀区金沙江路 1518 弄'
              }],
              dialogTableVisible: false,
            }
        },
        created() {
            this.selectFileList();
            this.selectSigList();
        },
        methods: {
          selectFileList() {
            axios.get('/api/doc/selectAllByUserId').then(response => {
              this.tableData = response.data;
              console.log(this.tableData);
            }).catch(error => {
              console.error('Failed to fetch file list:', error);
            });
          },
          selectSigList() {
            axios.get('/api/sig/selectAllByUserId').then(response => {
              this.sigTableData = response.data;
              console.log(this.sigTableData);
            }).catch(error => {
              console.error('Failed to fetch signature list:', error);
            });
          },
          handleChoose(index, row) {
            console.log(row);
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
  .button-group-single {
    display: block; /* 设置为块级元素 */
  }
</style>
