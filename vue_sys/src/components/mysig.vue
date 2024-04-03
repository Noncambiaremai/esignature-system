<template>
  <div>
    <Menu>
      <el-table
        :data="tableData"
        style="width: 100%">
        <el-table-column type="expand" style="width: 20%">
          <div class="block">
            <el-image :src="src" style="width: 50%; height: 30%;" fit="contain">
              <div slot="placeholder" class="image-slot">
                加载中<span class="dot">...</span>
              </div>
            </el-image>
          </div>
        </el-table-column>

        <el-table-column
          label="签名创建时间"
          style="width: 20%">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 10px">{{ new Date(scope.row[4]).toLocaleString()}}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="签名名称"
          style="width: 20%">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row[2] }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="操作" style="width: 20%">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
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
            src: 'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg'
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
        }
    }
</script>

<style scoped>

</style>
