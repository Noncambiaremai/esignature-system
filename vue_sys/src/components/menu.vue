<template>
  <div>
    <el-container style="width: 100vw; height: 100vh; border: 1px solid #eee">
      <el-aside width="15%">
        <!--width="200px;"-->
        <!--style="background-color: rgb(238, 241, 246); "-->
        <el-menu style="height: 100%;">
              <el-menu-item v-show="!notAdmin" index="1-1" @click="gotoHome"><i class="el-icon-house"></i>首页</el-menu-item>
              <el-menu-item v-show="!notAdmin" index="1-2" @click="gotoAddSig"><i class="el-icon-thumb"></i>添加签名</el-menu-item>
              <el-menu-item v-show="!notAdmin" index="1-3" @click="gotoMySig"><i class="el-icon-files"></i>我的签名</el-menu-item>
              <el-menu-item v-show="!notAdmin" index="1-4" @click="gotoAddDoc"><i class="el-icon-document-add"></i>添加文件</el-menu-item>
              <el-menu-item v-show="!notAdmin" index="1-5" @click="gotoMyDoc"><i class="el-icon-notebook-2"></i>我的文件</el-menu-item>
              <el-menu-item v-show="!notAdmin" index="1-6" @click="gotoMergePage"><i class="el-icon-connection"></i>合成区域</el-menu-item>
              <el-menu-item v-show="!notAdmin" index="1-7" @click="gotoRecord"><i class="el-icon-time"></i>签名日志</el-menu-item>

              <el-menu-item v-show="notAdmin" index="1-8" @click="gotoUserManagement"><i class="el-icon-user"></i>用户管理</el-menu-item>
              <el-menu-item v-show="notAdmin" index="1-9" @click="gotoSigManagement"><i class="el-icon-files"></i>签名管理</el-menu-item>
              <el-menu-item v-show="notAdmin" index="1-10" @click="gotoFileManagement"><i class="el-icon-notebook-2"></i>文件管理</el-menu-item>
              <el-menu-item v-show="notAdmin" index="1-11" @click="gotoRecManagement"><i class="el-icon-time"></i>签名日志管理</el-menu-item>
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
      return {
        notAdmin: JSON.parse(sessionStorage.getItem('currentUser'))[4]
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
      gotoMergePage() {
        this.$router.push({name: 'mergePage'});
      },
      gotoRecord() {
        this.$router.push({name: 'record'});
      },
      gotoUserManagement() {
        this.$router.push({name: 'userManagement'});
      },
      gotoSigManagement() {
        this.$router.push({name: 'sigManagement'});
      },
      gotoFileManagement() {
        this.$router.push({name: 'fileManagement'});
      },
      gotoRecManagement() {
        this.$router.push({name: 'recManagement'});
      }
    }
  };
</script>
