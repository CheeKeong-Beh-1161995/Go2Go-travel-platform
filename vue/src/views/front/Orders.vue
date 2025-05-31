<!--<template>-->
<!--  <div style="width: 80%; margin: 10px auto; padding: 20px" class="card">-->
<!--    <div style="margin-bottom: 10px">-->
<!--      <el-input v-model="data.orderNo" prefix-icon="Search" style="width: 280px; margin-right: 10px" placeholder="Please enter the order number to check"></el-input>-->
<!--      <el-button type="info" plain @click="load">Inquire</el-button>-->
<!--      <el-button type="warning" plain style="margin: 0 10px" @click="reset">reset</el-button>-->
<!--    </div>-->

<!--    <div class="card" style="margin-bottom: 5px">-->
<!--      <el-button type="danger" plain @click="delBatch">Batch Delete</el-button>-->
<!--      <el-button type="info" plain @click="handleExport">Export Excel</el-button>-->
<!--    </div>-->

<!--    <div class="card" style="margin-bottom: 10px">-->
<!--      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">-->
<!--        <el-table-column prop="orderNo" label="Order number" width="150" show-overflow-tooltip></el-table-column>-->
<!--        <el-table-column prop="name" label="The title of the product" show-overflow-tooltip></el-table-column>-->
<!--        <el-table-column prop="tourismImg" label="Product images">-->
<!--          <template #default="scope">-->
<!--            <el-image style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.tourismImg" :preview-src-list="[scope.row.img]" preview-teleported></el-image>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column prop="tourismPrice" label="The price of the commodity"></el-table-column>-->
<!--&lt;!&ndash;        <el-table-column prop="tourismId" label="Product ID"></el-table-column>&ndash;&gt;-->
<!--        <el-table-column prop="num" label="Quantity purchased"></el-table-column>-->
<!--        <el-table-column prop="total" label="The total price of the item">-->
<!--          <template #default="scope">-->
<!--            <span style="color: orangered; font-weight: bold">${{scope.row.total }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column prop="time" label="Time when the order was placed"  width="150" show-overflow-tooltip></el-table-column>-->
<!--&lt;!&ndash;        <el-table-column prop="payNo" label="Payment order number"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-table-column prop="payTime" label="Payment time"></el-table-column>&ndash;&gt;-->
<!--        <el-table-column prop="status" label="Order status">-->
<!--          <template #default="scope">-->
<!--            <el-tag type="danger" v-if="scope.row.status === '已取消'">Canceled</el-tag>-->
<!--            <el-tag type="primary" v-if="scope.row.status === '待支付'">To be paid</el-tag>-->
<!--            <el-tag type="primary" v-if="scope.row.status === '待发货'">To be shipped</el-tag>-->
<!--            <el-tag type="success" v-if="scope.row.status === '已完成'">Done</el-tag>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column prop="start" label="Start Date"></el-table-column>-->
<!--        <el-table-column label="operate" width="150px" fixed="right">-->
<!--          <template v-slot="scope">-->
<!--            <el-button type="primary" @click="changeStatus(scope.row)" v-if="scope.row.status === '待支付'">pay</el-button>-->
<!--            <el-button type="danger"  circle :icon="Delete" @click="del(scope.row.id)" v-if="scope.row.status === '待支付'"></el-button>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--      </el-table>-->
<!--    </div>-->
<!--    <div v-if="data.total">-->
<!--      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<template>
  <div style="width: 80%; margin: 10px auto; padding: 20px" class="card">
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.orderNo" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="Enter order number to search"></el-input>
      <el-button type="info" plain @click="load">Search</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">Reset</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
<!--      <el-button type="danger" plain @click="delBatch">Batch Delete</el-button>-->
      <el-button type="info" plain @click="handleExport">Export Excel</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" :selectable="checkSelectable" />
        <el-table-column prop="orderNo" label="Order Number" show-overflow-tooltip></el-table-column>
        <el-table-column prop="name" label="Product Name" show-overflow-tooltip></el-table-column>
        <el-table-column prop="tourismImg" label="Product Image" show-overflow-tooltip>
          <template #default="scope">
            <el-image style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.tourismImg" :preview-src-list="[scope.row.img]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="tourismPrice" label="Product Price" show-overflow-tooltip></el-table-column>
<!--        <el-table-column prop="tourismId" label="Product ID" show-overflow-tooltip></el-table-column>-->
        <el-table-column prop="num" label="Quantity Purchased" show-overflow-tooltip></el-table-column>
        <el-table-column prop="total" label="The total price of the item">
          <template #default="scope">
            <span style="color: orangered; font-weight: bold">${{scope.row.total }}</span>
          </template>
        </el-table-column>
<!--        <el-table-column prop="userName" label="Order Placed By" show-overflow-tooltip></el-table-column>-->
        <el-table-column prop="time" label="Order Time" show-overflow-tooltip></el-table-column>
<!--        <el-table-column prop="payNo" label="Payment Number" show-overflow-tooltip></el-table-column>-->
<!--        <el-table-column prop="payTime" label="Payment Time" show-overflow-tooltip></el-table-column>-->
        <el-table-column prop="status" label="Order Status" show-overflow-tooltip>
          <template #default="scope">
            <el-tag type="danger" v-if="scope.row.status === 'Cancelled'">Cancelled</el-tag>
            <el-tag type="primary" v-if="scope.row.status === 'Pending Payment'">Pending Payment</el-tag>
            <el-tag type="primary" v-if="scope.row.status === 'Pending Shipment'">Pending Shipment</el-tag>
            <el-tag type="success" v-if="scope.row.status === 'Completed'">Completed</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Operation" width="150" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" @click="changeStatus(scope.row)" v-if="scope.row.status === 'Pending Payment'">pay</el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)" v-if="scope.row.status === 'Pending Payment'"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="margin-bottom: 5px; display: flex">
      <el-button type="info" plain @click="payBatch">Pay</el-button>
      <div style="margin-left: 20px;margin-top: 5px">totalPrice:       {{data.totalPrice}}</div>
<!--      <span style="margin-left: 20px"></span>-->
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>

import {reactive, watch, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";


const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  title: null,
  ids: [],
  totalPrice: 0,
  rowids: []
})

const checkSelectable = (row) =>{
  return row.status === 'Pending Payment'
}

const changeStatus = (row) => {
  let form = JSON.parse(JSON.stringify(row))  // 防止表格数据被修改
  form.status = 'Completed'
  request.put('/orders/update', form).then(res => {
    if (res.code === '200') {
      ElMessage.success('Pay Success')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const load = () => {
  request.get('/orders/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      orderNo: data.orderNo
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('The data cannot be recovered after deletion, are you sure to delete it？', 'Delete Confirmation', { type: 'warning' }).then(res => {
    request.delete('/orders/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("delete success")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

watch(
    () => data.ids.length,
    (newLength, oldLength) => {
      data.totalPrice = 0;
      for (let i = 0; i<newLength; i++){
        data.totalPrice = data.totalPrice + data.rowids[i].total;
      }
    }
);

const handleExport = async () => {
  request.get('/orders/export',{
    responseType: 'blob'
  }).then(res => {
    const blob = new Blob([res], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'OrdersInformation.xlsx');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  })
};

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("Please select data")
    return
  }
  ElMessageBox.confirm('Once deleted, the data cannot be recovered. Are you sure you want to delete it?', 'Delete Confirmation', {
    confirmButtonText: 'confirm',
    cancelButtonText: 'cancel',
    type: 'warning' }).then(res => {
    request.delete("/orders/delete/batch", {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('Operation successful')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const payBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("Please select data")
    return
  }
  ElMessageBox.confirm('Are you sure you want to pay it?', 'Pay Confirmation', {
    confirmButtonText: 'confirm',
    cancelButtonText: 'cancel',
    type: 'warning' }).then(res => {
    request.put("/orders/pay/batch", data.ids).then(res => {
      if (res.code === '200') {
        ElMessage.success('Operation successful')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const handleSelectionChange = (rows) => {
  data.rowids = rows;
  data.ids = rows.map(v => v.id)
}

const reset = () => {
  data.orderNo = null
  load()
}

load()
</script>