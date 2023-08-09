<!--举报-->
<template>
  <div class="idxs">
    <div class="idxs">

      <div style="width: 1000px;height: 600px;margin-top: 20px;position: absolute">
        <el-card>
          <h1 style="margin-bottom: 50px;text-align: center;color: #303133;font-family: 'Microsoft YaHei', sans-serif;">
            违规举报</h1>
          <el-divider></el-divider>
          <p>视频标题/用户id:</p>
          <el-input style="margin-top: 10px"
                    placeholder=""
                    v-model="input"
                    :disabled="true">
          </el-input>
          <br>
          <el-form style="margin-top: 40px" size="medium" label-position="left" ref="form" :model="form"
                   label-width="80px">
            <el-form-item label="违规描述">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item label="违规性质" @click="clear()">
              <el-select v-model="form.region" placeholder="请选择违规性质" @change="change">
                <el-option label="仇恨言论" value="1"></el-option>
                <el-option label="暴力黄色" value="2"></el-option>
                <el-option label="赌博/毒品" value="3"></el-option>
                <el-option label="其它" value="4"></el-option>
              </el-select>
            </el-form-item>


            <el-form-item v-if="flag==1" label="违规类型">
              <el-checkbox-group v-model="form.type">
                <el-checkbox label="违规类型1" name="type"></el-checkbox>
                <el-checkbox label="违规类型2" name="type"></el-checkbox>
                <el-checkbox label="违规类型3" name="type"></el-checkbox>
                <el-checkbox label="违规类型4" name="type"></el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item v-else-if="flag==2" label="违规类型">
              <el-checkbox-group v-model="form.type">
                <el-checkbox label="违规类型5" name="type"></el-checkbox>
                <el-checkbox label="违规类型6" name="type"></el-checkbox>
                <el-checkbox label="违规类型7" name="type"></el-checkbox>
                <el-checkbox label="违规类型8" name="type"></el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item v-else-if="flag==3" label="违规类型">
              <el-checkbox-group v-model="form.type">
                <el-checkbox label="违规类型9" name="type"></el-checkbox>
                <el-checkbox label="违规类型10" name="type"></el-checkbox>
                <el-checkbox label="违规类型11" name="type"></el-checkbox>
                <el-checkbox label="违规类型12" name="type"></el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item v-else-if="flag==4" label="违规类型">
              <el-checkbox-group v-model="form.type">
                <el-checkbox label="违规类型13" name="type"></el-checkbox>
                <el-checkbox label="违规类型14" name="type"></el-checkbox>
                <el-checkbox label="违规类型15" name="type"></el-checkbox>
                <el-checkbox label="违规类型16" name="type"></el-checkbox>
              </el-checkbox-group>
            </el-form-item>


            <el-form-item label="违规内容">
              <el-radio-group v-model="form.resource">
                <el-radio label="视频资源"></el-radio>
                <el-radio label="言论"></el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="留言    ">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit">提交举报</el-button>
              <el-button>取消</el-button>


            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </div>

  </div>
</template>

<script>
import Sidebar from "@/components/sidebar.vue";
import Handder from "@/components/Handder.vue";

export default {
  name: "report",
  components: {Handder, Sidebar},
  data() {
    return {
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
      }, flag: "1", input: "视频标题/用户名: "

    }
  },
  methods: {
    onSubmit() {
      console.log('submit!');
    },
    change(key) {
      this.flag = key
    }
  },
  created() {

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
    margin-left: 100px;
  }
}

.el-form-item {
  margin-bottom: 50px;
}
</style>