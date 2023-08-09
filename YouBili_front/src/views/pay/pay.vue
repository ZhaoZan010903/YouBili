<!--支付界面-->
<template>
  <div>
    <div class="idxs">
      <div class="wai_d">
        <div class="nei_d">
          <div class="h">
            <h2>会员购买</h2>
          </div>
          <div class="head_div">
            <div>
              <div class="title_d">
                <h2>YouBili</h2>
                <el-tag type="info">待付款</el-tag>
              </div>
              <p style="color: red">{{ tradeName }}</p>
              <p>官方网站: YouBili.com</p>
              <p>官方邮箱: 18340529872@163.com</p>
              <p>最终解释权归 YouBili 所有，若订单出现争议，请及时联系在线客服咨询处理</p>
            </div>


            <div class="content_d">
              <div>
                <h4>注意事项:</h4>
                <p class="content_p">1、该商品购买后不支持退款</p>
                <p class="content_p">2、存在生效服务的情况下新购服务不会立即生效</p>
                <p>3、已购买的服务可到订单中心进行查看</p>
                <p>4、若用于非法犯罪行为,官方有权停止提供服务</p>
                <p>详细服务条款请查阅《YouBili 服务使用协议》</p>
              </div>
              <div>
                <h4>享受权益:</h4>
                <p>美剧免费观看</p>
                <p>会员专属客服</p>
                <p>会员弹幕字体颜色</p>
                <p>电影无限播放</p>
              </div>
            </div>


            <div class="pay_d">
              <div>
                <el-select v-model="value" placeholder="支付方式">
                  <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="price_d">
                <p>订单金额:{{ price }}</p>
              </div>
            </div>
          </div>

        </div>

        <div class="card_d">
          <el-card class="box-card">
            <div class="text_item">
              <el-button class="pay_b" type="info" plain disabled>下载订单</el-button>
              <el-button class="pay_b" type="info" plain disabled>打印订单</el-button>
              <el-button class="pay_b" type="info">取消订单</el-button>
              <el-button class="pay_b" type="primary" @click="handlePay(price,options.label)">加入订单</el-button>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Handder from "@/components/Handder";
import Sidebar from "@/components/sidebar";
import {PayForOrders} from "@/utils/option";

export default {
  name: "pay",
  components: {Handder, Sidebar},
  data() {
    return {
      tradeName: "vip",
      price: 79.99,
      options: [{
        value: '支付宝',
        label: '支付宝'
      }],
      value: ''
    }
  },

  methods: {
    handlePay() {
      if (this.value == null || this.value == "") {
        this.$notify.error({
          title: '错误',
          message: '请选择支付方式',
          position: 'bottom-right'
        });
        return;
      }
      PayForOrders({tradeName: this.tradeName, price: this.price, payment: this.value}).then(res => {

        if (res.success) {
          this.$notify.success({
            title: '成功',
            message: '加入订单成功,请去往订单页面完成支付!',
            position: 'bottom-right'
          })
        }
      })
    }
  },
  cancelOrder() {

  },
  created() {

  }
}
</script>

<style scoped>
.wai_d {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.nei_d {
  width: 75%;
}

.h {
  width: 100%;
  margin-top: 2%;
  margin-left: 2%;
  color: red;
}

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

.head_div {
  background-color: white;
  border: 1px solid #ccc;
  border-bottom: none;
  width: 100%;
  margin-left: 2%;
  margin-top: 2%;
}

.card_d {
  width: 20%;
  margin-top: 5%;
}

.head_div > div {
  border-bottom: 1px solid #ccc;
  box-sizing: border-box;
  padding: 15px;

}

.title_d {
  display: flex;
  justify-content: space-between;
}

.content_d {
  display: flex;
  justify-content: space-between;
}

.content_d > div {
  width: 40%;
}

.pay_d {
  display: flex;
  justify-content: space-between;
}

.head_div p {
  margin: 5px 0;
}

.price_d {
  margin-right: 150px;
}

.text_item {
  font-size: 14px;
  padding: 18px 0;
}

.pay_b {
  width: 100%;
  margin: 12px auto;
  padding: 10px;
}

.content_p {
  color: red;
}

</style>