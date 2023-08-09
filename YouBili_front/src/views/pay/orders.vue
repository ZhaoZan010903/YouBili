<!--支付界面-->
<template>
  <div>
    <div class="idxs">
      <el-table
          :data="tableData.filter(data => !search || data.tradeName.toLowerCase().includes(search.toLowerCase()))"
          style="width: 100%">
        <el-table-column
            label="商品号"
            prop="orderID">
        </el-table-column>
        <el-table-column
            label="商品名称"
            prop="tradeName">
        </el-table-column>
        <el-table-column
            label="订单号"
            prop="orderNumber">
        </el-table-column>
        <el-table-column
            label="支付宝订单号"
            prop="zfbNumber">
        </el-table-column>
        <el-table-column
            label="支付方式"
            prop="payment">
        </el-table-column>
        <el-table-column
            label="金额"
            prop="price">
        </el-table-column>
        <el-table-column
            label="购买时间"
            prop="payTime">
        </el-table-column>
        <el-table-column
            label="订单状态"
            prop="enable">
        </el-table-column>
        <el-table-column
            align="right">
          <template slot="header" slot-scope="scope">
            <el-input
                v-model="search"
                size="mini"
                placeholder="输入关键字搜索"/>
          </template>
          <el-table-column label="操作">
            <template slot-scope="scope">

              <el-button type="primary" size="mini" @click="buy(scope.row)" v-if="!scope.row.status">支付</el-button>
              <el-button type="primary" size="mini" @click="buy(scope.row)" v-else disabled>支付</el-button>

              <el-button type="danger" icon="el-icon-delete" size="mini"
                         @click="openDeleteConfirm(scope.row)"></el-button>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import Handder from "@/components/Handder";
import Sidebar from "@/components/sidebar";
import {
  handleDeleteOrders,
  loadOrdersList,
  BuyOrders
} from "@/utils/option";

export default {
  name: "orders",
  components: {Handder, Sidebar},
  data() {
    return {
      tableData: [],
      search: '',
      status: ''
    }
  },
  methods: {
    //加载订单列表
    loadOrdersListMethod() {
      loadOrdersList().then(res => {
        if (res.success) {
          this.tableData = res.data;

          this.status = res.data.status
        }
      })
    },
    cancel() {

    },

    buy(row) {
      console.log(row.tradeName)
      console.log(row.orderNumber)
      console.log(row.price)
      window.open("http://localhost:10001/alipay/pay?subject=" + row.tradeName + "&out_trade_no=" + row.orderNumber + "&total_amount=" + row.price)
      this.$notify.success({
        title: '成功',
        message: '支付宝请求成功',
        position: 'bottom-right'
      })
    },


    // //支付功能
    // buy(row){
    //   BuyOrders().then(res=>{
    //       let url = "http://localhost:8089/alipay/pay?subject="+row.tradeName+"&tradeNo="+row.orderNumber+"&totalAmount="+row.price
    //       window.open(url)
    //       this.$message.success("支付宝请求成功!")
    //   })
    // },

// 弹出删除确认框
    openDeleteConfirm(tableData) {
      alert(tableData.status)
      let message = '此操作将永久删除【' + tableData.tradeName + '】订单，是否继续？';
      this.$confirm(message, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.handleDeleteDetails(tableData);
      }).catch(() => {
      });
    },
    // 执行删除
    handleDeleteDetails(tableData) {
      handleDeleteOrders(tableData).then(res => {
        if (res.success) {
          this.$notify.success({
            message: '删除标签成功！',
            title: '成功',
            position: 'bottom-right',

          });
          this.loadOrdersListMethod();
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
  created() {
    //加载订单列表
    this.loadOrdersListMethod()
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
    margin-left: 50px;
  }
}


</style>