<template>
  <div>
    <el-carousel height="400px" :interval="5000">
      <el-carousel-item v-for="item in data.imgs" :key="item">
        <img :src="item" alt="" style="width: 100%">
      </el-carousel-item>
    </el-carousel>

    <div style="display: flex; grid-gap: 40px; width: 80%; margin: 10px auto">
      <!-- Left recommendations -->
      <div style="flex: 1">
        <div style="display: flex; align-items: center; border-bottom: 1px solid #ddd">
          <div class="item" :class="{ 'active' : data.sort === 'hot' }" @click="loadBySort('hot')">Recommended tours</div>
          <div class="item" :class="{ 'active' : data.sort === 'new' }" @click="loadBySort('new')">Recommended car rental services</div>
          <div style="flex: 1; text-align: right">
            <el-button @click="goPage('/front/addComments')" type="primary" style="padding: 10px 30px"><el-icon><Edit /></el-icon>Write a review</el-button>
          </div>
        </div>

        <div>
          <div v-for="item in data.travelList" :key="item.id" style="margin: 20px 0">
            <div style="display: flex; grid-gap: 20px">
              <img :src="item.cover" alt="" style="width: 180px; height: 150px; border-radius: 5px">
              <div style="flex: 1">
                <a :href="'/front/travelDetail?id=' + item.id"><div class="title line1">{{ item.title }}</div></a>
                <div style="color:#666; margin: 20px 0; text-align: justify; height: 60px" class="line3">{{ item.descr }}</div>
                <div style="color: #666; display: flex; align-items: center">
                  <span style="margin-right: 20px"><el-icon size="14" style="top: 2px"><Location /></el-icon> {{ item.location }}</span>
                  <span style="margin-right: 20px">Publisher: {{ item.userName }}</span>
                  <span style="margin-right: 20px"><el-icon size="14" style="top: 2px"><View /></el-icon> {{ item.readCount }}</span>
                  <span style="margin-right: 20px"><el-icon size="14" style="top: 2px"><Clock /></el-icon> {{ item.time }}</span>
                </div>
              </div>
            </div>
          </div>

          <div style="display: flex; justify-content: flex-end" v-if="data.total">
            <el-pagination @current-change="loadTravels" size="small" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
          </div>
        </div>
      </div>

      <!-- Right travel guides -->
      <div style="width: 300px">
        <!-- Travel guides -->
        <div>
          <div style="padding: 10px 0; display: flex; align-items: center; border-bottom: 1px solid #ddd; margin-bottom: 20px">
            <div style="flex: 1; font-size: 18px">Recommended travel guides</div>
            <a style="color: #666" href=""><div>more>></div></a>
          </div>
          <div>
            <div v-for="item in data.articleList" :key="item.id" style="margin: 20px 0">
              <img :src="item.cover" alt="" style="width: 100%; height: 200px; border-radius: 5px">
              <a :href="'/front/articleDetail?id=' + item.id"><div class="title line1">{{ item.title }}</div></a>
            </div>
          </div>
        </div>

        <!-- Customer reviews -->
        <div>
          <div style="padding: 10px 0; border-bottom: 1px solid #ddd">
            <div style="flex: 1; font-size: 18px">Customer reviews</div>
          </div>
          <div v-for="item in data.noticeList" :key="item.id" style="margin: 10px 0">
            <div style="font-size: 12px; color: #666;">{{ item.content }}</div>
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

const data = reactive({
  imgs: [img1, img2, img3],
  sort: 'hot',
  pageNum: 1,
  pageSize: 5,
  total: 0,
  travelList: [],
  articleList: [],
  noticeList: []
})

const goPage = (path) => {
  location.href = path
}

request.get('/article/selectRecommend').then(res => {
  data.articleList = res.data
})

const top = (id) => {

}

const loadBySort = (sort) => {
  data.sort = sort
  loadTravels()
}

const loadTravels = () => {
  request.get('/travels/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      sort: data.sort
    }
  }).then(res => {
    if (res.code === '200') {
      data.travelList = res.data?.list || []
      data.total = res.data?.total
    }
  })
}
loadTravels()
</script>

<style scoped>
.item {
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