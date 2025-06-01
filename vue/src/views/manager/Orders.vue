<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.orderNo" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="Enter order number to search"></el-input>
      <el-button type="info" plain @click="load">Search</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">Reset</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="danger" plain @click="delBatch">Batch Delete</el-button>
      <el-button type="info" plain @click="handleExport">Export Excel</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="orderNo" label="Order Number" show-overflow-tooltip></el-table-column>
        <el-table-column prop="name" label="Product Name" show-overflow-tooltip></el-table-column>
        <el-table-column prop="tourismImg" label="Product Image" show-overflow-tooltip></el-table-column>
        <el-table-column prop="tourismPrice" label="Product Price" show-overflow-tooltip></el-table-column>
        <el-table-column prop="tourismId" label="Product ID" show-overflow-tooltip></el-table-column>
        <el-table-column prop="num" label="Quantity Purchased" show-overflow-tooltip></el-table-column>
        <el-table-column prop="total" label="Total Price" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userName" label="Order Placed By" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="Order Time" show-overflow-tooltip></el-table-column>
        <el-table-column prop="payNo" label="Payment Number" show-overflow-tooltip></el-table-column>
        <el-table-column prop="payTime" label="Payment Time" show-overflow-tooltip></el-table-column>
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
            <el-button type="primary" @click="changeStatus(scope.row)" v-if="scope.row.status === 'Pending Payment'">Approve</el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

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

const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  title: null,
  ids: []
})

const changeStatus = (row) => {
  let form = JSON.parse(JSON.stringify(row))  // Prevent table data from being modified
  form.status = 'Completed'
  request.put('/orders/update', form).then(res => {
    if (res.code === '200') {
      ElMessage.success('Operation successful')
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

const update = () => {
  request.put('/orders/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('Operation successful')
      data.formVisible = false
      load()
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('Once deleted, the data cannot be recovered. Are you sure you want to delete it?', 'Delete Confirmation', {
    confirmButtonText: 'confirm',
    cancelButtonText: 'cancel',
    type: 'warning' }).then(res => {
    request.delete('/orders/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("Deleted successfully")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}
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
const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const reset = () => {
  data.orderNo = null
  load()
}

load()
</script>