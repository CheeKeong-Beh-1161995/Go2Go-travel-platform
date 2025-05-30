<template>
  <div style="padding: 20px 0">
    <div style="margin-bottom: 20px">
      <div style="font-size: 24px; font-weight: bold; margin-bottom: 10px">Comments {{ data.commentCount }}</div>
      <el-input style="margin-bottom: 5px" type="textarea" v-model="data.form.content" placeholder="Enter your comment"></el-input>
      <div style="text-align: right">
        <el-button type="primary" @click="addComment(null)">Comment</el-button>
      </div>
    </div>

    <div>
      <div v-for="item in data.commentList" :key="item.id">
        <div style="display: flex; align-items: flex-start; grid-gap: 20px; margin-bottom: 10px">
          <img :src="item.userAvatar" alt="" style="width: 50px; height: 50px; border-radius: 50%">
          <div style="flex: 1; width: 0">
            <div style="margin-bottom: 10px; color: #666">{{ item.userName }}</div>
            <div style="margin-bottom: 10px; text-align: justify">{{ item.content }}</div>
            <div style="margin-bottom: 10px; color: #666; font-size: 13px">
              <span>{{ item.time }}</span>
              <span style="margin: 0 20px; cursor: pointer" @click="handleReply(item)" :class="{ 'active-btn': item.showReply }">Reply</span>
              <span style="cursor: pointer" v-if="item.userId === data.user.id" @click="del(item.id)">Delete</span>
            </div>
            <div v-if="item.showReply">
              <el-input type="textarea" v-model="item.replyContent" placeholder="Enter your comment"></el-input>
              <div style="margin-top: 5px; text-align: right"><el-button type="primary" @click="addComment(item)">Save</el-button></div>
            </div>
          </div>
        </div>
        <!-- Secondary comments start -->
        <div style="padding-left: 70px" v-if="item.children?.length">
          <div v-for="subItem in item.children" :key="subItem.id">
            <div style="display: flex; align-items: flex-start; grid-gap: 20px; margin-bottom: 10px">
              <img :src="subItem.userAvatar" alt="" style="width: 50px; height: 50px; border-radius: 50%">
              <div style="flex: 1; width: 0">
                <div style="margin-bottom: 10px; color: #666">{{ subItem.userName }} <span v-if="subItem.parentUserName !== item.userName">Reply to @{{ subItem.parentUserName }}</span></div>
                <div style="margin-bottom: 10px; text-align: justify">{{ subItem.content }}</div>
                <div style="margin-bottom: 10px; color: #666; font-size: 13px">
                  <span>{{ subItem.time }}</span>
                  <span style="margin: 0 20px; cursor: pointer" @click="handleReply(subItem)" :class="{ 'active-btn': subItem.showReply }">Reply</span>
                  <span style="cursor: pointer" v-if="subItem.userId === data.user.id" @click="del(subItem.id)">Delete</span>
                </div>
                <div v-if="subItem.showReply">
                  <el-input type="textarea" v-model="subItem.replyContent" placeholder="Enter your comment"></el-input>
                  <div style="margin-top: 5px; text-align: right"><el-button type="primary" @click="addComment(subItem)">Save</el-button></div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- Secondary comments end -->
      </div>

      <div style="padding: 20px 0" v-if="data.total">
        <el-pagination size="small" @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import { ElMessage, ElMessageBox } from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  fid: router.currentRoute.value.query.id,
  commentCount: 0,
  form: {},
  commentList: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
});

const props = defineProps({
  module: String
});

const handleReply = (comment) => {
  comment.showReply = !comment.showReply;
};

const del = (id) => {
  ElMessageBox.confirm('This action cannot be undone. Are you sure?', 'Confirm Deletion', { type: 'warning' }).then(res => {
    request.delete('/comment/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("Deleted successfully");
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(err => {
    console.error(err);
  });
};

const load = () => {
    request.get('/comment/selectTree', {
      params: {
        pageNum: data.pageNum,
        pageSize: data.pageSize,
        fid: data.fid,
        module: props.module
      }
    }).then(res => {
      data.commentList = res.data?.list;
      data.total = res.data?.total;
    });
    request.get('/comment/selectCount/' + data.fid + '/' + props.module).then(res => {
      data.commentCount = res.data;
    });
};
load();

const addComment = (parentComment) => {
  if (!data.user.id){
    location.href = '/loginNav/login'
  }else {
    if (parentComment) {
      data.form.pid = parentComment.id;
      data.form.content = parentComment.replyContent;
    }
    if (!data.form.content) {
      ElMessage.warning('Please enter a comment');
      return;
    }
    data.form.fid = data.fid;
    data.form.module = props.module;
    request.post('/comment/add', data.form).then(res => {
      if (res.code === '200') {
        data.form = {};
        ElMessage.success('Comment posted successfully');
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }
};
</script>

<style>
.active-btn {
  color: #1890ff;
}
</style>