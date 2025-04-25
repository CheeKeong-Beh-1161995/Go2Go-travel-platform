<template>
  <div style="width: 50%" class="card">
    <el-form ref="formRef" :rules="data.rules" :model="data.user" label-width="80px" style="padding: 20px">
      <el-form-item label="UserName" prop="username">
        <el-input v-model="data.user.username" placeholder="Please enter your username"></el-input>
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
  user: {},
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
    newPassword: [
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
    confirmPassword: [
      { validator: validatePass, trigger: 'blur' }
    ]
  }
});

const updatePassword = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.put('/forgetPassword', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success('Saved successfully');
          router.push('/loginNav/login');
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

</script>

<style scoped>
</style>