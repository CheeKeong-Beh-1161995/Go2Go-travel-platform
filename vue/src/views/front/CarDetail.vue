<template>
  <div style="width: 60%; margin: 10px auto">
    <div style="display: flex; grid-gap: 20px">
      <div style="flex: 1">
        <img :src="data.car.img" alt="" style="width: 100%; height: 300px">
      </div>

      <div style="flex: 1">
        <div style="font-size: 20px">{{ data.car.name }}</div>
        <div style="margin: 10px 0">
          <el-tag style="margin-right: 10px" v-for="item in JSON.parse(data.car.specials || '[]')">{{ item }}</el-tag>
        </div>
        <div style="margin: 10px 0; color: #666; font-size: 13px">{{ data.car.descr }}</div>
        <div style="margin: 10px 0; background-color: #eee; padding: 15px; display: flex; align-items: baseline">
          <span style="font-size: 24px; font-weight: bold; color: orangered; margin-right: 30px">￥{{ data.car.price }}</span>
          <span style="color: #666">inventory：{{ data.car.store }}</span>
        </div>
        <div style="margin: 10px 0; color: #666; font-size: 13px">
          Booking instructions: After the payment is completed, the merchant will confirm whether the booking is successful within 2 working hours (9:00-18:00 on weekdays) at the latest
        </div>
        <div>
          <el-button @click="addOrder" style="padding: 20px 30px; background-color: orange; border-color: orange; color: #fff">Buy now</el-button>
        </div>
      </div>
    </div>

    <div style="margin: 20px 0">
      <div style="padding-left: 10px; border-left: 5px solid #189500; font-size: 20px; margin: 20px 0">Product details</div>
      <div>
        <div v-html="data.car.content"></div>
      </div>
    </div>

    <el-dialog title="Confirm order" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form label-width="70px" style="padding: 20px">
        <el-form-item label="Product name">{{data.car.name }}</el-form-item>
        <el-form-item label="Unit price">{{data.car.price }}</el-form-item>
        <el-form-item label="Quantity">
          <el-input-number :min="1" :max="10" style="width: 150px" v-model="data.form.num" @change="changeNum"></el-input-number>
        </el-form-item>
        <el-form-item label="Total price"><span style="color: red; font-weight: bold; font-size: 16px">￥{{ data.form.total }}</span></el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">Cancel</el-button>
          <el-button type="primary" @click="saveOrder">Confirm order</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive} from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const data = reactive({
  id: router.currentRoute.value.query.id,
  car: {},
  formVisible: false,
  form: {}  // order object
})

const changeNum = () => {
  data.form.total = data.car.price * data.form.num
}

const saveOrder = () => {
  request.post('Orders/add',data.form).then(res =>{
    if (res.code === '200'){
      ElMessage.success('Order placed successfully')
      router.push('/front/carOrders')
    }else {
      ElMessage.error(res.msg)
    }
  })
}

const addOrder = () => {
  data.formVisible = true
  data.form.carId = data.car.id
  data.form.carImg = data.car.img
  data.form.name = data.car.name
  data.form.price = data.car.price
  data.form.num = 1
  data.form.total = data.car.price
}

const load = () => {
  request.get('/car/selectById/' + data.id).then(res => {
    if (res.code === '200') {
      data.car = res.data
    }
  })
}
load()
</script>

<style scoped>
:deep(.el-form-item__label) {
  font-weight: bold !important;
}
:deep(.el-form-item) {
  margin-bottom: 10px;
}
</style>