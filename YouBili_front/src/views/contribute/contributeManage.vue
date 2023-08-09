<!--投稿管理界面-->
<template>
  <div class="div1">
    <h1>投稿管理</h1>

    <el-card>
      <template>
        <el-table
            :data="tableData"
            style="width: 100%">

          <el-table-column
              prop="cover"
              label="封面"
              width="120">
            <img src="logo1.png" width="50%">
          </el-table-column>
          <el-table-column
              prop="title"
              label="标题"
              width="200">
          </el-table-column>
          <el-table-column
              prop="type"
              label="类型"
              width="80">
          </el-table-column>
          <el-table-column
              prop="address"
              label="摘要"
              width="400">
          </el-table-column>

          <el-table-column
              prop="pv"
              label="浏览量"
              width="80">
          </el-table-column>
          <el-table-column
              prop="rv"
              label="回复量"
              width="80">
          </el-table-column>

          <el-table-column
              prop="rp"
              label="审核状态"
              width="100">
          </el-table-column>

          <el-table-column
              prop="up"
              label="上传进度"
              width="100">
            <el-progress :text-inside="true" :stroke-width="22" :percentage="80" status="warning"></el-progress>
          </el-table-column>

          <el-table-column
              prop="date"
              label="创建日期"
              width="180">
          </el-table-column>

        </el-table>
      </template>
    </el-card>
  </div>
</template>

<script>

export default {

  name: "contributeManage",
  data() {

    return {
      tableData: [{
        cover: '',
        title: '测试',
        date: '2016-05-02',
        name: '王小虎',
        address: '6月15日北京工人体育场，阿根廷VS澳大利亚的比赛出现意外，有球迷翻下看台冲进场拥抱梅西，随后被安保人员抬走。当事人回应冲进比赛球场拥抱梅西，“对不起梅西 ，我实在太想见你，我抢票实在抢不着，就以这个方式，实在不好意思，本来想要一个签名。我这个行为其实不太好，我希望以我个人的惩罚为起点，希望以后能多增加点安保。”',
        type: '足球',
        pv: '1',
        rv: '3',
        rp: '正在审核'
      }, {
        cover: '',
        title: '测试',
        date: '2016-05-02',
        name: '王小虎',
        address: '近日召开的二十届中央财经委员会第一次会议强调，“着力提高人口整体素质”“要加强人力资源开发利用，稳定劳动参与率，提高人力资源利用效率”“加快发展多层次、多支柱养老保险体系”。围绕会议相关部署要求，记者采访了人力资源社会保障部相关司局负责人。',
        type: '新闻',
        pv: '1',
        rv: '3',
        rp: '正在审核'
      }, {
        cover: '',
        title: '测试',
        date: '2016-05-02',
        name: '王小虎',
        address: '性转一下，新闻回顾：\n' +
            '\n' +
            '2021年5月11日，成都一位女网友在某小吃街被一名陌生男子泼奶茶。事发后这个女孩第一时间报了警。\n' +
            '\n' +
            '警方非常给力，5月13日就把人抓到了，根据女孩回复，作案人已经被顶格拘留。',
        type: '热点',
        pv: '1',
        rv: '3',
        rp: '正在审核'
      }, {
        cover: '',
        title: '测试',
        date: '2016-05-02',
        name: '王小虎',
        address: '彭清林站着就害怕，犹豫再三还是跳了下去\n' +
            '\n' +
            '这就是真正的英雄\n' +
            '\n' +
            '\n' +
            '\n' +
            '那些眼红的说变味的\n' +
            '\n' +
            '别人拿命换的，车祸死亡30岁赔偿7、80万\n' +
            '\n' +
            '现在奖励这个数不少，但也不算多',
        type: '热点',
        pv: '1',
        rv: '3',
        rp: '正在审核'
      }]
    }
  },
  methods: {},
  created() {

  }
}
</script>

<style scoped>
.div1 {
  margin-left: 200px;
  margin-top: 100px;
}

</style>