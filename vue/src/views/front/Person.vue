<template>
  <div style="width: 40%; margin: 5px auto" class="card">
    <el-form ref="user" :model="data.user" :rules="data.rules" label-width="60px" style="padding: 20px">
      <div style="text-align: center; margin-bottom: 20px">
        <el-upload
            :action="baseUrl + '/files/upload'"
            :on-success="handleFileUpload"
            :show-file-list="false"
            class="avatar-uploader"
        >
          <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </div>
      <el-form-item prop="username" label="Username">
        <el-input disabled v-model="data.user.username" placeholder="Enter username"></el-input>
      </el-form-item>
      <el-form-item prop="name" label="Name">
        <el-input v-model="data.user.name" placeholder="Enter name"></el-input>
      </el-form-item>
      <el-form-item prop="phone" label="Phone">
        <el-input v-model="data.user.phone" placeholder="Enter phone number"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="Email">
        <el-input v-model="data.user.email" placeholder="Enter email"></el-input>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" @click="update">Save</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";

const baseUrl = import.meta.env.VITE_BASE_URL;

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
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
          var phonereg = /^\d{7,20}$/
          if (!phonereg.test(value)) {
            callback(new Error('Invalid phone number format'))
          }else {
            callback()
          }
        }
      }
    ],

  }
});

const handleFileUpload = (res) => {
  data.user.avatar = res.data;
};

const emit = defineEmits(['updateUser']);
const update = () => {
  request.put('/user/update', data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('Saved successfully');
      localStorage.setItem('xm-user', JSON.stringify(data.user));
      emit('updateUser');
    } else {
      ElMessage.error(res.msg);
    }
  });
};
</script>

<style>
.avatar-uploader {
  height: 120px;
}
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>