<template>
  <div class="manager-container">
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">go2go</div>
      </div>
      <div class="manager-header-center">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">Home</el-breadcrumb-item>
          <el-breadcrumb-item>{{ router.currentRoute.value.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="manager-header-right">
        <el-dropdown style="cursor: pointer">
          <div style="padding-right: 20px; display: flex; align-items: center">
            <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.avatar" alt="">
            <span style="margin-left: 5px; color: white">{{ data.user.name }}</span><el-icon color="#fff"><arrow-down /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/person')">Profile</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/password')">Change Password</el-dropdown-item>
              <el-dropdown-item @click="logout">Log Out</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <!-- Below part starts -->
    <div style="display: flex">
      <div class="manager-main-left">
        <el-menu :default-active="router.currentRoute.value.path"
                 :default-openeds="['1', '2']"
                 router
        >
          <el-menu-item index="/manager/home">
            <el-icon><HomeFilled /></el-icon>
            <span>System Home</span>
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>Information Management</span>
            </template>
            <el-menu-item index="/manager/article">Travel Guide Information</el-menu-item>
            <el-menu-item index="/manager/routes">Travel Route Information</el-menu-item>
            <el-menu-item index="/manager/travels">Travel Diary Information</el-menu-item>
            <el-menu-item index="/manager/tourism">Tourism Product Information</el-menu-item>
            <el-menu-item index="/manager/car">Car Product Information</el-menu-item>
            <el-menu-item index="/manager/comments">Comment Information</el-menu-item>
            <el-menu-item index="/manager/orders">User Order Information</el-menu-item>
            <el-menu-item index="/manager/notice">System Announcements</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>User Management</span>
            </template>
            <el-menu-item index="/manager/admin">Admin Information</el-menu-item>
            <el-menu-item index="/manager/user">User Information</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <div class="manager-main-right">
        <RouterView @updateUser="updateUser" />
      </div>
    </div>
    <!-- Below part ends -->
  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}')
})

if (data.user.role !== 'ADMIN'){
  location.href = '/loginNav/adminLogin'
}

const logout = () => {
  localStorage.removeItem('xm-user')
  router.push('/login')
}

const updateUser = () => {
  data.user =  JSON.parse(localStorage.getItem('xm-user') || '{}')
}

if (!data.user.id) {
  logout()
  ElMessage.error('Please log in!')
}
</script>

<style scoped>
@import "@/assets/css/manager.css";
</style>