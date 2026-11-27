<template>
  <div class="space-y-6">
    <div class="yb-card p-5 flex flex-wrap items-center justify-between gap-4">
      <div>
        <h1 class="text-xl font-bold">我的订单</h1>
        <p class="text-xs mt-0.5" style="color: var(--yb-text-muted);">仅展示当前登录账号的个人会员订单</p>
      </div>
      <div class="flex items-center gap-3">
        <el-input
          v-model="search"
          placeholder="搜索商品名称或订单号"
          clearable
          prefix-icon="Search"
          class="!w-60"
        />
        <el-button type="primary" round @click="router.push('/vip')">选购大会员</el-button>
      </div>
    </div>

    <div class="yb-card p-4 overflow-x-auto">
      <el-table v-loading="loading" :data="filteredOrders" style="width: 100%" empty-text="暂无订单记录">
        <el-table-column prop="orderNumber" label="商户订单号" min-width="210" />
        <el-table-column prop="tradeName" label="商品名称" min-width="170" />
        <el-table-column label="金额" width="110">
          <template #default="{ row }">
            <span class="font-bold" style="color: var(--yb-brand);">¥{{ Number(row.price).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payment" label="支付方式" width="100" />
        <el-table-column prop="zfbNumber" label="支付宝流水号" min-width="180">
          <template #default="{ row }">{{ row.zfbNumber || '-' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.paid ? 'success' : 'warning'" size="small" round>
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="170">
          <template #default="{ row }">{{ row.gmtCreate || row.payTime || '-' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="!row.paid"
              type="primary"
              size="small"
              round
              :loading="payingOrder === row.orderNumber"
              @click="handlePay(row as OrderItem)"
            >
              去支付
            </el-button>
            <el-button
              v-if="!row.paid"
              type="danger"
              text
              size="small"
              @click="handleDelete(row as OrderItem)"
            >
              删除
            </el-button>
            <span v-else class="text-xs" style="color: var(--yb-text-muted);">已完成</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { orderApi } from '@/api/modules'
import type { OrderItem } from '@/types/api'

const router = useRouter()

const loading = ref(true)
const orders = ref<OrderItem[]>([])
const search = ref('')
const payingOrder = ref('')

const filteredOrders = computed(() => {
  const q = search.value.trim().toLowerCase()
  if (!q) return orders.value
  return orders.value.filter(
    (o) => o.tradeName.toLowerCase().includes(q) || o.orderNumber.toLowerCase().includes(q)
  )
})

async function loadOrders() {
  loading.value = true
  try {
    orders.value = await orderApi.myOrders()
  } finally {
    loading.value = false
  }
}

async function handlePay(order: OrderItem) {
  payingOrder.value = order.orderNumber
  try {
    const { formHtml } = await orderApi.payOrder(order.orderNumber)
    const payWindow = window.open('', '_blank')
    if (payWindow) {
      payWindow.document.open()
      payWindow.document.write(formHtml)
      payWindow.document.close()
    } else {
      // 浏览器拦截弹窗时在当前页提交表单
      const wrapper = document.createElement('div')
      wrapper.innerHTML = formHtml
      document.body.appendChild(wrapper)
      const formEl = wrapper.querySelector('form')
      formEl?.submit()
    }
    ElMessage.success('正在跳转至支付宝收银台...')
  } finally {
    payingOrder.value = ''
  }
}

async function handleDelete(order: OrderItem) {
  try {
    await ElMessageBox.confirm(`确定删除未支付订单「${order.tradeName}」吗？`, '删除订单', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await orderApi.deleteOrder(order.orderId)
    orders.value = orders.value.filter((o) => o.orderId !== order.orderId)
    ElMessage.success('订单已删除')
  } catch {
    // 取消
  }
}

onMounted(loadOrders)
</script>
