<!--收藏-->
<template>
  <div>
    <div>
      <div style="width: auto">
        <div style="margin-left: 600px">
          <h1>收藏记录</h1>
        </div>
        <div style="width: auto">
          <el-divider></el-divider>
        </div>
      </div>

      <div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="grid-content bg-purple"></div>
          </el-col>


          <el-col :span="12">
            <!--            <div class="idxe">-->
            <el-result icon="error" title="错误提示" subTitle="请根据提示进行操作" v-if="!isShow">
              <template slot="extra">
              </template>
            </el-result>
            <!--            </div>-->
            <div class="grid-content bg-purple" v-else>
              <div style="margin-left: 250px;margin-top: 30px;width: auto" v-for="(v,i) of favorite" :key="i">
                <el-card style="width: 900px;height: 250px">
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

                      <div style="margin-top: 60px;display: flex">
                        <div>收藏日期:<span> {{ v.favoriteDate }} </span></div>
                        <div style="margin-left: 300px">
                          <el-button type="danger" icon="el-icon-delete" circle
                                     @click="deleted(v.favoriteID)"></el-button>
                        </div>
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
</template>

<style>


</style>

<script>

import {deleteCollection, getCollection} from "@/utils/option";
import login from "@/components/login.vue";

export default {
  name: "collection",
  computed: {},
  data() {
    return {
      isShow: '',
      favorite: {
        favoriteID: '',
        videoID: '',
        nickname: '',
        favoriteDate: '',
        title: '',
        imgUrl: '',
        videoImgUrl: '',
      },

    }
  },

  methods: {

    deleted(favoriteID) {
      this.$confirm('此收藏记录要被永久删除,是否确定此操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        console.log("被删除的ID是:", favoriteID);
        // 写axios; 删除方法!;
        deleteCollection({favoriteID: favoriteID}).then(res => {
          if (res.success) {
            this.$notify({
              title: '删除成功',
              message: res.data,
              position: 'bottom-right',
              type: 'success'
            });
          } else {
            this.$notify.error({
              title: '错误',
              message: '这是一条错误的提示消息',
              position: 'bottom-right',
            });
          }
        })

      }).catch(() => {
        this.$notify({
          title: '取消删除',
          message: '',
          position: 'bottom-right',
        });
      });
    }

  },
  created() {
    // 获取收藏列表;
    getCollection().then(res => {
      if (res.success) {
        this.favorite = res.data
        this.isShow = true
        // if (res.data === null) {
        //   this.isShow = false;
        // }
        console.log("收藏列表加载完毕")
      } else {
        this.$notify({
          title: '出现问题',
          message: res.message,
          position: 'bottom-right',
          type: 'error'
        })
        this.isShow = false;
      }
    })
  }
}

</script>

<style scoped>

.zoom:hover {
  transform: scale(1.1); /* (150% zoom - Note: if the zoom is too large, it will go outside of the viewport) */
}

.zoom {
  transition: transform 0.3s; /* Animation */
}

.idxe {
  margin-left: 550px;
  margin-top: 200px;
}
</style>