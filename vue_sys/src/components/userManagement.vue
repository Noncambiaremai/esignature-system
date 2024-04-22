<template>
  <div>
    <Menu>
      <el-table
        :data="tableData"
        style="width: 100%">
        <el-table-column
          label="用户id"
          style="width: 13%">
          <template slot-scope="scope">
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{ scope.row.id }}</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="用户账号/手机号"
          style="width: 13%">
          <template slot-scope="scope">
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{ scope.row.user_id }}</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="用户名"
          style="width: 13%">
          <template slot-scope="scope">
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{ scope.row.user_name }}</el-tag>
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
        name: "userManagement",
        components: {
          Menu,
        },
      data() {
        return {
          tableData: [],
        }
      },
      created() {
        this.selectUserList();
      },
      methods: {
        selectUserList() {
          axios.get('/api/user/selectAll',).then(response => {
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
