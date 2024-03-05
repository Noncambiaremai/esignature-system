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
          label="签名时间"
          style="width: 20%">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 10px">{{ scope.row.date }}</span>
          </template>
        </el-table-column>

        <el-table-column
          label="文件名称"
          style="width: 20%">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>姓名: {{ scope.row.name }}</p>
              <p>住址: {{ scope.row.address }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.name }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column
          label="签名名称"
          style="width: 20%">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>姓名: {{ scope.row.name }}</p>
              <p>住址: {{ scope.row.address }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.name }}</el-tag>
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
import Menu from "@/components/menu.vue";
    export default {
        name: "record",
        components: {
          Menu,
        },
    }
</script>

<style scoped>

</style>
