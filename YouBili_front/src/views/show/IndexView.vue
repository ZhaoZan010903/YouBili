<!--主页-->
<template>
  <div id="idx">
    <!--    <div class="idxs">-->
    <div>
      <el-row :gutter="20">
        <el-col :span="12">
          <div style="margin-top: 10px;margin-left: 15px">
            <el-carousel height="500px" trigger="click">
              <el-carousel-item v-for="(item,idx) in lbList" :key="idx">
                <h3 class="small">
                  <img :src="item.videoImgUrl" alt="" height="500px" width="100%">
                </h3>
              </el-carousel-item>
            </el-carousel>
          </div>
        </el-col>

        <el-col :span="12">
          <el-row :gutter="20">
            <el-col v-for="(v,i) of lbList2" :key="i" :span="12">
              <div @click="jump(v.videoID)">
                <!--                   @mouseenter="tjPlayVideo(i)" @mouseleave="tjStopVideo(i)"-->
                <div class="sp_box" style="margin-top: 15px">
                  <img style="height: 200px;" ref="tjVideo" poster="" muted :src="v.videoImgUrl"
                       class="vdo-sty"/>
                  <!--                    </img>-->
                  <h3>{{ v.title }}</h3>
                  <div style="width:100%;display: flex;justify-content: space-between">
                    <div>{{ v.nickname }}</div>
                    <div>{{ v.duration.slice(v.duration.indexOf(":") + 1) }}</div>
                  </div>

                </div>
              </div>
            </el-col>

          </el-row>
        </el-col>

      </el-row>
    </div>
    <el-divider></el-divider>
    <div>
      <el-row :gutter="20" style="overflow-y: hidden">
        <!--          v-infinite-scroll="load"-->
        <el-col v-for="(v,i) of videoList" :key="i" :span="8">
          <el-card style="margin-top: 20px;height: 350px" class="zoom" @click.native="jump(v.videoID)">
            <!--@mouseenter="playVideo(i)" @mouseleave="stopVideo(i)"-->
            <div class="sp_box" style="height: 300px;">
              <img style="height: 260px;" ref="myVideo" poster="" muted :src="v.videoImgUrl"
                   class="vdo-sty"/>

              <h3>{{ v.title }}</h3>
              <div style="width:100%;display: flex;justify-content: space-between">
                <div>{{ v.nickname }}</div>
                <div>{{ v.duration.slice(v.duration.indexOf(":") + 1) }}</div>
              </div>
            </div>
          </el-card>
        </el-col>

      </el-row>
    </div>

    <el-backtop :bottom="50" :right="8" :visibility-height="100"></el-backtop>
    <!--    </div>-->

  </div>
</template>

<!-- 
  1. 将两个组件引入进来,头部导航栏 Handder , sidebar . 在大的<div></div>里面添加<Handder></Handder> <sidebarVue></sidebarVue>,
  2. 在自己的<div></div> 需要引入class="idxs"
  3. 在Style里面添加"idxs"样式
 -->
<script>
import {getIndexVideocarousel, getIndexVideoList,} from "@/utils/option"

export default {
  name: "index",
  data() {
    return {
      isShow: false,
      videoList: [],
      // 轮播图片
      lbList: {
        videoImgUrl: '',
        videoID: '',
        title: '',
      },

      // {src: "https://uploadstatic.mihoyo.com/contentweb/20200509/2020050914430985995.jpg"},
      // {src: "https://uploadstatic.mihoyo.com/contentweb/20200120/2020012017544296758.jpg"},
      // {src: "https://uploadstatic.mihoyo.com/contentweb/20191114/2019111415075614503.png"},
      // {src: "https://uploadstatic.mihoyo.com/contentweb/20191114/2019111415062311645.png"}

      lbList2: []
    }
  },
  mounted() {

  },
  methods: {
    // 跳转页面并且传参数
    jump(data) {
      this.$router.push({path: "/videoIndex", query: {videoID: data}})
    },
    playVideo(idx) {
      this.$refs.audio.load();
      this.$refs["myVideo"][idx].play()
      this.isShow = true;
    },
    stopVideo(idx) {
      if (this.isShow) {
        this.$refs["myVideo"][idx].pause();
      }
    },
    tjPlayVideo(idx) {
      this.$refs["tjVideo"][idx].play()
      this.isShow = true;
    },
    tjStopVideo(idx) {
      if (this.isShow) {
        this.$refs["tjVideo"][idx].pause()
      }
    },
    load() {
      setTimeout(() => {
        this.sum += 10;
      }, 2000)

    }
  },
  created() {
    // // 获取主页面视频列表
    getIndexVideoList().then(res => {
      if (res.data) {
        this.videoList = res.data;
        console.log(res.data)
      } else {
        this.$notify.error({
          position: 'bottom-right',
          title: '失败'
        })
      }
    })

    getIndexVideocarousel().then(res => {
      if (res.data) {
        this.lbList = res.data
        this.lbList2 = res.data
        console.log(this.lbList2)
      } else {
        this.$notify.error({
          position: 'bottom-right',
          title: '失败'
        })
      }
    })
  }
}
</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
  border: 0;
}


@media screen and (min-width: 1970px) {
  .idxs {
    margin: 0 auto;
    width: calc(100% - 290px);
    max-width: 1970px;
    min-width: 1000px;
  }
}

@media screen and (max-width: 1969px) {
  /*.idxs {*/
  /*  position: absolute;*/
  /*  left: 270px;*/
  /*  top: 60px;*/
  /*  bottom: 0;*/
  /*  right: 20px;*/
  /*  max-width: 1970px;*/
  /*  min-width: 1200px;*/
  /*}*/
  .idxs {
    /*width: 1270px;*/
    /*min-width: 1000px;*/
    margin-top: 60px;
    margin-left: 270px;
    /*padding: 40px;*/
    width: calc(100% - 290px);
    max-width: 1970px;
    min-width: 1000px;
  }
}


.lunbo {
  width: 500px;
}

#el_box {
  margin-bottom: 20px;
}

.sp_box {
  border-radius: 5px;
  /*background: greenyellow;*/
  margin-bottom: 20px;
  cursor: pointer;

  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-start;

  width: 100%;
  height: 240px;
}

.vdo-sty {
  border-radius: 5px;
  width: 100%;
  transition: all .4s;
}

.vdo-sty:hover {
  transform: scale(1.03);
}

.sp_box img {
  border-radius: 5px;
}

.sp_box p {
  font-size: 13px;
  color: #555;
  font-weight: bold;
}

.el-carousel {
  border-radius: 5px;
}

/* 轮播图的样式   可以注释 */
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

.zoom:hover {
  transform: scale(1.01); /* (150% zoom - Note: if the zoom is too large, it will go outside of the viewport) */
}

.zoom {
  transition: transform 0.3s; /* Animation */
}

</style>