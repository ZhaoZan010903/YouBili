<!--搜索-->
<template>
  <el-container>
    <el-tabs v-model="activeName" @tab-click="categorySelect()" style="padding: 0; left: 20px">
      <el-tab-pane v-for="(item, idx) in categoryAxios" :key="idx" :name="String(item.categoryID)"
                   :label=item.categoryName></el-tab-pane>
      <!--      <el-tab-pane label="电视剧" name="second"></el-tab-pane>-->
      <!--      <el-tab-pane label="综艺" name="third"></el-tab-pane>-->
      <!--      <el-tab-pane label="动漫" name="fourth"></el-tab-pane>-->
      <!--      <el-tab-pane label="自制" name="fifth"></el-tab-pane>-->
      <!--      <el-tab-pane label="搬运" name="sixth"></el-tab-pane>-->
    </el-tabs>

    <el-header style="width: 0px;height: 0px;background-color: white">
      <!--      <div v-model="activeName"> {{ item.categoryName }} </div>-->
    </el-header>
    <div>
      <el-row :gutter="10">
        <el-col class="main-left" :span="24">
          <div class="grid-content bg-purple">
            <el-row :gutter="20">
              <el-col :span="6" v-for="(v, i) of categoryVideo" :key="i">
                <!--              <router-link to="/videoIndex">-->
                <el-card class="box-card" style="height:220px; width: 100%; border-radius: 10px;text-align: left"
                         @click.native="jump(v.videoID)">
                  <img width="100%" height="144px"
                       :src="v.videoImgUrl"
                       style="border-radius: 10px;">
                  <p style="color: #a7a7a7;font-size: 15px; font-weight: bold">{{ v.title }}</p>
                  <!--                  <router-link to="/otherSpace">-->
                  <i class="el-icon-user-solid" style="color: #a7a7a7; font-size: 10px">{{ v.nickname }}
                    <i class="el-icon-time">{{ v.uploadDate }}</i>
                  </i>
                  <!--                  </router-link>-->
                </el-card>
                <!--              </router-link>-->
              </el-col>
            </el-row>
            <el-pagination
                style="text-align: right"
                layout="prev, pager, next"
                :total="1000">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </div>

  </el-container>
</template>
<script>
import {categorySelect, categoryVideo,} from "@/utils/option";

export default {
  name: "fans",
  data() {
    return {
      videoCon: [{
        username: '',
        uploadDate: '',
        videoImgUrl: '',
        videoID: '',
      }],
      activeName: '5',
      categoryAxios: [],
      categoryVideo: [],
    }
  },
  methods: {
    jump(data) {
      console.log(data)
      this.$router.push({path: "/videoIndex", query: {videoID: data}})
    },
    categorySelect() {
      console.log(this.activeName)
      categoryVideo({categoryID: this.activeName}).then(res => {
        if (res.success) {
          this.categoryVideo = res.data
        }
      })
    },
  },
  created() {
    categorySelect().then(res => {
      if (res.success) {
        this.categoryAxios = res.data
        // 获取分类名字
        categoryVideo({categoryID: this.activeName}).then(res => {
          if (res.success) {
            this.categoryVideo = res.data
          }
        })
      } else {
      }
    })
  }
}
</script>

<style scoped>

/*头 身体*/
.el-header {
  /*position: absolute;*/
  /*right: 0;*/
  /*left: 270px;*/
  min-height: 75px;
  background-color: #B3C0D1;
  color: #333;
  text-align: left;
  line-height: 75px;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  padding: 0px;
  position: absolute;
  top: 135px;
  right: 0;
  bottom: 0;
  left: 270px;
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  line-height: 20px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

/*身体布局*/
.el-row {
  margin-bottom: 20px;

}

.el-row:last-child {
  margin-bottom: 0;
}

.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
  background: #99a9bf;
}

.bg-card {
  background: white;
  width: 260px;
}

.bg-purple {
  background: white;
}

.bg-purple-light {
  background: orange;
}

.grid-card {
  border-radius: 10px;
  min-height: 162px;
  position: relative;
  left: -5px;
}

.grid-content {
  border-radius: 10px;
  /*min-height: 562px;*/
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

/*走马灯*/
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

/*卡片样式*/
.box-card:hover {
  transform: scale(1.1); /* (150% zoom - Note: if the zoom is too large, it will go outside of the viewport) */
}

.box-card {
  transition: transform 0.3s; /* Animation */
  margin: 10px 0;
}

.box-card .text {
  pointer-events: none !important;
}


</style>