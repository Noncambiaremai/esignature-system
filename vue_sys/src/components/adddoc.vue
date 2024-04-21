<template>
  <div>
    <Menu>
      <el-upload
        style="float: left"
        class="upload"
        drag
        action="http://localhost:8080/api/doc/upload"
        :on-success="handleSuccess"
        :before-upload="beforeUpload"
        :data="{ userId: userId }"
        multiple>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">可接受文件格式：PDF</div>
      </el-upload>
    </Menu>
  </div>

</template>

<script>
import Menu from "@/components/menu.vue";
    export default {
        name: "adddoc",
        components: {
          Menu,
        },
        data() {
          return {
            fileList: [],
            userId: JSON.parse(sessionStorage.getItem('currentUser'))[1]
          }
        },
        methods: {
          handleSuccess(response, file, fileList) {
            // 文件上传成功后的处理逻辑 response为后端返回数据
            console.log(response);
            this.$message.success('文件上传成功');
          },
          beforeUpload(file) {
            // console.log('文件名', file.name);
            const extension = file.name.split('.').pop(); // 获取文件扩展名
            // console.log('文件扩展名', extension);
            if (!['pdf'].includes(extension)) {
              this.$message.error('只能上传pdf格式的文件');
              return false; // 取消上传
            }
            return true; // 允许上传
          }
        }
    }
</script>

<style scoped>

</style>
