<template>
  <div>
    <Menu>
      <el-table
        :data="tableData"
        style="width: 100%">

        <el-table-column
          label="签名时间"
          style="width: 20%">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 10px">{{ new Date(scope.row.sig_time).toLocaleString() }}</span>
          </template>
        </el-table-column>

        <el-table-column
          label="文件名称"
          style="width: 20%">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.doc_name + '.pdf' }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column
          label="签名名称"
          style="width: 20%">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.sig_name + '.png' }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column
          label="用户id"
          style="width: 20%">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.user_id }}</el-tag>
              </div>
            </el-popover>
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
    name: "recManagement",
    components: {
      Menu,
    },
    data() {
      return {
        tableData: [],
      }
    },
    created() {
      this.selectRecordList();
    },
    methods: {
      selectRecordList() {
        axios.get('/api/rec/selectRecordListAll').then(response => {
          this.tableData = response.data;
        }).catch(error => {
          console.error('Failed to fetch signature list:', error);
        });

      }
    }
  }
</script>

<style scoped>

</style>
