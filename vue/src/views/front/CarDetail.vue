<template>
  <div style="width: 60%; margin: 10px auto">
    <div style="display: flex; grid-gap: 20px">
      <div style="flex: 1">
        <img :src="data.tourism.img" alt="" style="width: 100%; height: 300px">
      </div>

      <div style="flex: 1">
        <div style="font-size: 20px">{{ data.tourism.name }}</div>
        <div style="margin: 10px 0">
          <el-tag style="margin-right: 10px" v-for="item in JSON.parse(data.tourism.specials || '[]')">{{ item }}</el-tag>
        </div>
        <div style="margin: 10px 0; color: #666; font-size: 13px">{{ data.tourism.descr }}</div>
        <div style="margin: 10px 0; background-color: #eee; padding: 15px; display: flex; align-items: baseline">
          <span style="font-size: 24px; font-weight: bold; color: orangered; margin-right: 30px">$ {{ data.tourism.price }}</span>
          <span style="color: #666">Stock: {{ data.tourism.store }}</span>
          <span style="margin-left: 10px; color: #666">Discount: {{ (data.tourism.discount * 100) }}%</span>
        </div>
        <div style="margin: 10px 0; color: #666; font-size: 13px">
          Booking Note: After payment is completed, the merchant will confirm whether the booking is successful within a maximum of 2 working hours (working hours 9:00-18:00).
        </div>
        <div>
          <el-button @click="addOrder" style="padding: 20px 30px; background-color: orange; border-color: orange; color: #fff">Booking now</el-button>
        </div>
      </div>
    </div>

    <div style="margin: 20px 0">
      <div style="padding-left: 10px; border-left: 5px solid #189500; font-size: 20px; margin: 20px 0">Introduction</div>
      <div>
        <div v-html="data.tourism.content"></div>
      </div>
    </div>

    <el-dialog title="Confirm Order" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form label-width="150px" style="padding: 20px">
        <el-form-item label="Product Name">{{ data.tourism.name }}</el-form-item>
        <el-form-item label="Unit Price">{{ data.tourism.price }}</el-form-item>
        <el-form-item label="Days">
          <el-input-number :min="1" :max="10" style="width: 150px" v-model="data.form.num" @change="changeNum"></el-input-number>
        </el-form-item>
        <el-form-item label="Total Price"><span style="color: red; font-weight: bold; font-size: 16px">$ {{ data.form.total }}</span></el-form-item>
        <el-form-item label="Start Date"><el-date-picker v-model="data.form.start" type="date" placeholder="Select date" :picker-options="pickerOptions"></el-date-picker></el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">Cancel</el-button>
          <el-button type="primary" @click="saveOrder">Confirm Order</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import { ElMessage } from "element-plus";

const data = reactive({
  id: router.currentRoute.value.query.id,
  tourism: {},
  formVisible: false,
  form: {}  // Represents the order object
});

const changeNum = () => {
  data.form.total = data.tourism.price * data.form.num * data.tourism.discount;
  data.form.total = data.form.total.toFixed(2);
};

const saveOrder = () => {
  request.post("orders/add", data.form).then((res) => {
    if (res.code === "200") {
      ElMessage.success("Order placed successfully");
      router.push("/front/orders");
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const addOrder = () => {
  data.formVisible = true;
  data.form.tourismId = data.tourism.id;
  data.form.tourismImg = data.tourism.img;
  data.form.name = data.tourism.name;
  data.form.price = data.tourism.price;
  data.form.num = 1;
  data.form.total = (data.tourism.price * data.tourism.discount).toFixed(2);
};

const load = () => {
  request.get("/car/selectById/" + data.id).then((res) => {
    if (res.code === "200") {
      data.tourism = res.data;
    }
  });
};
load();
</script>

<style scoped>
:deep(.el-form-item__label) {
  font-weight: bold !important;
}
:deep(.el-form-item) {
  margin-bottom: 10px;
}
</style>