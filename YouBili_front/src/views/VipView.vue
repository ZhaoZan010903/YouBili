<template>
  <div class="max-w-5xl mx-auto space-y-8">
    <!-- Hero Banner -->
    <section
      class="rounded-3xl p-8 text-white relative overflow-hidden shadow-lg"
      style="background: linear-gradient(135deg, #1e1b4b 0%, #701a75 45%, #fb7299 100%);"
    >
      <div class="relative z-10 flex flex-wrap items-center justify-between gap-4">
        <div class="space-y-2">
          <div class="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold bg-white/15 backdrop-blur-md">
            <el-icon><Trophy /></el-icon>
            YouBili 大会员 · 尊享视听体验
          </div>
          <h1 class="text-3xl sm:text-4xl font-extrabold tracking-tight">解锁全站高清画质与专属弹幕特权</h1>
          <p class="text-sm text-white/80 max-w-xl">
            所有套餐价格由服务端安全校验，支持支付宝沙箱快捷支付，开通后即时激活会员身份标识。
          </p>
        </div>

        <div v-if="authStore.isLoggedIn" class="px-5 py-3 rounded-2xl bg-white/15 backdrop-blur-md text-sm">
          <div class="text-white/75 text-xs">当前会员状态</div>
          <div class="font-bold text-base mt-0.5">
            {{ authStore.isVip ? '已开通 YouBili 大会员' : '普通用户（未开通）' }}
          </div>
        </div>
      </div>
    </section>

    <!-- Plans Grid -->
    <section v-loading="loading" class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div
        v-for="(plan, idx) in plans"
        :key="plan.code"
        class="yb-card p-6 flex flex-col justify-between relative transition-all cursor-pointer"
        :style="selectedPlanCode === plan.code
          ? { borderColor: 'var(--yb-brand)', boxShadow: '0 0 0 2px var(--yb-brand-soft)' }
          : {}"
        @click="selectedPlanCode = plan.code"
      >
        <span
          v-if="idx === 2"
          class="absolute -top-3 right-5 px-3 py-0.5 rounded-full text-xs font-bold text-white"
          style="background: linear-gradient(135deg, #fb7299, #ff9966);"
        >
          超值推荐
        </span>

        <div>
          <h2 class="text-lg font-bold">{{ plan.name }}</h2>
          <p class="text-xs mt-1" style="color: var(--yb-text-muted);">有效期 {{ plan.durationDays }} 天</p>

          <div class="my-5 flex items-baseline gap-1">
            <span class="text-sm font-bold" style="color: var(--yb-brand);">¥</span>
            <span class="text-4xl font-black tracking-tight" style="color: var(--yb-brand);">
              {{ Number(plan.price).toFixed(2) }}
            </span>
          </div>

          <ul class="space-y-2.5 text-sm" style="color: var(--yb-text-secondary);">
            <li v-for="priv in plan.privileges" :key="priv" class="flex items-center gap-2">
              <el-icon style="color: var(--yb-brand);"><CircleCheckFilled /></el-icon>
              <span>{{ priv }}</span>
            </li>
          </ul>
        </div>

        <el-button
          :type="selectedPlanCode === plan.code ? 'primary' : 'default'"
          round
          size="large"
          class="w-full mt-6 !font-semibold"
          :loading="creating && selectedPlanCode === plan.code"
          @click.stop="createVipOrder(plan.code)"
        >
          立即开通
        </el-button>
      </div>
    </section>

    <!-- Notice -->
    <section class="yb-card p-5 text-xs space-y-1.5" style="color: var(--yb-text-muted);">
      <div class="font-semibold text-sm mb-2" style="color: var(--yb-text);">购买须知</div>
      <p>1. 虚拟会员商品一经支付成功不支持退款，请在下单前确认套餐周期。</p>
      <p>2. 创建订单后可在「我的订单」页面发起支付宝沙箱支付或取消未支付订单。</p>
      <p>3. 支付回调验证通过后，系统自动将当前账号升级为 Active 大会员。</p>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { orderApi } from '@/api/modules'
import { useAuthStore } from '@/stores/auth'
import type { VipPlan } from '@/types/api'

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(true)
const creating = ref(false)
const plans = ref<VipPlan[]>([])
const selectedPlanCode = ref('VIP_YEAR')

async function loadPlans() {
  loading.value = true
  try {
    plans.value = await orderApi.plans()
    if (plans.value.length > 0 && !plans.value.some((p) => p.code === selectedPlanCode.value)) {
      selectedPlanCode.value = plans.value[0].code
    }
  } finally {
    loading.value = false
  }
}

async function createVipOrder(planCode: string) {
  if (!authStore.isLoggedIn) {
    authStore.openAuthModal('login')
    return
  }
  selectedPlanCode.value = planCode
  creating.value = true
  try {
    await orderApi.createOrder(planCode, '支付宝')
    ElMessage.success('订单已创建，即将前往订单中心完成支付')
    router.push('/orders')
  } finally {
    creating.value = false
  }
}

onMounted(loadPlans)
</script>
