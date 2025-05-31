<template>
  <div>
    <el-carousel height="400px" :interval="5000">
      <el-carousel-item v-for="item in data.imgs" :key="item">
        <img :src="item" alt="" style="width: 100%">
      </el-carousel-item>
    </el-carousel>

    <div style="display: flex; grid-gap: 40px; width: 80%; margin: 10px auto">
      <!-- recommendations -->
      <div style="flex: 1">
        <div style="display: flex; align-items: center; border-bottom: 1px solid #ddd">
          <div class="item" :class="{ 'active' : data.flag === 'left' }" @click="loadReview">Travel blog</div>
          <div class="itemCar" :class="{ 'active' : data.flag === 'right' }" @click="loadArticle">Recommended Travel Guides</div>
          <div style="flex: 1; text-align: right">
            <el-button @click="goPage('/loginNav/login')" type="primary" style="padding: 10px 30px"><el-icon><Edit /></el-icon>Write a travel blog</el-button>
          </div>
        </div>
        <div>
          <div v-if="data.flag === 'left'" v-for="item in data.reviewsList" :key="item.id" style="margin: 20px 0">
            <div style="display: flex; grid-gap: 20px">
              <img :src="item.userAvatar" alt="" style="width: 180px; height: 150px; border-radius: 5px">
              <div style="flex: 1">
                <a :href="'/front/reviewDetail?id=' + item.id"><div class="title line1">{{ item.title }}</div></a>
                <div style="color:#666; margin: 20px 0; text-align: justify; height: 60px" class="line3">{{ item.descr }}</div>
                <div style="color: #666; display: flex; align-items: center">
                  <span style="margin-right: 20px"><el-icon size="14" style="top: 2px"><Clock /></el-icon> {{ item.time }}</span>
                </div>
              </div>
            </div>
          </div>
          <div v-if="data.flag === 'right'" v-for="item in data.articleList" :key="item.id" style="margin: 20px 0">
            <div style="display: flex; grid-gap: 20px">
              <img :src="item.cover" alt="" style="width: 180px; height: 150px; border-radius: 5px">
              <div style="flex: 1">
                <a :href="'/loginNav/login'"><div class="title line1">{{ item.title }}</div></a>
                <div style="color:#666; margin: 20px 0; text-align: justify; height: 60px" class="line3">{{ item.descr }}</div>
                <div style="color: #666; display: flex; align-items: center">
                  <span style="margin-right: 20px"><el-icon size="14" style="top: 2px"><View /></el-icon> {{ item.readCount }}</span>
                  <span style="margin-right: 20px"><el-icon size="14" style="top: 2px"><Clock /></el-icon> {{ item.date }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div style="width: 300px">
        <div>
          <div style="padding: 10px 0; display: flex; align-items: center; border-bottom: 1px solid #ddd; margin-bottom: 20px">
            <div style="flex: 1; font-size: 18px">Tours Review</div>
            <a style="color: #666" href="/loginNav/login"><div>more>></div></a>
          </div>
          <div>
            <div v-for="item in data.travelsList" :key="item.id" style="margin: 20px 0">
              <img :src="item.cover" alt="" style="width: 100%; height: 200px; border-radius: 5px">
              <a :href="'/loginNav/login'"><div class="title line1">{{ item.title }}</div></a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <Footer />
  </div>
</template>

<script setup>
import { reactive } from "vue";
import img1 from '@/assets/imgs/1.jpg'
import img2 from '@/assets/imgs/2.jpg'
import img3 from '@/assets/imgs/3.jpg'
import {Edit, Location, View, Clock} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import Footer from "@/components/Footer.vue"
import router from "@/router/index.js";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  imgs: [img1, img2, img3],
  flag: 'left',
  pageNum: 1,
  pageSize: 5,
  total: 0,
  articleList: [],
  reviewsList: [],
  travelsList: [],
})

const goPage = (path) => {
  if (!data.user.id){
    location.href = '/loginNav/login'
  }else {
    location.href = path
  }
}

request.get('/review/selectPage', {
  params: {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    // sort: data.sort
  }
}).then(res => {
  if (res.code === '200') {
    data.reviewsList = res.data?.list || []
    data.total = res.data?.total
  }else if (res.code === '401') {
    router.push('/loginNav/login')
  }
})

request.get('/travels/selectRecommend').then(res => {
  if (res.code === '200'){
    data.travelsList = res.data
  }else if (res.code === '401') {
    router.push('/loginNav/login')
  }
})


const loadArticle = () =>{
  data.flag = 'right'
  location.href = '/loginNav/login'
  if (!data.user.id){
    location.href = '/loginNav/login'
  }else {
    request.get('/article/selectPage', {
      params: {
        pageNum: data.pageNum,
        pageSize: data.pageSize,
        // sort: data.sort
      }
    }).then(res => {
      if (res.code === '200') {
        data.articleList = res.data?.list || []
        data.total = res.data?.total
      }else if (res.code === '401') {
        router.push('/loginNav/login')
      }
    })
  }

}

const loadReview = () =>{
  data.flag = 'left'
  if (!data.user.id){
    location.href = '/loginNav/login'
  }else {
    request.get('/review/selectPage', {
      params: {
        pageNum: data.pageNum,
        pageSize: data.pageSize,
        // sort: data.sort
      }
    }).then(res => {
      if (res.code === '200') {
        data.reviewsList = res.data?.list || []
        data.total = res.data?.total
      }else if (res.code === '401') {
        router.push('/loginNav/login')
      }
    })
  }

}

// const loadCarService = () => {
//   request.get('/car/selectPage', {
//     params: {
//       pageNum: data.pageNum,
//       pageSize: data.pageSize,
//       // sort: data.sort
//     }
//   }).then(res => {
//     if (res.code === '200') {
//       data.carList = res.data?.list || []
//       data.total = res.data?.total
//     }else if (res.code === '401') {
//       router.push('/loginNav/login')
//     }
//   })
// }

// const loadTourService = () => {
//   request.get('/tourism/selectPage', {
//     params: {
//       pageNum: data.pageNum,
//       pageSize: data.pageSize,
//       // sort: data.sort
//     }
//   }).then(res => {
//     if (res.code === '200') {
//       data.tourismList = res.data?.list || []
//       data.total = res.data?.total
//     }else if (res.code === '401') {
//       router.push('/loginNav/login')
//     }
//   })
// }

// const loadTravels = () => {
//   request.get('/travels/selectPage', {
//     params: {
//       pageNum: data.pageNum,
//       pageSize: data.pageSize,
//       // sort: data.sort
//     }
//   }).then(res => {
//     if (res.code === '200') {
//       data.travelList = res.data?.list || []
//       data.total = res.data?.total
//     }else if (res.code === '401') {
//       router.push('/loginNav/login')
//     }
//   })
// }
// loadReview()
// loadTravels()
</script>

<style scoped>
.item {
  padding: 10px 20px;
  font-size: 18px;
  cursor: pointer;
}
.itemCar {
  padding: 10px 20px;
  font-size: 18px;
  cursor: pointer;
}
.active {
  border-bottom: 1px solid #189500;
  color: #189500;
}
.title {
  font-size: 18px;
  color: #333;
}
.title:hover {
  color: #189500;
  text-decoration: underline;
}
</style>