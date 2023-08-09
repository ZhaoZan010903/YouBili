<template>
  <div class="idxs">
    <!--        设置关注和粉丝之间的跳转-->
    <el-menu mode="horizontal" :default-active="'/follow'" @select="handleSelect" active-text-color="orange" router>
      <el-menu-item index="/fans">粉丝</el-menu-item>
      <el-menu-item index="/follow">关注</el-menu-item>
    </el-menu>

    <el-empty description="没有粉丝" v-if="abc"></el-empty>
    <div v-for="(v,i) in fans">
      <el-card class="zoom" style="height: 120px;width: 800px; margin-left: 200px;margin-top: 30px;">
        <el-col>
          <el-row :span="8" style="margin-top: 10px">
            <div>
              <el-popover placement="bottom" width="200" trigger="hover">
                <el-row :gutter="10">
                  <el-col :span="12">
                    <span><i class="el-icon-user"></i><span>粉丝数:</span></span>
                    <span>{{ v.fansCount }}</span>
                  </el-col>
                  <el-col :span="12">
                    <span><i class="el-icon-user"></i><span>关注数:</span></span>
                    <span>{{ v.followCount }}</span>
                  </el-col>
                </el-row>
                <el-avatar slot="reference" style="cursor: pointer" shape="square" :size="60"
                           :src=v.imgUrl @click.native="jump(v.followerID)"></el-avatar>
              </el-popover>
            </div>
          </el-row>
          <el-row :span="8" style="margin-top: -45px;margin-left: 50px">
            <div style="margin-left: 50px;margin-top: 0px"><h3>{{ v.nickname }}</h3></div>
          </el-row>
          <!--            取消关注-->
          <el-row :span="8" style="margin-left: 650px;margin-top: -30px">
            <el-button type="primary" round @click="addFollow(v.nickname,v.followID,v.isShow,i,v)">关注</el-button>
          </el-row>

        </el-col>
      </el-card>
    </div>
  </div>
</template>

<script>
import {
  followInfo,
  followDelete, followList, addfan, fanlist, fansList

} from "@/utils/option";
import axios from "axios";

export default {
  name: "personalCenter",
  data() {
    return {
      activeName: 'first',
      abc: false,
      fans: [
        {
          followID: '',
          followerID: null,
          userID: '',
          nickname: '',
          fansCount: '',
          followCount: '',
          text: '',
          imgUrl: '',
        }
      ],
    }
  },

  methods: {
    // 跳转页面并且传参数
    jump(data) {
      console.log(data)
      this.$router.push({path: "otherSpace", query: {userID: data}})
    },
    //加载关注信息
    load() {
      fansList().then((res) => {
        if (res.success) {
          this.fans = res.data;
          console.log(this.fans)
        } else {
          let title = '操作失败';
          this.$alert(res.message, title, {
            confirmButtonText: '确定',
          });
        }
      })
    },
    //添加关注
    addFollow(nickname, followedID) {
      console.log(followedID)
      addfan({userID: followedID}).then(res => {
        if (res.success) {
          this.load();
          this.$notify.success({
            message: '关注成功',
            position: 'bottom-right'
          });
        }
      })
    },


  },
  created() {
    this.load();
  },
  mounted() {

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
    /*margin-left: 250px;*/
  }
}

.el-card:hover {

}

.zoom:hover {
  transform: scale(1.1); /* (150% zoom - Note: if the zoom is too large, it will go outside of the viewport) */
}

.zoom {
  transition: transform 0.3s; /* Animation */
}

</style>