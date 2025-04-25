<template>
  <div style="width: 50%; margin: 10px auto">
    <div style="text-align: right">
      <el-button @click="router.back()"> << return</el-button>
    </div>
    <div style="margin: 20px 0; text-align: center; font-size: 24px">Write a review</div>
    <div>
      <el-form ref="form" :model="data.form" label-width="90px" style="padding: 20px" :rules="data.rules">
        <el-form-item prop="title" label="title">
          <el-input v-model="data.form.title" placeholder="Please enter a title"></el-input>
        </el-form-item>
        <el-form-item prop="descr" label="Brief introduction">
          <el-input :rows="3" type="textarea" v-model="data.form.descr" placeholder="Please enter a brief introduction"></el-input>
        </el-form-item>
        <el-form-item prop="startDate" label="Date of travel">
          <el-date-picker style="width: 100%" type="datetime" v-model="data.form.startDate" format="YYYY-MM-DD HH:mm" value-format="YYYY-MM-DD HH:mm" placeholder="Please select a travel date"></el-date-picker>
        </el-form-item>
        <el-form-item label="expenses" prop="money">
          <el-input-number style="width: 200px" v-model="data.form.money" placeholder="Please enter a fee" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item prop="location" label="Where to go">
          <el-input v-model="data.form.location" placeholder="Please enter your travel location"></el-input>
        </el-form-item>
        <el-form-item prop="days" label="Number of days traveled">
          <el-input-number style="width: 200px" v-model="data.form.days" placeholder="Please enter the number of days you are traveling"></el-input-number>
        </el-form-item>
        <el-form-item label="content" prop="content">
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
        <div style="text-align: center">
          <el-button type="primary" style="padding: 20px 40px" @click="save">Post a review</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import { reactive } from "vue";
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {onBeforeUnmount, ref, shallowRef} from "vue";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  id: router.currentRoute.value.query.id,
  user: JSON.parse(localStorage.getItem('xm-user')),
  form: {},
  rules: {
    title: [
      { required: true, message: 'Please enter a title', trigger: 'blur' }
    ],
    descr: [
      { required: true, message: 'Please enter a brief introduction', trigger: 'blur' }
    ],
    startDate: [
      { required: true, message: 'Please select a travel date', trigger: 'blur' }
    ],
    money: [
      { required: true, message: 'Please enter the amount you will spend', trigger: 'blur' }
    ],
    location: [
      { required: true, message: 'Please enter your travel location', trigger: 'blur' }
    ],
    days: [
      { required: true, message: 'Please enter the number of days you are traveling', trigger: 'blur' }
    ],
  }
})
const baseUrl = import.meta.env.VITE_BASE_URL
const form = ref()

if (data.id) {
  request.get('/travels/selectById/' + data.id).then(res => {
    data.form = res.data
  })
}

const save = () => {
  if (data.id) {
    update()
  } else {
    add()
  }
}

const add = () => {
  form.value.validate((valid) => {
    if (valid) {  // Form validation passed
      if (!data.form.content) {
        ElMessage.warning('Please enter your content')
        return
      }
      request.post('/travels/add', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('The release was successful')
          if (res.data.id) {
            router.replace('/front/addTravel?id=' + res.data.id)
          }
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const update = () => {
  form.value.validate((valid) => {
    if (valid) {  // Form validation passed
      if (!data.form.content) {
        ElMessage.warning('Please enter your content')
        return
      }
      request.put('/travels/update', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('The release was successful')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const handleFileUpload = (res) => {
  data.form.cover = res.data
}

/* wangEditor5 initialization starts */
const editorRef = shallowRef()  // Use shallowRef
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
// Image upload
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user.token,
  },
  server: baseUrl + '/files/wang/upload',  // Server image upload API
  fieldName: 'file'  // Parameter for server image upload API
}
// Ensure the editor is delete when the component is unmounted to prevent memory leaks
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
// Record editor
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5 initialization ends */
</script>