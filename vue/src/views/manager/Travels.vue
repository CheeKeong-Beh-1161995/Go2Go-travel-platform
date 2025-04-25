<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.title" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="Enter title to search"></el-input>
      <el-button type="info" plain @click="load">Search</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">Reset</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="danger" plain @click="delBatch">Batch Delete</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="title" label="Title" show-overflow-tooltip />
        <el-table-column prop="descr" label="Description" show-overflow-tooltip />
        <el-table-column prop="cover" label="Cover">
          <template #default="scope">
            <el-image :src="scope.row.cover" style="width: 50px; height: 50px; border-radius: 5px" :preview-src-list="[scope.row.cover]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="Content" width="100px">
          <template #default="scope">
            <el-button @click="preview(scope.row.content)">View Content</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="Publisher" />
        <el-table-column prop="time" label="Publish Time" />
        <el-table-column prop="readCount" label="Read Count" />
        <el-table-column prop="startDate" label="Travel Date" />
        <el-table-column prop="money" label="Cost" />
        <el-table-column prop="days" label="Duration" />
        <el-table-column prop="location" label="Location" />
        <el-table-column label="Review Status">
          <template #default="scope">
            <el-tag type="success" v-if="scope.row.status === '通过'">Approved</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '拒绝'">Rejected</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Operation" width="180" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" size="small" @click="changeStatus(scope.row, '通过')" v-if="scope.row.status === '待审核'">Approve</el-button>
            <el-button type="danger" size="small" @click="changeStatus(scope.row, '拒绝')" v-if="scope.row.status === '待审核'">Reject</el-button>
<!--            <el-button type="primary" circle :icon="Edit" @click="update(scope.row.id)"></el-button>-->
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="Content Preview" v-model="data.formVisibleContent" width="50%" destroy-on-close>
      <div style="padding: 20px">
        <div v-html="data.content"></div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisibleContent = false">Close</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {onBeforeUnmount, ref, shallowRef} from "vue";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@/assets/css/wangeditor.css'

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user')),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  title: null,
  ids: [],
  content: null,
  formVisibleContent: false
})

const changeStatus = (row, status) => {
  row.status = status
  request.put('/travels/update', row).then(res => {
    if (res.code === '200') {
      ElMessage.success('Operation successful')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const preview = (content) => {
  data.content = content
  data.formVisibleContent = true
}

const baseUrl = import.meta.env.VITE_BASE_URL
const handleFileUpload = (res) => {
  data.form.cover = res.data
}

/* wangEditor5 Initialization Start */
const editorRef = shallowRef()  // Editor instance, must use shallowRef
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
// Image upload configuration
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user.token,
  },
  server: baseUrl + '/files/wang/upload',  // Server image upload interface
  fieldName: 'file'  // Server image upload interface parameter
}
// When the component is destroyed, also destroy the editor in time, otherwise it may cause memory leaks
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
// Record editor instance, important!
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5 Initialization End */

const load = () => {
  request.get('/travels/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    }
  })
}

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const add = () => {
  request.post('/travels/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('Operation successful')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/travels/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('Operation successful')
      data.formVisible = false
      load()
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('Once deleted, the data cannot be recovered. Are you sure you want to delete it?', 'Delete Confirmation', { type: 'warning' }).then(res => {
    request.delete('/travels/delete/' + id).then(res => {
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
  ElMessageBox.confirm('Once deleted, the data cannot be recovered. Are you sure you want to delete it?', 'Delete Confirmation', { type: 'warning' }).then(res => {
    request.delete("/travels/delete/batch", {data: data.ids}).then(res => {
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
  data.title = null
  load()
}

load()
</script>