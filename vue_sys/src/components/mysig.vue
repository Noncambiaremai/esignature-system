<template>
  <div>
    <Menu>
      <el-table
        :data="tableData"
        style="width: 100%">
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
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </Menu>
  </div>
</template>

<script>
  import axios from 'axios';
  import Menu from "@/components/menu.vue";
    export default {
        name: "mysig",
        components: {
          Menu,
        },
        data() {
          return {
            tableData: [],
          }
        },
        created() {
          this.selectSigList();
        },
        methods: {
          selectSigList() {
            axios.get('/api/sig/selectAllByUserId').then(response => {
              this.tableData = response.data;
              console.log(this.tableData);
            }).catch(error => {
              console.error('Failed to fetch signature list:', error);
            });
          },
          // handleEdit(index, row) {
          //   console.log(index, row);
          // },
          // handleDelete(index, row) {
          //   console.log(index, row);
          // }
          handleDelete(index, row) {
            this.$confirm('确定删除该签名?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              // 调用后端接口删除文件
              axios.get('/api/sig/deleteSigBySigId',
                { params: { sig_id: row.sig_id, sig_path: row.sig_path }}).then(response => {});
              window.location.reload();
              this.$message({ type: 'success', message: '删除成功!' });
            }).catch(() => { this.$message({ type: 'info', message: '已取消删除' });
            });
          }
        }
    }
</script>

<style scoped>

</style>
