<template>
  <div>
    <Menu>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>手势识别电子签名系统</span>
        </div>
        <div class="text item">
          <p>欢迎使用手势识别电子签名系统。这个系统允许您打开摄像头通过手势识别进行签名，并提供方便的文件预览和保存功能。</p>
        </div>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>使用说明</span>
        </div>
        <div class="text item">
          <p>点击“添加签名”页面，打开摄像头后绘制您的签名。系统主要识别您的食指和拇指的夹角。</p>
          <p>当您的食指和拇指夹角较大，趋于90°夹角，系统不会记录您的手势轨迹，当您的食指和拇指夹角较小趋于平行时，系统会记录您的手势轨迹。</p>
          <p>点击“保存签名轨迹”按钮可保存您的签名，在我的签名页面可以查看您添加过的签名信息。</p>
          <p>点击“添加文件”页面，可以上传您需要签名的文件。点击“我的文件”页面，可以查看您添加过的文件信息。</p>
          <p>点击“合成区域”页面，您可以选择需要添加签名的文件以及您想要添加的签名，点击文件的任意位置添加任意大小的签名图片。</p>
          <p>添加签名图片过后，您可以在“我的文件”页面对新文件进行下载。</p>
        </div>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>联系方式</span>
        </div>
        <div class="text item">
          <p>如有问题或建议，请联系我们：Email: wang_j.y@foxmail.com</p>
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
