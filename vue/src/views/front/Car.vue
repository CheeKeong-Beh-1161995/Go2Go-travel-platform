<template>
  <div>
    <div class="bg"></div>
    <div style="width: 60%; margin: 20px auto">
      <div style="display: flex; align-items: center; margin-bottom: 20px">
        <div style="flex: 1; padding-left: 10px; border-left: 5px solid orangered; font-size: 20px;">Car Available</div>
        <div style="width: fit-content; font-weight: bold">
          <span style="cursor: pointer" :class="{ 'active' : data.sort === 'hot' }" @click="loadByHot">Most popular</span> |
          <span style="cursor: pointer" :class="{ 'active' : data.sort === 'new'}" @click="loadByNew">Latest</span>
        </div>
      </div>

      <div style="margin: 20px 0">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in data.carList" :key="item.id" style="margin-bottom: 20px; cursor: pointer" @click="goPage('/front/carDetail?id=' + item.id)">
            <img :src="item.img" alt="" style="width: 100%; height: 130px; border-radius: 5px">
            <div style="font-size: 16px; margin: 5px 0" class="line2 title">{{ item.name }}</div>
            <div style="display: flex; align-items: center">
              <div style="color: orange; font-size: 20px; font-weight: bold; flex: 1">${{ item.price }}</div>
<!--              <div style="color: #666">Sold: {{ item.num }}</div>-->
            </div>
          </el-col>
        </el-row>
        <div style="margin: 10px 0" v-if="data.total">
          <el-pagination size="small" @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive} from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";

const data = reactive({
  carList: [],
  pageNum: 1,
  pageSize: 8,
  total: 0,
  sort: 'hot'
})

const goPage = (path) => {
  location.href = path
}

const load = () => {
  request.get('/car/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      sort: data.sort
    }
  }).then(res => {
    if (res.code === '200') {
      data.carList = res.data?.list || []
      data.total = res.data?.total
    }else if (res.code === '401') {
      router.push('/loginNav/login')
    }
  })
}
const loadByHot = () =>{
  data.sort = 'hot';
  load()
}
const loadByNew = () =>{
  data.sort = 'new';
  load()
}
load()
</script>

<style scoped>
.bg {
  height: 400px;
  background-image: url("@/assets/imgs/su7.jpg");
  background-size: 100% 110%;
}
.active {
  color: red;
}
.title:hover {
  color: orangered!important;
}
</style>