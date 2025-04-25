<template>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #1450aa">Welcome to Register</div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="Enter your username"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input :prefix-icon="User" size="large" v-model="data.form.email" placeholder="Enter your email"></el-input>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input :prefix-icon="User" size="large" v-model="data.form.phone" placeholder="Enter your phone number"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="Enter your password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.confirmPassword" placeholder="Confirm your password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="register">Register</el-button>
        </el-form-item>
        <div style="text-align: right">
          Already have an account? Please <a href="/loginNav/login">Log in</a>
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

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('Please confirm your password'))
  } else {
    if (value !== data.form.password) {
      callback(new Error("The confirmed password does not match the original password!"))
    }
    callback()
  }
}
const data = reactive({
  form: { role: 'USER'},
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
    ],
    phone: [
      { required: true, message: 'Enter your phone number', trigger: 'blur' },
      {
        trigger: 'blur',
        validator: (rule, value, callback) => {
          var phonereg = /^\d{1,21}$/
          if (!phonereg.test(value)) {
            callback(new Error('Invalid phone number format'))
          }else {
            callback()
          }
        }
      }
    ],
    confirmPassword: [
      { validator: validatePass, trigger: 'blur' }
    ]
  }
})

const formRef = ref()

const register = () => {
  formRef.value.validate(valid => {
    if (valid) { // Indicates that the form validation has passed
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('Registration successful')
          router.push('/loginNav/login')
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
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to top, #00467f, #a5cc82);
}
.login-box {
  width: 350px;
  padding: 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.5);
}
</style>