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
        <el-table-column
          label="用户id"
          style="width: 13%">
          <template slot-scope="scope">
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{ scope.row.user_id }}</el-tag>
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
  export default {
    name: "fileManagement",
    components: {
      Menu,
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
        axios.get('/api/doc/selectAll',).then(response => {
          this.tableData = response.data;
        }).catch(error => {
          console.error('Failed to fetch file list:', error);
        });
      }
    }
  }
</script>

<style scoped>

</style>
