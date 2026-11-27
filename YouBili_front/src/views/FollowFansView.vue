<template>
  <div class="max-w-4xl mx-auto space-y-6">
    <div class="yb-card p-5 flex flex-wrap items-center justify-between gap-4">
      <div class="flex items-center gap-2">
        <button
          type="button"
          class="px-5 py-2 rounded-full text-sm font-semibold transition-all cursor-pointer"
          :style="activeTab === 'follow'
            ? { backgroundColor: 'var(--yb-brand)', color: '#fff' }
            : { backgroundColor: 'var(--yb-surface-muted)', color: 'var(--yb-text-secondary)' }"
          @click="switchTab('follow')"
        >
          我的关注 ({{ followList.length }})
        </button>
        <button
          type="button"
          class="px-5 py-2 rounded-full text-sm font-semibold transition-all cursor-pointer"
          :style="activeTab === 'fans'
            ? { backgroundColor: 'var(--yb-brand)', color: '#fff' }
            : { backgroundColor: 'var(--yb-surface-muted)', color: 'var(--yb-text-secondary)' }"
          @click="switchTab('fans')"
        >
          我的粉丝 ({{ fansList.length }})
        </button>
      </div>
      <el-button round plain :loading="loading" @click="loadAll">刷新</el-button>
    </div>

    <div v-if="loading" class="space-y-3">
      <div v-for="n in 4" :key="n" class="yb-card p-4">
        <el-skeleton :rows="1" animated />
      </div>
    </div>

    <el-empty
      v-else-if="currentList.length === 0"
      :description="activeTab === 'follow' ? '还没有关注任何 UP 主' : '还没有粉丝关注你'"
    />

    <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div
        v-for="person in currentList"
        :key="person.userId"
        class="yb-card p-4 flex items-center justify-between gap-4 hover:border-[var(--yb-brand)] transition-colors"
      >
        <div
          class="flex items-center gap-3.5 min-w-0 cursor-pointer"
          @click="router.push(`/space/${person.userId}`)"
        >
          <el-avatar :size="52" :src="person.avatarUrl || FALLBACK_AVATAR" class="shrink-0" />
          <div class="min-w-0">
            <div class="flex items-center gap-2">
              <span class="font-bold text-base truncate hover:text-[var(--yb-brand)]">
                {{ person.nickname }}
              </span>
              <el-tag v-if="person.mutualFollow" size="small" type="success" effect="plain" round>
                互相关注
              </el-tag>
            </div>
            <div class="text-xs mt-1" style="color: var(--yb-text-muted);">
              粉丝 {{ formatCount(person.fansCount) }} · 关注 {{ formatCount(person.followCount) }}
            </div>
          </div>
        </div>

        <div class="shrink-0 flex items-center gap-2">
          <template v-if="activeTab === 'follow'">
            <el-button round size="small" plain type="danger" @click="handleUnfollow(person)">
              取消关注
            </el-button>
          </template>
          <template v-else>
            <el-button
              v-if="!person.mutualFollow"
              round
              size="small"
              type="primary"
              @click="handleFollowBack(person)"
            >
              + 回关
            </el-button>
            <el-button v-else round size="small" plain @click="handleUnfollow(person)">
              已互关
            </el-button>
            <el-button
              round
              size="small"
              text
              type="danger"
              @click="handleRemoveFan(person)"
            >
              移除
            </el-button>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { userApi } from '@/api/modules'
import { useAuthStore } from '@/stores/auth'
import type { FollowUser } from '@/types/api'
import { FALLBACK_AVATAR, formatCount } from '@/utils/format'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const activeTab = ref<'follow' | 'fans'>(route.path === '/fans' ? 'fans' : 'follow')
const loading = ref(true)
const followList = ref<FollowUser[]>([])
const fansList = ref<FollowUser[]>([])

const currentList = computed(() => (activeTab.value === 'follow' ? followList.value : fansList.value))

function switchTab(tab: 'follow' | 'fans') {
  activeTab.value = tab
  router.replace(tab === 'fans' ? '/fans' : '/follow')
}

async function loadAll() {
  loading.value = true
  try {
    const [follows, fans] = await Promise.all([userApi.follows(), userApi.fans()])
    followList.value = follows
    fansList.value = fans
  } finally {
    loading.value = false
  }
}

async function handleFollowBack(person: FollowUser) {
  await userApi.follow(person.userId)
  ElMessage.success(`已关注 ${person.nickname}`)
  await Promise.all([loadAll(), authStore.refreshProfile()])
}

async function handleUnfollow(person: FollowUser) {
  try {
    await ElMessageBox.confirm(`确定取消关注「${person.nickname}」吗？`, '取消关注', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await userApi.unfollow(person.userId)
    ElMessage.success('已取消关注')
    await Promise.all([loadAll(), authStore.refreshProfile()])
  } catch {
    // 取消
  }
}

async function handleRemoveFan(person: FollowUser) {
  try {
    await ElMessageBox.confirm(`确定移除粉丝「${person.nickname}」吗？`, '移除粉丝', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await userApi.removeFan(person.userId)
    ElMessage.success('已移除该粉丝')
    await Promise.all([loadAll(), authStore.refreshProfile()])
  } catch {
    // 取消
  }
}

watch(
  () => route.path,
  (path) => {
    activeTab.value = path === '/fans' ? 'fans' : 'follow'
  }
)

onMounted(loadAll)
</script>
