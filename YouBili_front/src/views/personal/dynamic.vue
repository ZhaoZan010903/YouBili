<!--动态-->
<template>

  <div>
    <!--最上面那个字-->
    <h1 style="color:hotpink; margin-left: 5%">动态</h1>
    <!--那根线-->
    <el-divider></el-divider>
    <!-- 显示数据列表 -->
    <el-row :gutter="100">
      <el-col :span="12">
        <div class="grid-content" style=" border: 1.5px solid #dbdbdb;border-radius: 5px; margin-top: 10px;"
             v-for="(v, i) of itemArr" :key="i">
          <!--          <router-link to="/videoIndex">-->
          <div class="flex-col page space-y-7">
            <!--头像  昵称  发布时间-->
            <div class="flex-row space-x-22">
              <img
                  class="image"
                  :src="v.imgUrl"
              />
              <div class="flex-col self-start space-y-10">
                <span class="self-start text">{{ v.nickname }}</span>
                <div class="flex-row items-baseline">
                  <span class="font_1">{{ v.uploadDate }}</span>
                  <span class="font_1">投稿了视频</span>
                </div>
              </div>
            </div>
            <div class="flex-col group space-y-15" @click="jump(v.videoID)">
              <!--卡片-->
              <div style="border: 1.5px solid #dbdbdb; border-radius: 5px; overflow: hidden;" class="flex-row section">
                <div class="flex-col justify-start items-end shrink-0 text-wrapper">
                  <div style="flex: 1;">
                    <img :src="v.videoImgUrl" style="width: 100%; height: 100%; object-fit: cover;">
                  </div>
                  <span class="font_2 text_3">{{ v.duration }}</span>
                </div>
                <div class="flex-col flex-auto section_2 space-y-60">
                  <span class="self-start font_1 text_2">{{ v.title }}</span>
                  <!--                    <span class="font_2">222222222222{{ v.videodetail }}</span> &lt;!&ndash; 添加视频详情的标签 &ndash;&gt;-->
                  <div class="flex-row justify-center relative group_2">
                    <div class="flex-row space-x-5 pos">
                      <img class="shrink-0 image_2"
                           src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/64aa303e92373d00124b78f8/16888751024617278190.png"/>
                      <span class="font_2">8888{{ v.playBackNum }}</span>
                    </div>
                    <div class="flex-row space-x-5">
                      <img class="shrink-0 image_3"
                           src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/64aa303e92373d00124b78f8/16888751024617615778.png"/>
                      <span class="font_2">00{{ v.commentNum }}</span>
                    </div>
                  </div>
                </div>
              </div>
              <!--转发评论点赞-->
              <div class="flex-row self-start space-x-130">
                <div class="flex-row items-center space-x-6">
                  <img
                      class="shrink-0 image_4"
                      src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/64aa303e92373d00124b78f8/16888751024576627525.png"
                  />
                  <span class="font_2">转发</span>
                </div>
                <div class="flex-row items-center space-x-6">
                  <img
                      class="shrink-0 image_4"
                      src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/64aa303e92373d00124b78f8/16888751024599264891.png"
                  />
                  <span class="font_2">00{{ v.commentNum }}</span>
                </div>
                <div class="flex-row items-center space-x-6">
                  <img
                      class="shrink-0 image_4"
                      src="https://codefun-proj-user-res-1256085488.cos.ap-guangzhou.myqcloud.com/64aa303e92373d00124b78f8/16888751024609330995.png"
                  />
                  <span class="font_2">{{ v.likeNum }}</span>
                </div>
              </div>
            </div>
          </div>
          <!--          </router-link>-->
        </div>
      </el-col>
    </el-row>
  </div>


</template>


<script>

import {getDyNamic} from "@/utils/option";

export default {
  name: "dynamic",
  components: {},
  data() {
    return {
      count: 0,//无限滚动的初始数值
      circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
      squareUrl: "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png",
      itemArr: [
        {
          imgUrl: "",
          nickname: "",
          uploadDate: "",
          videodetail: "",
          videoImgUrl: "",
          title: "",
          commentNum: "2",
          likeNum: "2",
          playBackNum: "10",
          duration: "8:02"

        }]
    }

  },
  methods: {
    jump(data) {
      console.log(data)
      this.$router.push({path: "/videoIndex", query: {videoID: data}})
    },
  },
  created() {
    getDyNamic().then(responce => {
      if (responce.status) {
        this.itemArr = responce.data
        console.log(this.itemArr);
      }
    })
  }
}
</script>

<style scoped>
.page {
  padding: 18px;
  background-color: #ffffff;
  width: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  height: 100%;
}

.space-y-7 > *:not(:first-child) {
  margin-top: 7px;
}

.space-x-22 > *:not(:first-child) {
  margin-left: 22px;
}

.image {
  border-radius: 50%;
  width: 48px;
  height: 48px;
}

.space-y-10 > *:not(:first-child) {
  margin-top: 10px;
}

.text {
  color: #ff6699;
  font-size: 17px;
  font-family: Microsoft YaHei;
  font-weight: 700;
  line-height: 18px;
}

.font_1 {
  font-size: 13px;
  font-family: Microsoft YaHei;
  line-height: 14px;
  font-weight: 700;
  color: #9499a0;
}

.group {
  padding-left: 70px;
}

.space-y-15 > *:not(:first-child) {
  margin-top: 15px;
}

.section {
  background-size: 100% 100%;
  background-repeat: no-repeat;
}

.text-wrapper {
  /*padding: 90px 0 12px;*/
  width: 200px;
  height: 113px;
}

.font_2 {
  font-size: 13px;
  font-family: Microsoft YaHei;
  line-height: 11px;
  font-weight: 700;
  color: #9499a0;
}

.text_3 {
  margin-right: 5px;
  color: #ffffff;
  font-size: 14px;
  line-height: 12px;
}

.section_2 {
  padding: 12px 11px;
  background-color: #ffffff;
  background-size: 100% 100%;
  background-repeat: no-repeat;
  height: 113px;
}

.space-y-60 > *:not(:first-child) {
  margin-top: 60px;
}

.text_2 {
  color: #18191c;
  font-size: 14px;
  line-height: 15px;
}

.text_4 {
  color: #505050;
  font-size: 14px;
  line-height: 15px;
}

.group_2 {
  padding: 0 6px;
}

.space-x-5 > *:not(:first-child) {
  margin-left: 5px;
}

.pos {
  position: absolute;
  left: 6px;
  top: 50%;
  transform: translateY(-50%);
}

.image_2 {
  width: 13px;
  height: 11px;
}

.image_3 {
  width: 14px;
  height: 12px;
}

.space-x-130 > *:not(:first-child) {
  margin-left: 130px;
}

.space-x-6 > *:not(:first-child) {
  margin-left: 6px;
}

.image_4 {
  width: 14.5px;
  height: 14.5px;
}

/************************************************************
** 请将全局样式拷贝到项目的全局 CSS 文件或者当前页面的顶部 **
** 否则页面将无法正常显示                                  **
************************************************************/

html {
  font-size: 16px;
}

body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', 'Fira Sans',
  'Droid Sans', 'Helvetica Neue', 'Microsoft Yahei', sans-serif;
}

body * {
  box-sizing: border-box;
  flex-shrink: 0;
}

#app {
  width: 100vw;
  height: 100vh;
}

.flex-row {
  display: flex;
  flex-direction: row;
}

.flex-col {
  display: flex;
  flex-direction: column;
}

.justify-start {
  justify-content: flex-start;
}

.justify-end {
  justify-content: flex-end;
}

.justify-center {
  justify-content: center;
}

.justify-between {
  justify-content: space-between;
}

.justify-around {
  justify-content: space-around;
}

.justify-evenly {
  justify-content: space-evenly;
}

.items-start {
  align-items: flex-start;
}

.items-end {
  align-items: flex-end;
}

.items-center {
  align-items: center;
}

.items-baseline {
  align-items: baseline;
}

.items-stretch {
  align-items: stretch;
}

.self-start {
  align-self: flex-start;
}

.self-end {
  align-self: flex-end;
}

.self-center {
  align-self: center;
}

.self-baseline {
  align-self: baseline;
}

.self-stretch {
  align-self: stretch;
}

.flex-1 {
  flex: 1 1 0%;
}

.flex-auto {
  flex: 1 1 auto;
}

.grow {
  flex-grow: 1;
}

.grow-0 {
  flex-grow: 0;
}

.shrink {
  flex-shrink: 1;
}

.shrink-0 {
  flex-shrink: 0;
}

.relative {
  position: relative;
}
</style>
