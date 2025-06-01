<template>
  <div style="width: 50%; margin: 10px auto">
    <div style="text-align: right">
      <el-button @click="router.back()"> << return</el-button>
    </div>
    <div style="margin: 20px 0; text-align: center; font-size: 24px">Write a Review</div>
    <div>
      <el-form ref="form" :model="data.form" label-width="190px" style="padding: 20px" :rules="data.rules">
        <el-form-item prop="title" label="Title">
          <el-input v-model="data.form.title" placeholder="Please enter the title"></el-input>
        </el-form-item>
        <el-form-item label="Cover">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :headers="{ 'token': data.user.token }"
              list-type="picture"
              :on-success="handleFileUpload"
          >
            <el-button type="primary">Upload Image</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="descr" label="Description">
          <el-input :rows="3" type="textarea" v-model="data.form.descr" placeholder="Please enter the description"></el-input>
        </el-form-item>
        <el-form-item prop="startDate" label="Travel Date">
          <el-date-picker style="width: 100%" type="datetime" v-model="data.form.startDate" format="YYYY-MM-DD HH:mm" value-format="YYYY-MM-DD HH:mm" placeholder="Please select the travel date"></el-date-picker>
        </el-form-item>
        <el-form-item label="Cost" prop="money">
          <el-input-number style="width: 200px" v-model="data.form.money" placeholder="Please enter the cost" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item prop="location" label="Travel Location">
          <el-input v-model="data.form.location" placeholder="Please enter the travel location"></el-input>
        </el-form-item>
        <el-form-item prop="days" label="Travel Days">
          <el-input-number style="width: 200px" v-model="data.form.days" placeholder="Please enter the travel days"></el-input-number>
        </el-form-item>
        <el-form-item label="Content" prop="content">
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
          <el-button type="primary" style="padding: 20px 40px" @click="save">Publish Review</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import { reactive } from "vue";
import '@wangeditor/editor/dist/css/style.css' // Introduce CSS
import {onBeforeUnmount, ref, shallowRef} from "vue";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import { i18nChangeLanguage } from '@wangeditor/editor'

i18nChangeLanguage('en')

const data = reactive({
  id: router.currentRoute.value.query.id,
  user: JSON.parse(localStorage.getItem('xm-user')),
  form: {},
  rules: {
    title: [
      { required: true, message: 'Please enter the title', trigger: 'blur' }
    ],
    descr: [
      { required: true, message: 'Please enter the description', trigger: 'blur' }
    ],
    startDate: [
      { required: true, message: 'Please select the travel date', trigger: 'blur' }
    ],
    money: [
      { required: true, message: 'Please enter the cost', trigger: 'blur' }
    ],
    location: [
      { required: true, message: 'Please enter the travel location', trigger: 'blur' }
    ],
    days: [
      { required: true, message: 'Please enter the travel days', trigger: 'blur' }
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
    if (valid) {  // 表单校验通过
      if (!data.form.content) {
        ElMessage.warning('Please enter the content')
        return
      }
      request.post('/travels/add', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('Published successfully')
          router.push('/front/article')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const update = () => {
  form.value.validate((valid) => {
    if (valid) {
      if (!data.form.content) {
        ElMessage.warning('Please enter the content')
        return
      }
      request.put('/travels/update', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('Published successfully')
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


const editorRef = shallowRef()
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
// 图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user.token,
  },
  server: baseUrl + '/files/wang/upload',
  fieldName: 'file'
}

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor
}

</script>