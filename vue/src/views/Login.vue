<template>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #147300">Welcome to Login</div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="Enter your username"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input :prefix-icon="User" size="large" v-model="data.form.email" placeholder="Enter your email"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="Enter your password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="login">Login</el-button>
        </el-form-item>
        <div style="display: flex">
          <a style="color: #189500" href="/loginNav/forgetPassword">Forgot Password</a>
          <div style="flex: 1; text-align: right">Don't have an account? Please <a style="color: #189500" href="/loginNav/register">Register</a></div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  form: { role: 'USER' },
  rules: {
    username: [
      { required: true, message: 'Enter your username', transform: value => value, trigger: 'blur' },
      {
        type: 'string',
        message: 'Enter characters without spaces',
        trigger: 'blur',
        transform(value) {
          if (value && value.indexOf(' ') === -1) {
            return value
          } else {
            return false
          }
        }
      },
      {
        trigger: 'blur',
        validator: (rule, value, callback) => {
          var usernamereg = /^[a-zA-Z0-9_-]{8,16}$/;
          if (!usernamereg.test(value)) {
            callback(new Error('The username must be a combination of numbers and letters, please enter 8-16 characters'))
          }else{
            callback()
          }
        } }
    ],
    password: [
      { required: true, message: 'Enter your password', transform: value => value, trigger: 'blur' },
      {
        type: 'string',
        message: 'Enter characters without spaces',
        trigger: 'blur',
        transform(value) {
          if (value && value.indexOf(' ') === -1) {
            return value
          } else {
            return false
          }
        }
      },
      {
        trigger: 'blur',
        validator: (rule, value, callback) => {
          var passwordreg = /(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,20}/
          if (!passwordreg.test(value)) {
            callback(new Error('The password must be a combination of numbers, letters, and special characters, please enter 8-20 characters'))
          }else{
            callback()
          }
        }
      }
    ],
    email: [
      { required: true, message: 'Enter your email', trigger: 'blur' },
      {
        trigger: 'blur',
        validator: (rule, value, callback) => {
          var emailreg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
          if (!emailreg.test(value)) {
            callback(new Error('Invalid email format'))
          }else {
            callback()
          }
        }
      }
    ]
  }
})

const formRef = ref()

const login = () => {
  formRef.value.validate(valid => {
    if (valid) { // Indicates that the form validation has passed
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('Login successful')
          // Store user information in the browser's cache
          localStorage.setItem('xm-user', JSON.stringify(res.data))
          router.push('/front/home_login')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 93vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: 100% 100%;
}
.login-box {
  width: 350px;
  padding: 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.5);
}
</style>