<!--侧边栏-->
<template>
  <div id="mao">
    <el-menu
        id="sidebar"
        class="el-menu-vertical-demo"
        :default-active="$router.currentRoute.path"
        router
        v-show="isShow"
        active-text-color="#fe3355"
    >
      <el-submenu :index="i+''" v-for="(v, i) of themeAll" :key="i">
        <template slot="title">
          <span>{{ v }}</span>
        </template>
        <el-menu-item v-for="(item, idx) of contentAll"
                      v-if="v==item.father" v-show="!item.isShow"
                      :index="'/'+item.path">
          <template slot="title">
            <i class="iColor" :class="item.icon"></i>
            <span>{{ item.chinaName }}</span>
          </template>
        </el-menu-item>
      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
import Home from "@/views/Home";

export default {
  name: "sidebar",
  data() {
    return {
      themeAll: [],
      contentAll: [],
      isShow: true
    }
  },
  methods: {
    changeWidth(widthShow) {
      let sidebar = document.getElementById("sidebar");
      if (widthShow) {
        sidebar.style.width = 0;
      } else {
        sidebar.style.width = 250 + "px";
      }
      Home.methods.changStyle(widthShow)
    }
  },
  created() {
    console.log(this.$router.options.routes)
    let a = this.$router.options.routes[0].children
    let arr1 = a.map((view, idx) => {
      return {
        chinaName: view.chinaName,
        path: view.path,
        father: view.meta.father,
        icon: view.meta.icon,
        isShow: view.meta.isShow
      }
    })
    let arr = a.map((view, idx) => {
      return view.meta.father;
    })
    let item = Array.from(new Set(arr))
    this.themeAll = item;
    this.contentAll = arr1;
    console.log(this.$router.currentRoute.path);
  },
}
</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
//color: #fff;
}

#mao {

}

#sidebar {
  position: fixed;
  top: 80px;
  left: 0;
  bottom: 0;
  width: 250px;
  overflow-y: auto;
  border: 0;
  background-image: linear-gradient(#FF9565, #FB2C4C);
  transition: all .6s;
}

/deep/ #sidebar li ul {
  background-color: transparent !important;
}

#sidebar .el-menu {
  border: 0;

}

ul .el-menu .el-menu--inline {
  background-color: #000 !important;
}

.el-menu-item.is-active {
  background: #fad7a1 !important;
}

a {
  color: #303133;
}

.uicss-cn {

  height: 580px;
  overflow-y: scroll;

  scrollbar-face-color: #EAEAEA;

  scrollbar-shadow-color: #EAEAEA;

  scrollbar-highlight-color: #EAEAEA;

  scrollbar-3dlight-color: #EAEAEA;

  scrollbar-darkshadow-color: #697074;

  scrollbar-track-color: #F7F7F7;

  scrollbar-arrow-color: #666666;

}

/deep/ .el-menu-item:hover {
  background-color: #d26161 !important;
}

::v-deep .el-submenu__title:hover {
  background-color: #d26161 !important;
}

::v-deep .el-submenu__title i {
  color: #ffffff;
}

.iColor {
  margin: 0 10px;
  color: #ffffff;
}

.el-menu-item {
  color: #ffffff;
}

::v-deep .el-submenu__title {
  color: #ffffff;
}
</style>