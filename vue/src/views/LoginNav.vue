<template>
  <div>
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">go2go</div>
      </div>
      <div class="front-header-center">
        <el-menu :default-active="router.currentRoute.value.path" router mode="horizontal">
          <el-menu-item index="/front/home">Home</el-menu-item>
        </el-menu>
      </div>
      <div class="front-header-right">
        <div v-if="!data.user.id">
          <el-button @click="router.push('/loginNav/login')">Log in</el-button>
          <el-button @click="router.push('/loginNav/register')">Register</el-button>
        </div>
        <div v-else>
          <el-dropdown style="cursor: pointer; height: 60px">
            <div style="display: flex; align-items: center">
              <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.avatar" alt="">
              <span style="margin-left: 5px;">{{ data.user.name }}</span><el-icon><arrow-down /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goPage('/front/person')">Profile</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/password')">Change Password</el-dropdown-item>
                <el-dropdown-item @click="logout">Log Out</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div class="main-body">
      <RouterView @updateUser="updateUser" />
    </div>
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import { reactive } from "vue";
import request from "@/utils/request.js";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  top: '',
  noticeData: []
})

const logout = () => {
  localStorage.removeItem('xm-user')
  router.push('/loginNav/login')
}
const goPage = (path) => {
  location.href = path
}

const updateUser = () => {
  data.user =  JSON.parse(localStorage.getItem('xm-user') || '{}')
}

</script>

<style scoped>
@import "@/assets/css/front.css";
</style>