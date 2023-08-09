<!--投稿界面-->
<template>
  <div class="div1">
    <el-card>
      <h1 style="text-align: center">投稿中心</h1>
      <!-- 文本框开始   -->
      <div>
        <div>
          <h3 class="title_h3">标题</h3>
          <el-input class="text_box_a"
                    type="text"
                    placeholder="请输入视频标题"
                    v-model="video.title"
                    maxlength="10"
                    show-word-limit>
          </el-input>
        </div>
        <div style="margin: 20px 0;"></div>
        <div>
          <h3 class="title_h3">描述</h3>
          <el-input class="text_box_b"
                    type="textarea"
                    placeholder="请输入视频描述"
                    v-model="video.description"
                    maxlength="100"
                    show-word-limit
          >
          </el-input>
        </div>
      </div>
      <!--  文本框结束    -->
      <!--   上传封面开始   -->
      <div>

        <h3>封面</h3>
        <el-upload
            class="upload-demo"
            action="#"
            list-type="picture-card"
            :multiple="false"
            accept=".jpg"
            :limit="1"
            :on-change="addFile"
            ref="files"
            :http-request="uploadCoverFile"
        >

          <i slot="default" class="el-icon-plus"></i>
          <div slot="file" slot-scope="{file}">
            <img
                class="el-upload-list__item-thumbnail"
                :src="videoImgUrl" alt=""
            >
            <span class="el-upload-list__item-actions">
        <span
            class="el-upload-list__item-preview"
            @click="handlePictureCardPreview(file)"
        >
          <i class="el-icon-zoom-in"></i>
        </span>
        <span
            v-if="!disabled"
            class="el-upload-list__item-delete"
            @click="handleDownload(file)"
        >
          <i class="el-icon-download"></i>
        </span>
        <span
            v-if="!disabled"
            class="el-upload-list__item-delete"
            @click="handleRemove(file)"
        >
          <i class="el-icon-delete"></i>
        </span>
      </span>
          </div>
          <div class="el-upload__img" slot="tip">请上传视频封面</div>
        </el-upload>

        <el-dialog :visible.sync="dialogVisible">
          <img width="100%" :src="videoImgUrl" alt="">
        </el-dialog>
      </div>
      <!--   上传封面结束   -->
      <!--  上传视频开始   -->
      <div>
        <h3>视频上传</h3>
        <el-upload
            class="upload-demo"
            drag
            action="#"
            accept=".mp4"
            :limit="1"
            :http-request="uploadVideoFile"
            multiple
            :file-list="fileList">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传mpe4文件，最少上传1920*1080且不超过500mb</div>
          <el-progress v-show="showProgressBar" :percentage="progress" :text-inside="true"></el-progress>
        </el-upload>
      </div>
      <!--   上传视频结束   -->
      <!--   视频分类开始   -->
      <div class="video_d">
        <h3>分类</h3>
        <template>
          <el-select v-model="video.type" placeholder="请选择视频分类">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </template>
      </div>
      <!--   视频分类开始   -->
      <!--  视频标签开始    -->
      <div>
        <h3>标签</h3>
        <el-select multiple collapse-tags v-model="video.tag" placeholder="请选择标签分类">
          <el-option
              v-for="item in tagList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="up-btn">
        <el-button type="primary" @click="uploadInformation">上传视频信息</el-button>
      </div>

      <!--  视频标签结束    -->


    </el-card>
  </div>
</template>

<script>
import {
  uploadVideo, // 上传视频
  uploadContent, // 提交视频信息
  uploadCover, // 上传封面
} from "@/utils/option"

export default {
  name: "contribute",
  data() {
    return {
      fileList: [],
      video: {
        userID: "", // 当前用户id
        title: "", // 标题
        description: "", // 描述
        videoImgUrl: "", // 视频封面
        videoSrcUrl: "",
        type: "", // 分类
        tag: [] // 标签
      },
      progress: 0,
      showProgressBar: false,
      /*文本框*/
      text: '',
      textarea: '',
      /*文本框*/
      /*视频分类*/
      options: [{
        value: '2',
        label: '电影'
      }, {
        value: '3',
        label: '电视剧'
      }, {
        value: '4',
        label: '综艺'
      }, {
        value: '5',
        label: '动漫'
      }, {
        value: '6',
        label: '自制'
      }, {
        value: '7',
        label: '搬运'
      }],
      tagList: [{
        value: '1',
        label: '鬼畜'
      }, {
        value: '2',
        label: '搞笑'
      }, {
        value: '3',
        label: '热血'
      }, {
        value: '4',
        label: '恐怖'
      }, {
        value: '5',
        label: '睡眠'
      }],
      value: '',
      /*视频分类*/
      /*上传封面*/
      // dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      /*上传封面*/
      videoImgUrl: "",
      tags: "",
      input: ''
    }
  },
  methods: {
    // 上传封面
    uploadCoverFile(param) {
      console.log(222)
      const file = param.file;
      let form = new FormData();
      form.append("file", file);
      uploadCover(form).then(res => {
        if (res.success) {
          this.video.videoImgUrl = res.data;
          console.log(this.video.videoImgUrl)
        }
      })
    },
    // 上传给后台管理视频信息
    uploadInformation() {
      console.log(this.video);
      if (this.video.title == "") {
        this.$notify.error({
          title: '',
          message: "请输入标题",
          position: 'bottom-right'
        });
        return;
      }
      if (this.video.description == "") {
        this.$notify.error({
          title: '',
          message: "请描述内容",
          position: 'bottom-right'
        });
        return;
      }
      if (this.video.videoImgUrl == "") {
        this.$notify.error({
          title: '',
          message: "请上传封面",
          position: 'bottom-right'
        });
        return;
      }
      if (this.video.videoSrcUrl == "") {
        this.$notify.error({
          title: '',
          message: "请上传视频",
          position: 'bottom-right'
        });
        return;
      }
      if (this.video.type == "") {
        this.$notify.error({
          title: '',
          message: "请选择分类",
          position: 'bottom-right'
        });
        return;
      }
      if (this.video.videoSrcUrl.length <= 0) {
        this.$notify.error({
          title: '',
          message: "请选择标签",
          position: 'bottom-right'
        });
        return;
      }

      uploadContent(this.video).then(res => {
        if (res.success) {
          this.$notify({
            title: '',
            message: "投稿成功，请等待审核",
            position: 'bottom-right',
            type: "success"
          });
        } else {
          this.$notify.error({
            title: '',
            message: res.message,
            position: 'bottom-right'
          });
        }
      })
    },
    // 上传视频
    uploadVideoFile(param) {
      this.showProgressBar = true;
      const file = param.file;
      let form = new FormData();
      form.append("file", file);
      uploadVideo(form, (progressEvent) => {
        // if (progressEvent.lengthComputable) {
        const progress = Math.round((progressEvent.loaded / progressEvent.total) * 100);
        this.progress = progress;
        console.log(this.progress)
        // }
      }).then(res => {
        if (res.success) {
          // this.$notify({
          //   title: '上传成功',
          //   message: res.message,
          //   position: 'bottom-right',
          //   type: "success"
          // });
          console.log(this.progress)
          this.video.userID = res.data.userID;
          this.video.videoSrcUrl = res.data.videoSrcUrl;
        } else {
          this.$notify.error({
            title: '',
            message: res.message,
            position: 'bottom-right'
          });

          location.reload();
        }
      })
    },
    /*上传封面*/
    // 删除封面
    handleRemove(file) {
      this.$refs.files.handleRemove(file);
      this.videoImgUrl = ""
      // 需要和后端对接进行删除
    },
    addFile(file) {
      // 当上传之后 获取上传的路径
      this.videoImgUrl = file.url;
    },
    handlePictureCardPreview(file) {
      this.videoImgUrl = file.url;
      this.dialogVisible = true;
    },
    handleDownload(file) {
      console.log(file);
    },
    /*上传封面*/

  },

  created() {

  }
}
</script>

<style scoped>
.text_box_a {
  width: 500px;
}

.text_box_b {
  width: 600px;

}

.upload-demo {
  margin: 20px 2px;
}

.div1 {
  /*margin-left: 200px;*/
  margin-top: 100px;
}

.video_d {
  margin: 0;
  padding: 0;
}

h3, .up-btn {
  margin: 10px 0;
}

</style>