<template>
  <div style="width: 50%" class="card">
    <el-form ref="formRef" :rules="data.rules" :model="data.user" label-width="80px" style="padding: 20px">
      <el-form-item label="Original Password" prop="password">
        <el-input v-model="data.user.password" placeholder="Please enter the original password" show-password></el-input>
      </el-form-item>
      <el-form-item label="New Password" prop="newPassword">
        <el-input v-model="data.user.newPassword" placeholder="Please enter the new password" show-password></el-input>
      </el-form-item>
      <el-form-item label="Confirm Password" prop="confirmPassword">
        <el-input v-model="data.user.confirmPassword" placeholder="Please confirm the new password" show-password></el-input>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" @click="updatePassword">Save</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

const formRef = ref();

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('Please confirm the password'));
  } else {
    if (value !== data.user.newPassword) {
      callback(new Error("The confirmation password does not match the new password!"));
    }
    callback();
  }
};

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  rules: {
    password: [
      { required: true, message: 'Please enter the original password', trigger: 'blur' },
    ],
    newPassword: [
      { required: true, message: 'Please enter the new password', trigger: 'blur' },
    ],
    confirmPassword: [
      { validator: validatePass, trigger: 'blur' }
    ]
  }
});

const updatePassword = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.put('/updatePassword', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success('Saved successfully');
          logout();
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

const logout = () => {
  localStorage.removeItem('xm-user');
  router.push('/loginNav/login');
};
</script>

<style scoped>
</style>