<!--通知-->
<template>
  <div class="idxs">
    <div style="height:1000px;margin-right: 500px;margin-left: 0px /* 设置容器高度为视口高度 */;overflow: hidden">
      <div style="width: 1400px;height: 1000px;position: absolute;">
        <div>
          <el-page-header @back="goBack"></el-page-header>
        </div>
        <el-button @click="tableNotice1" type="primary" style="margin-left: 30px; margin-top: 30px">
          公告
        </el-button>
        <br>

        <!--        <el-card class="info_c">-->
        <!--让编辑按钮处于一行的右侧-->
        <div style="display: flex; justify-content: space-between;">
          <h2 class="title_h">个人消息</h2>
        </div>
        <!--分隔行-->
        <el-divider content-position="right"></el-divider>

        <!--表格组件-->
        <el-table
            ref="multipleTable"
            :data="tableData"
            tooltip-effect="dark"
            style="width: 100%"
        >
          <el-table-column class="aaa"
                           type="selection"
                           width="55">
          </el-table-column>
          <!--          <el-table-column class="aaa"-->
          <!--                           property="noticeTime"-->
          <!--                           label="日期"-->
          <!--                           width="450">-->
          <!--            <template slot-scope="scope">{{ scope.row.date }}</template>-->
          <!--          </el-table-column>-->
          <el-table-column class="aaa"
                           property="noticeText"
                           label="内容"
                           show-overflow-tooltip>
          </el-table-column>
          <el-table-column class="aaa"
                           label="删除">
            <template slot-scope="scope">
              <el-button class="delete_b" type="primary" icon="el-icon-delete"
                         @click="notifyDelete(scope.row)">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-drawer
            title="我是标题"
            :visible.sync="tableNotice"
            :label="direction"
            :with-header="false">
          <el-table :data="getNotice">
            <el-table-column property="noticeTime" label="日期" width="150" style="width: 200px;"></el-table-column>
            <el-table-column property="noticeText" label="内容" style="right: 100px"></el-table-column>
          </el-table>
          <div style="display: flex">
            <div v-for="v in notifyData">
              <el-card>
                {{ v.noticeText }}
              </el-card>
            </div>
          </div>
        </el-drawer>
        <el-pagination
            class="page_p"
            background
            layout="prev, pager, next"
            :total="1000">

        </el-pagination>

        <!--        </el-card>-->
      </div>
    </div>
  </div>
</template>

<script>
import {getNotice, postNoticeDelete} from "@/utils/option";

export default {
  name: "notify",
  data() {
    return {
      size: "200",
      scope: '',
      avatarUrl: "",
      tableData: [],
      multipleSelection: [],
      tableNotice: false,
      getNotice: {
        noticeTime: '',
        noticeText: ''
      },
      direction: 'btt',
      notifyData: {
        noticeTime: '',
        noticeText: ''
      },
      deleteByUserId: {
        noticeID: '',
        userID: ''
      }

    }
  },
  methods: {
    tableNotice1() {
      this.tableNotice = true;
      getNotice({enable: 0}).then(res => {
        if (res.success) {
          this.getNotice = res.data;
          console.log("111111111111111" + res.data)
        } else {
          this.$notify.error({
            position: 'bottom-right',
            title: res.message
          })
        }
      })
    },
    goBack() {
    },
    notifyDelete(row) {
      console.log(row.noticeID)
      postNoticeDelete({noticeID: row.noticeID}).then(res => {
        if (res.success) {
          this.$notify.success({
            message: '删除成功！',
            title: '成功',
            position: 'bottom-right',

          });
        } else {
          let title = '操作失败';
          this.$notify.error({
            title: title,
            text: res.message,
            position: 'bottom-right'
          });
        }
      })
    }
  },

  handleSelectionChange() {
    // this.deleate = true;
  },

  mounted() {
    getNotice({enable: 1}).then(res => {
      console.log(res.data);
      if (res.success) {
        this.tableData = JSON.parse(JSON.stringify(res.data));
        console.log("111111111111111" + this.tableData)
      } else {
        this.$notify.error({
          position: 'bottom-right',
          title: res.message
        })
      }
    })
  },
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
    width: calc(100% - 290px);
    max-width: 1570px;
    min-width: 1200px;
    margin: 60px auto;
  }

  .idxs {
    margin-left: 0px;
  }
}


.info_c {
  height: 80%;
  width: 100%;
  justify-content: center;
  margin-left: 100px;
}

.title_h {
  margin-left: 30px;
  margin-top: 20px;
  color: deeppink;
}

.delete_b {
  background-color: indianred;
  border: 0;
  color: white;
}

.page_p {
  margin-left: 450px;
}
</style>