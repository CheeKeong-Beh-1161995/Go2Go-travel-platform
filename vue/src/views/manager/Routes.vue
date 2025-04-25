<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="Enter the name of the travel route to search"></el-input>
      <el-button type="info" plain @click="load">Search</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">Reset</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd">Add</el-button>
      <el-button type="danger" plain @click="delBatch">Batch Delete</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="Route Name" show-overflow-tooltip />
        <el-table-column prop="days" label="Number of Days" />
        <el-table-column prop="tips" label="Tips" show-overflow-tooltip />
        <el-table-column prop="img" label="Route Cover">
          <template #default="scope">
            <el-image :src="scope.row.img" style="width: 50px; height: 50px; border-radius: 5px" :preview-src-list="[scope.row.img]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="Route Content">
          <template #default="scope">
            <el-button @click="preview(scope.row.content)">View Content</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="Route Location" />
        <el-table-column prop="longitude" label="Longitude" />
        <el-table-column prop="latitude" label="Latitude" />
        <el-table-column label="Operation" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
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

    <el-dialog title="Travel Route Information" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item prop="name" label="Route Name">
          <el-input v-model="data.form.name" placeholder="Enter the name of the route"></el-input>
        </el-form-item>
        <el-form-item prop="days" label="Number of Days">
          <el-input v-model="data.form.days" placeholder="Enter the number of days"></el-input>
        </el-form-item>
        <el-form-item prop="tips" label="Tips">
          <el-input v-model="data.form.tips" placeholder="Enter tips for the route"></el-input>
        </el-form-item>
        <el-form-item prop="location" label="Route Location">
          <el-input v-model="data.form.location" placeholder="Enter the location of the route"></el-input>
        </el-form-item>
        <el-form-item prop="longitude" label="Longitude">
          <div style="width: 100%; display: flex; align-items: center">
            <el-input style="flex: 1" v-model="data.form.longitude" placeholder="Enter the longitude of the route"></el-input>
            <a style="color: #1890ff" href="https://api.map.baidu.com/lbsapi/getpoint/index.html" target="_blank">Check Longitude and Latitude</a>
          </div>
        </el-form-item>
        <el-form-item prop="latitude" label="Latitude">
          <el-input v-model="data.form.latitude" placeholder="Enter the latitude of the route"></el-input>
        </el-form-item>

        <el-form-item prop="cover" label="Route Image">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :headers="{ 'token': data.user.token }"
              :on-success= handleFileUpload
              list-type="picture"
          >
            <el-button type="primary">Upload</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="Route Content" prop="content">
          <div style="border: 1px solid #ccc; width: 100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 500px; overflow-y: hidden;"
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">Cancel</el-button>
          <el-button type="primary" @click="save">Confirm</el-button>
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
  name: null,
  ids: [],
  content: null,
  formVisibleContent: null
})

const preview = (content) => {
  data.content = content
  data.formVisibleContent = true
}

const baseUrl = import.meta.env.VITE_BASE_URL
const handleFileUpload = (res) => {
  data.form.img = res.data
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
  request.get('/routes/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
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
  request.post('/routes/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('Operation successful')
      data.formVisible = false
      load()} else {
      ElMessage.error(res.msg)
    }
  })
}
const update = () => {
  request.put('/routes/update', data.form).then(res => {
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
    request.delete('/routes/delete/' + id).then(res => {
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
    request.delete("/routes/delete/batch", {data: data.ids}).then(res => {
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
  data.name = null
  load()
}
load()
</script>