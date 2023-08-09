<!--历史记录-->
<template>
  <div class="idxs">
    <div style="
  height:1000px;margin-right: 500px;margin-left: 0px /* 设置容器高度为视口高度 */;overflow: hidden">
      <div style="width: 1400px;height: 1000px;position: absolute;">
        <!--顶部栏-->
        <span>历史记录</span>
        <span class="button-container"> <el-button>搜索历史记录</el-button></span>
        <span class="button-container"> <el-button>清空历史记录</el-button></span>
        <!--分割线-->
        <el-divider><i class="el-icon-mobile-phone"></i></el-divider>
        <div style="width: 1400px;height: 1000px;position: absolute;">

          <!--收藏卡片-->
          <el-card class="zoom" v-for="history in historyArr" style="margin-top: 10px">
            <br>
            <div style="width:1200px;height:300px;margin-left:100px">
              <div style="float:left"><img :src="history.videoImgUrl" width="360px" height="300px"></div>
              <div style="float:right">
                <div class="box" style="width:800px;height:80px;justify-content: center;">{{ history.title }}</div>
                <div class="box" style="width:800px;height:80px;margin:30px 0 ">
                  <span style="margin-left: 40px"> <el-avatar style="width:80px;height: 80px"><img
                      :src="history.imgUrl"></el-avatar></span>
                  <span> {{ history.nickname }}</span>
                  <span style="display: flex; justify-content: flex-end;margin-left: auto;"
                        @click="deleteHistory(history.historyID)">
                  <el-button type="danger" icon="el-icon-delete" circle></el-button>
              </span>
                </div>
                <div class="box" style="width:800px;height:80px;">
                  <span style="margin-left: 120px">观看时间:{{ history.watchDate }}</span>
                  <span
                      style="display: flex; justify-content: flex-end;margin-left: auto;">时间戳:{{ history.watchDuration }}</span>
                </div>
              </div>
            </div>
          </el-card>


        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Handder from "@/components/Handder.vue";
import Sidebar from "@/components/sidebar.vue";
import history from "@/views/personal/history.vue";
import {deleteByVideoId, getHistoryList} from "@/utils/option";

export default {
  name: "history",
  computed: {
    history() {
      return history
    }
  },
  components: {Sidebar, Handder},
  data() {
    return {
      size: "200",
      avatarUrl: "",

      historyArr: {
        historyID: '',
        videoImgUrl: '',
        userID: '',
        videoID: '',
        imgUrl: '',
        title: '',
        nickname: '',
        watchDate: '',
        watchDuration: ''
      }
      // historyArr:[
      //   {
      //     historyImgUrl:require("@/assets/1.png"),
      //     historyTitle:"汤蠢蠢",
      //     headImgUrl:require("@/assets/a.jpg"),
      //     nickName:"佩奇",
      //     viewTime:"2023.10.13",
      //     time:"3分40秒"
      //   },
      //   {
      //     historyImgUrl:require("@/assets/1.png"),
      //     historyTitle:"汤蠢蠢",
      //     headImgUrl:require("@/assets/a.jpg"),
      //     nickName:"佩奇",
      //     viewTime:"2023.10.13",
      //     time:"3分40秒"
      //   },
      //   {
      //     historyImgUrl:require("@/assets/1.png"),
      //     historyTitle:"汤蠢蠢",
      //     headImgUrl:require("@/assets/a.jpg"),
      //     nickName:"佩奇",
      //     viewTime:"2023.10.13",
      //     time:"3分40秒"
      //   }
      // ]
    }
  },
  methods: {
    loadHistoryList() {
      getHistoryList().then(res => {
        if (res.success) {
          this.historyArr = res.data;
        }
      })
    },
    deleteHistory(historyID) {
      deleteByVideoId({historyID: historyID}).then(res => {
        if (res.success) {
          this.$notify.success({
            title: '删除成功',
            position: 'bottom-right'
          })
          this.loadHistoryList();
        } else {
          this.$notify.error({
            position: 'bottom-right',
            title: res.message
          })
        }
      })
    }
  },
  created() {
    // 根据用户id获取历史记录
    this.loadHistoryList()
  }
}
</script>

<style scoped>

.button-container {
  display: flex;
  justify-content: flex-end;
  float: right;
}

.box {
  display: flex;
  align-items: center;
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
    margin-left: 0px;
  }

  /*悬浮调大小*/
  .zoom:hover {
    transform: scale(1.1); /* (150% zoom - Note: if the zoom is too large, it will go outside of the viewport) */
  }

  /*悬浮调动画时间*/
  .zoom {
    transition: transform 0.3s; /* Animation */
  }
}
</style>