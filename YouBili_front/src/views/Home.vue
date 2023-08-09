<template>
  <div>
    <Handder></Handder>
    <sidebarVue></sidebarVue>
    <div class="idxs">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import Handder from "@/components/Handder";
import sidebarVue from "@/components/sidebar";

export default {
  name: "Home",
  components: {
    Handder,
    sidebarVue
  },
  methods: {
    changStyle(widthShow) {
      let sty = document.getElementsByClassName("idxs")[0];

      if (widthShow) {
        sty.style.width = "calc(100% - 20px)";
        sty.style.margin = "90px 0 0 0 "
      } else {
        sty.style.width = "calc(100% - 290px)"
        sty.style.margin = "90px 0 0 270px"
      }
    }
  }
}
</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
  border: 0;
  overflow-x: hidden;
}

.idxs {
  transition: all .6s;
}

@media screen and (min-width: 1970px) {
  .idxs {
    margin: 0 auto;
    width: calc(100% - 290px);
    max-width: 1970px;
    min-width: 1200px;
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
    margin-top: 80px;
    margin-left: 270px;
    /*padding: 40px;*/
    width: calc(100% - 290px);
    max-width: 1970px;
    min-width: 1200px;
  }
}
</style>