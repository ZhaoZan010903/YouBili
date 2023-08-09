<!--个人中心-->
<template>
  <div>
    <div>
      <div class="idxs">
        <h1 style="font-size: 45px;text-align: center;margin-top: 30px;margin-bottom: 30px">
          {{ user.nickname }}的个人中心</h1>

        <div style="width: 1300px;height: 120px;margin-left: 50px;margin-bottom: 50px">

          <el-card>

            <el-avatar style="margin-bottom: 15px;float:left;width: 100px;height: 100px;" :src="user.imgUrl"
                       alt="头像" :round="true"></el-avatar>
            <ul style="float: left;margin-top: 30px;margin-left: 30px;list-style-type: none">
              <li>昵称: {{ user.nickname }}</li>
              <li>生日: {{ user.birthday }}</li>
              <li>会员等级: {{ user.accountLevel }}</li>
            </ul>

            <div style="width: 300px;float: right;padding-top: 20px">
              <div style="margin-left: 130px;height: 36px">

              </div>
              <el-descriptions style="margin-left: 10px;" class="margin-top" :column="4"
                               direction="vertical" :colon="false">
                <el-descriptions-item class="aa" label="关注数">{{
                    user.follows
                  }}
                </el-descriptions-item>
                <el-descriptions-item class="aa" label="粉丝数">{{
                    user.fans
                  }}
                </el-descriptions-item>
                <el-descriptions-item class="aa" label="获赞数">{{
                    user.totalLikes
                  }}
                </el-descriptions-item>

              </el-descriptions>
            </div>


          </el-card>


        </div>
        <div style="width: 1300px;margin-top: 30px;margin-left: 50px;height: 1000px;">
          <el-card>
            <div v-for="(c,id) in categoryArr">
              <h2>{{ c }}</h2>
              <el-divider></el-divider>
              <div>
                <el-card style="margin-bottom: 10px;" v-for="p in user.videoList.slice(id*3,(id+1)*3)"
                         v-if="p !== null"
                         @click.native="jump(p)">
                  <div style="float: left"><img :src="p.videoImgUrl"
                                                style="width: 300px;height: 250px;margin-left: 80px">
                  </div>
                  <div style="width: 700px;height: 250px;;float: right">
                    <div style="height: 180px;">
                      <p style="display: inline-block;width: 650px;height: 30px;margin-top: 20px">
                        <b> {{ p.title }}</b></p><br>
                      <p style="display: inline-block;width: 650px;height: 100px;margin-top: 20px">
                        {{ p.description }}</p></div>
                    <el-button style="width: 68px;height: 20px;line-height: 3px" type="info">
                      {{ p.categoryName }}
                    </el-button>
                    <div style="width:500px;height: 28px;line-height: 25px">
                      <h5>观看次数: {{ p.viewCount }} 评论数: {{ p.commentCount }}</h5>
                    </div>
                  </div>
                </el-card>
                <br>
                <div style="margin-top: 15px;text-align: center">
                  <el-button plain>查看更多</el-button>
                </div>
              </div>
              <el-divider></el-divider>
            </div>

          </el-card>
        </div>

      </div>
    </div>
  </div>

</template>

<script>
import Sidebar from "@/components/sidebar.vue";
import Handder from "@/components/Handder.vue";
import {getPersonalCenterInfo} from "@/utils/option";


export default {
  name: "personalCenter",
  components: {Handder, Sidebar},
  data() {
    return {
      userID: 0,
      size: "200",
      user: {
        imgUrl: "",
        nickname: "",
        fans: "",
        follows: "",
        totalLikes: "",
        birthday: "",
        accountLevel: "",
        videoList: [
          {
            nickname: "",
            imgUrl: "",
            videoImgUrl: "",
            title: "",
            likeCount: "",
            viewCount: "",
            categoryName: "",
            commentCount: "",
            description: ""
          }
        ]
      },
      categoryArr: ["我的视频", "我的收藏", "我的历史"],

    }
  },
  methods: {
    jump(data) {
      this.$router.push({path: "/videoIndex", query: data})
    },
  },
  created() {
    this.userID = this.$route.query.userID == null ? 0 : this.$route.query.userID
    console.log(this.userID)
    // this.$nextTick(() => {
    getPersonalCenterInfo(this.userID).then(response => {
      if (response.success) {
        // console.log(response.data)
        this.user = response.data;
        console.log(this.user)
      } else {
        this.$message.error(response.message)
      }
    })
    // })

  },
  mounted() {
    setTimeout(() => {
      console.log(this.user)
    }, 200)

  }
}
</script>

<style scoped>
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
    /*width: 1270px;*/
    /*min-width: 1000px;*/
    /*margin-top: 60px;*/
    /*margin-left: 270px;*/
    /*padding: 40px;*/
    width: calc(100% - 290px);
    max-width: 1570px;
    min-width: 1200px;
    margin: 60px auto;
  }

  .idxs {
    margin-left: 30px;
  }
}

img:hover {
  transform: scale(1.1); /* (150% zoom - Note: if the zoom is too large, it will go outside of the viewport) */
}

img {
  transition: transform 0.3s; /* Animation */
}

h2 {
  font-weight: normal;
}

i {
  display: inline-block;
  margin-left: 20px;
}

.aa:hover {
  cursor: pointer;
}

img:hover {
  transform: scale(1.1);
  cursor: pointer;
}

</style>