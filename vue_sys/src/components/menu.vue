<template>
  <div>
    <el-container style="width: 100vw; height: 100vh; border: 1px solid #eee">
      <el-aside width="15%">
        <!--width="200px;"-->
        <!--style="background-color: rgb(238, 241, 246); "-->
        <el-menu style="height: 100%;">
              <el-menu-item index="1-1" @click="gotoHome"><i class="el-icon-house"></i>首页</el-menu-item>
              <el-menu-item index="1-2" @click="gotoAddSig"><i class="el-icon-thumb"></i>添加签名</el-menu-item>
              <el-menu-item index="1-3" @click="gotoMySig"><i class="el-icon-files"></i>我的签名</el-menu-item>
              <el-menu-item index="1-4" @click="gotoAddDoc"><i class="el-icon-document-add"></i>添加文件</el-menu-item>
              <el-menu-item index="1-5" @click="gotoMyDoc"><i class="el-icon-notebook-2"></i>我的文件</el-menu-item>
              <el-menu-item index="1-6" @click="gotoRecord"><i class="el-icon-time"></i>签名日志</el-menu-item>

          <!--<el-submenu index="2">-->
            <!--<template slot="title"><i class="el-icon-menu"></i>导航二</template>-->
            <!--<el-menu-item-group>-->
              <!--<template slot="title">分组一</template>-->
              <!--<el-menu-item index="2-1">选项1</el-menu-item>-->
              <!--<el-menu-item index="2-2">选项2</el-menu-item>-->
            <!--</el-menu-item-group>-->
            <!--<el-menu-item-group title="分组2">-->
              <!--<el-menu-item index="2-3">选项3</el-menu-item>-->
            <!--</el-menu-item-group>-->
            <!--<el-submenu index="2-4">-->
              <!--<template slot="title">选项4</template>-->
              <!--<el-menu-item index="2-4-1">选项4-1</el-menu-item>-->
            <!--</el-submenu>-->
          <!--</el-submenu>-->
        </el-menu>
      </el-aside>

      <el-container>
        <el-header style="text-align: right; font-size: 12px">
          <el-dropdown>
            <i class="el-icon-setting" style="margin-right: 15px"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>查看</el-dropdown-item>
              <el-dropdown-item>新增</el-dropdown-item>
              <el-dropdown-item>删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span>momo</span>
        </el-header>

        <el-main>
          <slot></slot>
        </el-main>

      </el-container>
    </el-container>
  </div>
</template>


<style>
  .el-header {
    background-color: #b6c9b8;
    color: #333;
    line-height: 60px;
  }

  .el-aside {
    /*color: #4d785c;*/
  }
  .el-button:focus {
    outline: none;
  }

</style>

<script>
import axios from 'axios';

export default {
    name: "menu",
    data() {
      const item = {
        date: '2016-05-02',
        name: 'momo',
        address: '上海市普陀区金沙江路 1518 弄'
      };
      return {
        tableData: Array(20).fill(item)
      }
    },
    methods: {
      gotoHome() {
        this.$router.push({name: 'home'});
      },
      gotoAddSig() {
        this.$router.push({name: 'addsig'});
      },
      gotoMySig() {
        this.$router.push({name: 'mysig'});
      },
      gotoAddDoc() {
        this.$router.push({name: 'adddoc'});
      },
      gotoMyDoc() {
        this.$router.push({name: 'mydoc'});
      },
      gotoRecord() {
        this.$router.push({name: 'record'});
      },
    }
  };
</script>
