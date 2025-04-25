<template>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #147300">Welcome Login</div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="Enter your account"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="Enter your password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="login">Login</el-button>
        </el-form-item>
        <div style="display: flex">
          <a style="color: #189500" href="/loginNav/adminLogin">Admin Login</a>
          <div style="flex: 1; text-align: right"> <a style="color: #189500" href="/loginNav/forgetPassword">Forgot Password</a></div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  form: { role: 'ADMIN' },
  rules: {
    username: [
      { required: true, message: 'Enter your account', trigger: 'blur' }
    ],
    password: [
      { required: true, message: 'Enter your password', trigger: 'blur' }
    ]
  }
});

const formRef = ref();

const login = () => {
  formRef.value.validate(valid => {
    if (valid) { // Indicates that the form validation has passed
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('Login successful');
          // Store user information in the browser's cache
          localStorage.setItem('xm-user', JSON.stringify(res.data));
          router.push('/manager/home');
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};
</script>

<style scoped>
.login-container {
  height: 93vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #0066bc;
}
.login-box {
  width: 350px;
  padding: 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.5);
}
</style>