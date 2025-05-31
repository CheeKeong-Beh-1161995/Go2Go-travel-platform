<template>
  <div>
    <div class="front-notice">
      <div style="padding-left: 20px; flex: 1">
        <i style="font-size: 20px; color: #189500">Embark on Your Next Adventure Today</i>
      </div>
<!--      <iframe scrolling="no" src="https://widget.tianqiapi.com/?style=tg&skin=pitaya" frameborder="0" width="470" height="60" allowtransparency="true"></iframe>-->
<!--      <i style="font-size: 20px; color: #189500">Obey Your Thirst</i>-->
    </div>
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">go2go</div>
      </div>
      <div class="front-header-center">
        <el-menu :default-active="router.currentRoute.value.path" router mode="horizontal">
          <el-menu-item index="/front/home_login">Home</el-menu-item>
          <el-menu-item index="/front/tourism">Tours</el-menu-item>
          <el-menu-item index="/front/car">Car rental services</el-menu-item>
          <el-menu-item index="/front/about">About</el-menu-item>
          <el-menu-item index="/front/guest">Support</el-menu-item>
        </el-menu>
      </div>
      <div style="width: 250px">
        <el-input style="width: 150px; margin-right: 5px" v-model="data.search" placeholder="Please enter your destination"></el-input>
        <el-button type="primary" @click="goPage('/front/search?name=' + data.search)">Search</el-button>
      </div>
      <div class="front-header-right">
        <div v-if="!data.user.id">
          <el-button @click="router.push('/loginNav/login')">login</el-button>
          <el-button @click="router.push('/loginNav/register')">registered</el-button>
        </div>
        <div v-else>
          <el-dropdown style="cursor: pointer; height: 60px">
            <div style="display: flex; align-items: center">
              <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.avatar" alt="">
              <span style="margin-left: 5px;">{{ data.user.name }}</span><el-icon><arrow-down /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goPage('/front/person')">Personal Center</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/collect')">View Your Collect</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/password')">Change your password</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/orders')">View Your Orders</el-dropdown-item>
                <el-dropdown-item @click="logout">Sign out</el-dropdown-item>
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
    noticeData: [],
    search: ''
  })

  const logout = () => {
    localStorage.removeItem('xm-user')
    router.push('/loginNav/login')
  }
  const goPage = (path) => {
    if (!data.user.id){
      location.href = '/loginNav/login'
    }else {
      location.href = path
    }
  }

  const updateUser = () => {
    data.user =  JSON.parse(localStorage.getItem('xm-user') || '{}')
  }

</script>

<style scoped>
@import "@/assets/css/front.css";
</style>