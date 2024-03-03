<template>
  <div>
    <Menu>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>手势识别电子签名系统</span>
        </div>
        <div class="text item">
          <p>欢迎使用手势识别电子签名系统。这个系统允许您通过手势进行签名，并提供方便的保存和预览功能。</p>
        </div>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>使用说明</span>
        </div>
        <div class="text item">
          <p>在手势区域绘制您的签名。</p>
          <p>点击“保存签名”按钮保存您的签名。</p>
          <p>在电子签名板中预览您的签名。</p>
        </div>
      </el-card>


      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>系统公告</span>
        </div>
        <div class="text item">
          <p>系统升级公告：最新版本已发布，新增了XXX功能。请及时更新您的应用。</p>
        </div>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>联系方式</span>
        </div>
        <div class="text item">
          <p>如有问题或建议，请联系我们：</p>
          <p>Email: support@example.com</p>
          <p>电话：123-456-7890</p>
        </div>
      </el-card>

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
  .clearfix {
    font-size: 30px;
  }
  .text item {
    font-size: 20px;
  }
</style>
