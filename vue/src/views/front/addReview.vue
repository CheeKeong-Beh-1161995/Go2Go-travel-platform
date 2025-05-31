<template>
  <div style="width: 50%; margin: 10px auto">
    <div style="text-align: right">
      <el-button @click="router.back()"> << return</el-button>
    </div>
    <div style="margin: 20px 0; text-align: center; font-size: 24px">Write a review</div>
    <div>
      <el-form ref="form" :model="data.form" label-width="190px" style="padding: 20px" :rules="data.rules">
        <el-form-item prop="title" label="title">
          <el-input v-model="data.form.title" placeholder="Please enter a title"></el-input>
        </el-form-item>
        <el-form-item prop="descr" label="Brief introduction">
          <el-input :rows="3" type="textarea" v-model="data.form.descr" placeholder="Please enter a brief introduction"></el-input>
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
import { i18nChangeLanguage } from '@wangeditor/editor'

i18nChangeLanguage('en')

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
  }
})
const baseUrl = import.meta.env.VITE_BASE_URL
const form = ref()

if (data.id) {
  request.get('/review/selectById/' + data.id).then(res => {
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
        ElMessage.warning('Please enter your content')
        return
      }
      request.post('/review/add', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('The release was successful')
          router.replace('/front/home_login')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const update = () => {
  form.value.validate((valid) => {
    if (valid) {  // 表单校验通过
      if (!data.form.content) {
        ElMessage.warning('Please enter your content')
        return
      }
      request.put('/review/update', data.form).then(res => {
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
const editorRef = shallowRef()  // Editor instance must use shallowRef
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
// 图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user.token,
  },
  server: baseUrl + '/files/wang/upload',  // Server-side image upload interface
  fieldName: 'file'  // Server-side image upload interface parameters
}
// When the component is destroyed, the editor should also be destroyed in a timely manner, otherwise it may cause memory leaks.
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
// Record the editor instance, important!
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5 initialization completed */
</script>