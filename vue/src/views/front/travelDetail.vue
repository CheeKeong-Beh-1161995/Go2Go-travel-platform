<template>
  <div style="width: 60%; margin: 10px auto">
    <div style="margin: 20px; font-size: 24px; font-weight: bold; text-align: center">{{ data.travel.title }}</div>
    <div style="display: flex; align-items: center; color: #666; margin: 20px 0">
      <img style="width: 50px; height: 50px" :src="data.travel.userAvatar" alt="">
      <sapn style="margin:  0 15px"><el-icon size="14" style="top: 2px"><User/></el-icon> {{ data.travel.userName }}</sapn>
      <sapn style="margin:  0 15px"><el-icon size="14" style="top: 2px"><Clock/></el-icon> {{ data.travel.time }}</sapn>
      <sapn style="margin:  0 15px"><el-icon size="14" style="top: 2px"><View/></el-icon> {{ data.travel.readCount }}</sapn>
      <strong style="color: orange; font-size: 20px; margin-right: 5px">{{ data.travel.praiseCount }}</strong>
      <div class="top" @click="top(data.travel.id)">顶</div>
    </div>
    <div style="border: 1px dashed #ccc; padding: 20px; display: flex; align-items: center; margin: 20px 0">
      <div style="flex: 1">
        <el-icon style="top: 5px" size="20" color="orange"><Location/></el-icon>
        出行地点 <span style="color: orange">/</span> {{ data.travel.location }}
      </div>
      <div style="flex: 1">
        <el-icon style="top: 5px" size="20" color="orange"><Watch/></el-icon>
        出发日期 <span style="color: orange">/</span> {{ data.travel.startDate }}
      </div>
      <div style="flex: 1">
        <el-icon style="top: 5px" size="20" color="orange"><Calendar/></el-icon>
        出行天数 <span style="color: orange">/</span> {{ data.travel.days }}天
      </div>
      <div style="flex: 1">
        <el-icon style="top: 5px" size="20" color="orange"><Money/></el-icon>
        花费金额 <span style="color: orange">/</span> {{ data.travel.money }}元
      </div>
    </div>

    <div style="margin: 30px 0">
      <div v-html="data.travel.content"></div>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";

const data = reactive({
  id: router.currentRoute.value.query.id,
  travel: {}
})

request.put('/travels/updateReadCount/' + data.id).then(res => {
  load()
})

const load = () => {
  request.get('/travels/selectById/' + data.id).then(res => {
    data.travel = res.data
  })
}

const top = (id) => {

}

</script>

<style scoped>

</style>