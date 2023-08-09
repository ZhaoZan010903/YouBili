<!--搜索-->
<template>
  <el-container>
    <el-header></el-header>
    <el-row :gutter="10">
      <el-col class="main-left" :span="24">
        <div class="grid-content bg-purple">
          <el-row :gutter="20">
            <el-col :span="6" v-for="(v, i) of searchValue" :key="i">
              <!--              <router-link to="/videoIndex">-->
              <el-card class="box-card" style="height:220px; width: 100%; border-radius: 10px;text-align: left"
                       @click.native="jump(v.videoID)">
                <img width="100%" height="144px" :src='v.videoImgUrl'
                     style="border-radius: 10px;">
                <p style="color: #a7a7a7;font-size: 15px; font-weight: bold">{{ v.title }}</p>
                <router-link to="/otherSpace">
                  <i class="el-icon-user-solid" style="color: #a7a7a7; font-size: 10px">{{ v.nickname }}
                    <i class="el-icon-time">{{ v.uploadDate }}</i>
                  </i>
                </router-link>
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


  </el-container>
</template>

<script>
import {idxSearch} from "@/utils/option";

export default {
  name: "fans",
  data() {
    return {
      activeName: 'second',
      // searchValue: {
      //   videoID: '',
      //   nickname: '',
      //   duration: '',
      //   title: '',
      //   uploadDate: '',
      //   videoImgUrl: '',
      //   videoSrcUrl: ''
      // }
      searchValue: []
    }
  },
  methods: {
    jump(data) {
      this.$router.push({path: "/videoIndex", query: {videoID: data}})
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    search(keyword) {
      console.log(keyword)
      idxSearch({"title": keyword}).then(res => {
        if (res.success) {
          console.log(res.data)
          this.searchValue = res.data
          this.$notify.success({
            position: 'bottom-right',
            title: '搜索成功'
          });
        } else {
          this.$notify.error("搜索失败");
        }
      });
    }
  },

  created() {


  },
  mounted() {

    // if (location.href.includes("keyword")) {
    //   console.log("**********")
    const keyword = this.$route.query.value;
    this.search(keyword)
    // }


  }
}
</script>

<style scoped>

/*头 身体*/
.el-header {
  /*position: absolute;*/
  /*right: 0;*/
  /*left: 270px;*/
  height: 0px !important;
  color: #333;
  text-align: left;
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

.el-col-6 {

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
