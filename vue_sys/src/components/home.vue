<template>
  <div>
    <Menu>
      <!-- 在这里放置 el-main 中的内容 -->
      <el-table :data="tableData">
        <el-table-column prop="date" label="日期" width="140"></el-table-column>
        <el-table-column prop="name" label="姓名" width="120"></el-table-column>
        <el-table-column prop="address" label="地址"></el-table-column>

      </el-table>
    </Menu>
  </div>
</template>

<script>
import Menu from "@/components/menu.vue";

    export default {
        name: "home",
        components: {
          Menu,
      },
    }
</script>

<style scoped>

</style>
