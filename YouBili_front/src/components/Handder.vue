<!--头部导航栏-->
<template>
  <div id="box">
    <div id="left">
      <div>
        <i class="el-icon-s-fold" style="color: #fff" @click="controlLeft"></i>
      </div>
      <div class="logo-sty">
        <router-link to="/index">
          <el-image :src="src"></el-image>
        </router-link>
      </div>
    </div>
    <div>
      <el-autocomplete
          class="inline-input"
          v-model="state1"
          :fetch-suggestions="querySearch"
          placeholder="请输入内容"
          @select="handleSelect"
          @input="inputChange"
      >
        <i
            @click="search"
            class="el-icon-search el-input__icon"
            slot="suffix">
        </i>
      </el-autocomplete>
    </div>
    <div id="right">
      <div class="popover" v-if="!isLogin">
        <!-- 头像 -->
        <el-popover
            placement="bottom"
            title=""
            width="300"
            trigger="hover"
            content="">
          <el-avatar slot="reference" :size="40" :src="userImg1"></el-avatar>
          <div>
            <h4>登录以后你可以:</h4>
            <el-row :gutter="30">
              <el-col :span="11">
                <p class="p-m"><i class="el-icon-monitor"></i> 免费看高清视频</p>
              </el-col>
              <el-col :span="13">
                <p class="p-m"><i class="el-icon-time"></i> 多端同步播放记录</p>
              </el-col>
            </el-row>
            <el-row :gutter="30">
              <el-col :span="11">
                <p class="p-m"><i class="el-icon-chat-dot-square"></i> 发表弹幕/评论</p>
              </el-col>
              <el-col :span="13">
                <p class="p-m"><i class="el-icon-video-play"></i> 热门番剧影视看不停</p>
              </el-col>
            </el-row>
            <el-button style="width:100%" type="primary" @click="dialogVisible=true">立即登录</el-button>
          </div>
        </el-popover>
      </div>
      <div class="popover" v-else>
        <el-popover
            placement="bottom"
            title=""
            width="300"
            trigger="hover"
            content="">
          <el-avatar class="avatar" slot="reference" :size="40" :src="userData.imgUrl"></el-avatar>
          <div class="myCenter">
            <h3>{{ userData.nickname }}</h3>
            <div class="nick-sty">
              <div>
                <p>{{ userData.follows }}</p>
                <span>关注</span>
              </div>
              <div>
                <p>{{ userData.fans }}</p>
                <span>粉丝</span>
              </div>
              <div>
                <p>{{ userData.accountLevel }}</p>
                <span>等级</span>
              </div>
            </div>
            <p @click="jump(userID)"><i style="margin-right: 20px;font-size: 20px;" class="el-icon-user"></i>个人中心
            </p>            <!-- <el-divider></el-divider> -->
            <p @click="logout"><i style="margin-right: 20px;font-size: 20px;" class="el-icon-switch-button"></i>退出登录
            </p>
          </div>
        </el-popover>
      </div>
      <el-popover
          placement="bottom"
          title=""
          width="350"
          trigger="hover"
          content="">
        <div slot="reference" ref="collect" @mouseover="handleMouseOver" class="sc_cls">
          <i class="el-icon-star-off"></i>
          <p>收藏</p>
        </div>

        <div class="big-box">
          <div v-if="!isLogin" style="margin-top: 50px">
            <el-empty description="未登录,请您先登录"></el-empty>
            <el-button style="width:100%" type="primary" @click="dialogVisible=true">立即登录</el-button>
          </div>
          <div v-for="(v,i) of scList" :key="i" class="sc-box" @click="jumpVideo(v.videoID)">
            <!-- 图片 -->
            <div>
              <img width="120px" height="100%" :src="v.videoImgUrl" alt="">
            </div>
            <div class="sc-content">
              <div>
                <h4>{{ v.title }}</h4>
              </div>
              <div>
                <p>{{ v.nickname }}</p>
              </div>
            </div>
          </div>
        </div>
      </el-popover>
      <el-popover
          placement="bottom"
          title=""
          width="350"
          trigger="hover"
          content="">
        <div slot="reference" class="sc_cls">
          <i class="el-icon-time"></i>
          <p>历史</p>
        </div>

        <div class="big-box">
          <div v-if="!isLogin" style="margin-top: 50px">
            <el-empty description="未登录,请您先登录"></el-empty>
            <el-button style="width:100%" type="primary" @click="dialogVisible=true">立即登录</el-button>
          </div>
          <div v-for="(v,i) of lsList" :key="i" class="sc-box">
            <!-- 图片 -->
            <div>
              <img width="120px" height="100%" :src="v.videoImgUrl" alt="">
            </div>
            <div class="sc-content">
              <div>
                <h4>{{ v.title }}</h4>
              </div>
              <div>
                <p>{{ v.watchDate }}</p>
              </div>
              <div>
                <p>{{ v.nickname }}</p>
              </div>
            </div>
          </div>
        </div>
      </el-popover>
      <div>
        <el-button type="primary" icon="el-icon-upload">投稿</el-button>
      </div>
    </div>


    <!-- 登录的弹窗 -->
    <login :isTrue="dialogVisible" @showAvatar="avatar" @close="dialogVisible=false"></login>
  </div>
</template>

<script>
import {getHeaderCollect, getHeaderHistory, getMe, idxSearch, logout} from "@/utils/option"
import login from "./login"
import sidebar from "@/components/sidebar";

export default {
  name: "handder",
  components: {
    login
  },
  data() {

    return {
      widthShow: true,
      userData: {},
      username: "昵称",
      userID: null, // 当前登录用户的id
      isLogin: false,
      dialogVisible: false, // 登录注册弹窗
      src: "blLogo.png", // 图片路径
      restaurants: [], // 输入框模糊查询出的数据  需要后台传输
      state1: '', // 用户选中的数据
      userImg1: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
      userImg: "https://uploadstatic.mihoyo.com/contentweb/20191114/2019111415040910416.png",
      sclist: true,
      lslist: true,
      scList: [
        {},

      ],
      lsList: [
        {},
      ],
    }
  },

  methods: {
    // 跳转视频页
    jumpVideo(data) {
      this.$router.push({path: "/videoIndex", query: {videoID: data}})
      location.reload()
    },
    // 跳转页面并且传参数
    jump(data) {
      console.log(data)
      this.$router.push({path: "personalCenter", query: {userID: data}})
    },
    // 打开侧边栏
    controlLeft(widthShow) {
      sidebar.methods.changeWidth(this.widthShow);
      this.widthShow = !this.widthShow
    },

    //logout
    logout() {
      logout().then(res => {
        if (res.success) {
          this.$notify.success({
            title: '成功',
            message: res.message,
            position: 'bottom-right'
          })
          localStorage.removeItem("youbiliToken");
          localStorage.removeItem("searches");
          // this.$router.push("/")
          // setTimeout(() => {
          //   location.reload();
          // },0)

        }
      })
    },

    // 展示头像
    avatar(data) {
      this.isLogin = true;
      this.userData = data;
      console.log(this.userData)
      location.reload();
    },

    // 查询
    search() {
      console.log(this.state1);
      if (this.state1.trim() !== "") {  // 确保搜索内容不为空
        var searches = localStorage.getItem("searches");  // 从localStorage中获取之前的搜索记录

        if (searches === null) {  // 如果localStorage中没有搜索记录，创建一个空数组
          searches = [];
        } else {  // 如果localStorage中已有搜索记录，解析出数组
          searches = JSON.parse(searches);
        }
        var searchObject = {  // 创建包含搜索内容的对象
          value: this.state1,
          index: searches.length + 1
        };

        console.log(searches.length)


        searches.push(searchObject);  // 将当前搜索内容添加到数组中

        localStorage.setItem("searches", JSON.stringify(searches));  // 将更新后的搜索记录保存到localStorage中

        // 其他处理逻辑...

        console.log("搜索内容已保存到localStorage中");
      }
      if (location.href.includes("search")) {
        // if (!location.href.split("=")[1] !== this.state1 || !location.href.includes("value")) {
        //   window.location.href = window.location.href.split("=")[1] + '?value=' + this.state1
        //   window.location.reload()
        // }

        window.location.href = window.location.href.split("?")[0] + "?value=" + this.state1
        window.location.reload()

      } else {
        this.$router.push({path: "/search", query: {value: this.state1}})
      }
      // this.$bus.$emit("giveValue", this.state1)
      // this.$router.push('/search')
    },


    // 鼠标移动到收藏的时候 发生的变化
    handleMouseOver() {
      let sLeft = this.$refs.collect.clientLeft;
      let sTop = this.$refs.collect.clientTop;
      console.log(sLeft, sTop, this.$refs.collect);
    },
    querySearch(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    // 用户输入内容筛选
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },

    inputChange(item) {
      idxSearch({"title": item}).then(res => {
        if (res.success) {
          let arr = res.data

          arr = arr.map((v, i) => {
            return {value: v.title, index: v.videoID}
          })
          console.log(arr)
          this.restaurants = arr;

        }
      });
    },


    // 输入框方法
    handleSelect(item) {
      console.log(11111);
    },
    // 需要后台输入的
// 需要后台输入的
    loadAll() {
      // var searches = localStorage.getItem("searches");
      // console.log(searches)
      //
      let searchArr = JSON.parse(localStorage.getItem("searches"));
      searchArr = searchArr.slice(-10);
      searchArr = searchArr.reverse();
      if (searchArr == null || searchArr == []) {
        return []
      }
      return searchArr


      // return searches
    },
    // 获取个人信息
    getMeData() {
      if (window.localStorage.getItem("youbiliToken")) {
        getMe().then(res => {
          if (res.success) {
            console.log(res.data)
            this.userData = res.data;
            this.isLogin = true;
          }
        })
      }
    }
  },
  created() {
    // 获取个人信息
    this.getMeData();
    // 根据用户id获取收藏记录
    getHeaderCollect().then(res => {
      if (res.success) {
        this.scList = res.data;
      }
    })
    // 根据用户id获取历史记录
    getHeaderHistory().then(res => {
      if (res.success) {
        this.lsList = res.data;
      }
    })
  },
  mounted() {
    this.restaurants = this.loadAll();
  }
}
</script>

<style scoped>
i {
  cursor: pointer;
}

#box {
  position: fixed;
  top: 0;
  left: 0;
//background: #fff; height: 80px; width: 100%;
  min-width: 1200px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 99999;
  background-image: linear-gradient(#feb692, #ea5455);
}

#left {
  width: 200px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

#right {
  width: 300px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.sc_cls {
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
  position: relative;
}

.sc_cls i {
  transform: rotate(0deg);
  transition: all .3s ease;
}

.sc_cls:hover i {
  /* transform: rotate(360deg); */
  transform: translateY(-4px);
}

.sc_cls p {
  font-size: 12px;
}

.el-autocomplete {
  width: 680px;
}

.popover {
  cursor: pointer;
}

.p-m {
  margin: 10px 0;
  cursor: pointer;
  font-weight: bold;
}

.p-m i {
  font-size: 15px;
}

.big-box {
  overflow-y: auto;
  height: 400px;
}

.sc-box {
  padding: 10px 0;
  width: 100%;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  transition: all .3s;
  cursor: pointer;
}

.sc-box:hover {
  background: #f1f2f3;
}

.sc-box > div {
  height: 60px;
}

.sc-content {
  height: 100%;
  margin-left: 10px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-start;
}

.sc-content p {
  font-size: 13px;
}

.avatar {
  transition: all .3s;
}

.popover:hover .avatar {
  /* transform: scale(2) translateX(-25%); */
}

.myCenter {
  text-align: center;
}

.myCenter > p {
  cursor: pointer;
  text-align: left;
  border-radius: 5px;
  line-height: 35px;
  padding-left: 20px;
}

.myCenter > p:hover {
  background: #E3E5E7;
}

.nick-sty {
  padding: 20px;
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nick-sty p {
  font-size: 16px;
  font-weight: bold;
}

.nick-sty span {
  font-size: 12px;
}

.logo-sty {
  width: 100px;
}
</style>