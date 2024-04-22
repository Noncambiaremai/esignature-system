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

        <el-table-column
          label="用户id"
          style="width: 20%">
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
    name: "sigManagement",
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
        // JSON.parse(sessionStorage.getItem('currentUser'))[1]
        axios.get('/api/sig/selectAll',).then(response => {
          this.tableData = response.data;
          // console.log(this.tableData);
        }).catch(error => {
          console.error('Failed to fetch signature list:', error);
        });
      }
    }

  }
</script>

<style scoped>

</style>
