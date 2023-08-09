<template>
  <div id="app">
    <!--    <handder></handder>-->
    <!--    <div id="content">-->
    <!--      <sidebar-vue></sidebar-vue>-->
    <!--      <div class="i_w">-->
    <router-view/>
    <!--      </div>-->
    <!--      -->
    <!--    </div>-->
  </div>
</template>

<script>
export default {}
</script>

<style>
* {
  padding: 0;
  margin: 0;
  text-decoration: none;
  border: 0;
}

#content {
  display: flex;
  justify-content: flex-start;
}

.i_w {
  margin-left: 250px;
  margin-top: 60px;
  width: calc(100% - 250px);
  /* margin: 60px auto; */
}
</style>
