<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="Enter name to search"></el-input>
      <el-button type="info" plain @click="load">Search</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">Reset</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd">Add</el-button>
      <el-button type="danger" plain @click="delBatch">Batch Delete</el-button>
      <el-button type="info" plain @click="handleExport">Export Excel</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="Name" show-overflow-tooltip></el-table-column>
        <el-table-column prop="img" label="Image">
          <template #default="scope">
            <el-image style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.img" :preview-src-list="[scope.row.img]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="descr" label="Description" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="Details">
          <template #default="scope">
            <el-button type="primary" @click="view(scope.row.content)">View Content</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="Price"></el-table-column>
        <el-table-column prop="store" label="Stock"></el-table-column>
        <el-table-column prop="discount" label="Discount" :formatter="ChangeToPercentage"></el-table-column>
        <el-table-column prop="specials" label="Features" show-overflow-tooltip>
          <template #default="scope">
            <el-tag v-for="item in scope.row.specials" :key="item" style="margin-right: 5px; margin-bottom: 5px">{{ item }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="date" label="Release Date"></el-table-column>
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

    <el-dialog title="Travel Product" v-model="data.formVisible" width="50%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item label="Name" prop="name">
          <el-input type="textarea" v-model="data.form.name" placeholder="Name"></el-input>
        </el-form-item>
        <el-form-item label="Image" prop="img">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :headers="{ 'token': data.user.token }"
              :on-success="handleFileUpload"
              list-type="picture"
          >
            <el-button type="primary">Upload</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="Description" prop="descr">
          <el-input type="textarea" :rows="3" v-model="data.form.descr" placeholder="Description"></el-input>
        </el-form-item>
        <el-form-item label="Price" prop="price">
          <el-input-number style="width: 200px" :min="0" v-model="data.form.price" placeholder="Price"></el-input-number>
        </el-form-item>
        <el-form-item label="Stock" prop="store">
          <el-input-number style="width: 200px" :min="1" v-model="data.form.store" placeholder="Stock"></el-input-number>
        </el-form-item>
        <el-form-item label="Discount" prop="discount">
          <el-input-number style="width: 200px" :min="0.1" :max="1" :step="0.1" v-model="data.form.discount" placeholder="Discount"></el-input-number>
        </el-form-item>
        <el-form-item label="Features" prop="specials">
          <el-select multiple v-model="data.form.specials">
            <el-option value="SUV"></el-option>
            <el-option value="Comfortable"></el-option>
            <el-option value="Off-road"></el-option>
            <el-option value="A-Class Sedan"></el-option>
            <el-option value="RV"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Details" prop="content">
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

    <el-dialog title="Content" v-model="data.viewVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div class="view" style="padding: 20px" v-html="data.content"></div>
      <template #footer>
    <span class="dialog-footer">
      <el-button @click="data.viewVisible = false">Close</el-button>
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
import { i18nChangeLanguage } from '@wangeditor/editor'


const handleExport = async () => {
  request.get('/car/export',{
    responseType: 'blob'
  }).then(res => {
    const blob = new Blob([res], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'CarInformation.xlsx');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  })
};

const ChangeToPercentage = (row, column) => {
  return (row[column.property]*100)+"%"
}

i18nChangeLanguage('en')

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  ids: [],
  viewVisible: false,
  content: null
})


const view = (content) => {
  data.content = content
  data.viewVisible = true
}

const baseUrl = import.meta.env.VITE_BASE_URL
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

const handleFileUpload = (res) => {
  data.form.img = res.data
}

const load = () => {
  request.get('/car/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
      data.tableData.forEach(item => {
        item.specials = JSON.parse(item.specials ||  '[]')  // Convert the string from the database to an array for rendering in the dropdown
      })
    }
  })
}
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}
const handleEdit = (row) =>{
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const add = () => {
  data.form.specials = JSON.stringify(data.form.specials)  // Convert array to string
  request.post('/car/add', data.form).then(res => {
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
  data.form.specials = JSON.stringify(data.form.specials)
  request.put('/car/update', data.form).then(res => {
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
  ElMessageBox.confirm('Once deleted, the data cannot be recovered. Are you sure you want to delete it?', 'Delete Confirmation', {
    confirmButtonText: 'confirm',
    cancelButtonText: 'cancel',
    type: 'warning' }).then(res => {
    request.delete('/car/delete/' + id).then(res => {
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
    request.delete("/car/delete/batch", {data: data.ids}).then(res => {
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