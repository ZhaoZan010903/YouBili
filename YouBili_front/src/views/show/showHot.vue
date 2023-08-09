<!--热门-->
<template>
  <div>

    <div style="width: auto">
      <div style="margin-left: 600px">
        <h1>热门排行</h1>
      </div>
      <div style="width: auto">
        <el-divider></el-divider>
      </div>
    </div>

    <div>
      <div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="grid-content bg-purple"></div>
          </el-col>


          <el-col :span="12">
            <!--            <div class="idxe">-->
            <!--            </div>-->
            <div class="grid-content bg-purple">
              <div style="margin-left: 250px;margin-top: 30px;width: auto" v-for="(v,i) of arr" :key="i">
                <el-card style="width: 900px;height: 250px" @click.native="jump(v.videoID)">
                  <div style="display: flex">
                    <div style="height: 200px;width: 350px;margin-left: 15px;margin-top: 10px">
                      <img :src="v.videoImgUrl" style="width: 350px;height: 200px">
                    </div>
                    <div style="margin-top: 5px;margin-left: 50px">
                      <h1>
                        {{ v.title }}
                      </h1>
                      <div class="block" style="display: flex;margin-top: 5px">
                        <div>
                          <el-avatar :size="50"
                                     :src="v.imgUrl"></el-avatar>
                        </div>
                        <div style="margin-left: 5px;margin-top: 10px;font-size: 18px">{{ v.nickname }}</div>
                      </div>
                      <div style="margin-top: 80px">
                        <span>{{ v.uploadDate }}</span>
                      </div>
                    </div>

                  </div>
                </el-card>
              </div>
            </div>
          </el-col>


          <el-col :span="6">
            <div class="grid-content bg-purple"></div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>

  <!--  </div>-->
</template>

<script>


import {getShowHot} from "@/utils/option";

export default {
  name: "showHot",
  data() {
    return {
      size: "200",
      arr: {
        videoID: '',
        videoImgUrl: '',
        title: '',

      }
    }
  },
  methods: {
    jump(data) {
      this.$router.push({path: "/videoIndex", query: {videoID: data}})
    }
  },
  created() {
    getShowHot().then(response => {
      if (response.status) {
        this.arr = response.data
      } else {
        this.$message.error(response.message)
      }
    })
  }
}
</script>

<style scoped>

.tab-label {
  font-size: 300px; /* 调整文字大小为16px，可根据需要进行修改 */
}


.box {
  display: flex;
  align-items: center; /* 调整文字位置,让其垂直居中 */
}

@media screen and (min-width: 1970px) {
  .idxs {
    margin: 60px auto;
    width: calc(100% - 290px);
    max-width: 1570px;
    min-width: 1200px;
  }
}

@media screen and (max-width: 1969px) {
  .idxs {
    width: 1270px;
    min-width: 1000px;
    margin-top: 60px;
    margin-left: 270px;
    padding: 40px;
    width: calc(100% - 290px);
    max-width: 1570px;
    min-width: 1200px;
    margin: 60px auto;
  }

  .idxs {
    margin-left: 250px;
  }
}

/*悬浮调大小*/
.zoom:hover {
  transform: scale(1.1); /* (150% zoom - Note: if the zoom is too large, it will go outside of the viewport) */
}

/*悬浮调动画时间*/
.zoom {
  transition: transform 0.3s; /* Animation */
}
</style>