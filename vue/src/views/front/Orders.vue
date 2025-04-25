<template>
  <div style="width: 80%; margin: 10px auto; padding: 20px" class="card">
    <div style="margin-bottom: 10px">
      <el-input v-model="data.orderNo" prefix-icon="Search" style="width: 280px; margin-right: 10px" placeholder="Please enter the order number to check"></el-input>
      <el-button type="info" plain @click="load">Inquire</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">reset</el-button>
    </div>

    <div style="margin-bottom: 10px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column prop="orderNo" label="Order number" width="150"></el-table-column>
        <el-table-column prop="name" label="The title of the product" show-overflow-tooltip></el-table-column>
        <el-table-column prop="tourismImg" label="Product images">
          <template #default="scope">
            <el-image style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.tourismImg" :preview-src-list="[scope.row.img]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="tourismPrice" label="The price of the commodity"></el-table-column>
        <el-table-column prop="tourismId" label="Product ID"></el-table-column>
        <el-table-column prop="num" label="Quantity purchased"></el-table-column>
        <el-table-column prop="total" label="The total price of the item">
          <template #default="scope">
            <span style="color: orangered; font-weight: bold">￥{{scope.row.total }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="Time when the order was placed"  width="150"></el-table-column>
        <el-table-column prop="payNo" label="Payment order number"></el-table-column>
        <el-table-column prop="payTime" label="Payment time"></el-table-column>
        <el-table-column prop="status" label="Order status">
          <template #default="scope">
            <el-tag type="danger" v-if="scope.row.status === '已取消'">Canceled</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '待支付'">To be paid</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '待发货'">To be shipped</el-tag>
            <el-tag type="success" v-if="scope.row.status === '已完成'">Done</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" @click="changeStatus(scope.row)" v-if="scope.row.status === '待支付'">pay</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>

import {reactive} from "vue";
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
  ids: []
})

const changeStatus = (row) => {
  let form = JSON.parse(JSON.stringify(row))  // 防止表格数据被修改
  form.status = '已完成'
  request.put('/orders/update', form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
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
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/orders/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
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
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete("/orders/delete/batch", {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
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