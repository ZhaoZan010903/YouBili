<template>
  <div class="max-w-5xl mx-auto space-y-6">
    <div class="yb-card p-5 flex flex-wrap items-center justify-between gap-4">
      <div>
        <h1 class="text-xl font-bold">观看历史</h1>
        <p class="text-xs mt-0.5" style="color: var(--yb-text-muted);">最近观看过的 {{ list.length }} 条记录</p>
      </div>
      <div class="flex flex-wrap items-center gap-2">
        <el-input
          v-model="keyword"
          placeholder="搜索历史记录"
          clearable
          prefix-icon="Search"
          class="!w-56"
          @keyup.enter="loadList"
          @clear="loadList"
        />
        <el-button type="primary" plain @click="loadList">搜索</el-button>
        <el-button type="danger" plain :disabled="list.length === 0" @click="clearAll">
          清空历史
        </el-button>
      </div>
    </div>

    <div v-if="loading" class="space-y-4">
      <div v-for="n in 4" :key="n" class="yb-card p-4">
        <el-skeleton :rows="2" animated />
      </div>
    </div>

    <el-empty v-else-if="list.length === 0" description="暂无观看历史记录">
      <el-button type="primary" round @click="router.push('/index')">去首页看看</el-button>
    </el-empty>

    <div v-else class="space-y-3">
      <article
        v-for="item in list"
        :key="item.historyId"
        class="yb-card p-4 flex flex-col sm:flex-row gap-4 items-center hover:border-[var(--yb-brand)] transition-colors cursor-pointer group"
        @click="router.push(`/video/${item.videoId}`)"
      >
        <div class="relative w-full sm:w-52 aspect-video rounded-xl overflow-hidden shrink-0 bg-black/10">
          <img
            :src="item.videoImgUrl || FALLBACK_COVER"
            :alt="item.title"
            class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
          />
          <span class="absolute bottom-1.5 right-1.5 px-1.5 py-0.5 rounded bg-black/70 text-white font-mono text-[11px]">
            {{ formatDuration(item.duration) }}
          </span>
        </div>

        <div class="flex-1 min-w-0 w-full space-y-2">
          <h3 class="font-bold text-base line-clamp-2 group-hover:text-[var(--yb-brand)] transition-colors">
            {{ item.title }}
          </h3>
          <div class="flex flex-wrap items-center gap-3 text-xs" style="color: var(--yb-text-muted);">
            <div
              class="flex items-center gap-1.5 hover:text-[var(--yb-brand)]"
              @click.stop="router.push(`/space/${item.authorId}`)"
            >
              <el-avatar :size="22" :src="item.authorAvatarUrl || FALLBACK_AVATAR" />
              <span>{{ item.authorNickname }}</span>
            </div>
            <span>观看时间：{{ formatRelativeTime(item.watchDate) }}</span>
            <span v-if="item.watchDuration">看到 {{ formatDuration(item.watchDuration) }}</span>
          </div>
        </div>

        <el-button
          type="danger"
          icon="Delete"
          circle
          plain
          title="删除此条记录"
          @click.stop="deleteOne(item.historyId)"
        />
      </article>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { interactionApi } from '@/api/modules'
import type { HistoryItem } from '@/types/api'
import { FALLBACK_AVATAR, FALLBACK_COVER, formatDuration, formatRelativeTime } from '@/utils/format'

const router = useRouter()
const loading = ref(true)
const keyword = ref('')
const list = ref<HistoryItem[]>([])

async function loadList() {
  loading.value = true
  try {
    list.value = await interactionApi.listHistory(keyword.value.trim() || undefined)
  } finally {
    loading.value = false
  }
}

async function deleteOne(historyId: number) {
  await interactionApi.deleteHistory(historyId)
  list.value = list.value.filter((h) => h.historyId !== historyId)
  ElMessage.success('已删除该条历史记录')
}

async function clearAll() {
  try {
    await ElMessageBox.confirm('确定清空全部观看历史记录吗？清空后无法恢复。', '清空历史', {
      confirmButtonText: '确定清空',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await interactionApi.clearHistory()
    list.value = []
    ElMessage.success('观看历史已全部清空')
  } catch {
    // 取消
  }
}

onMounted(loadList)
</script>
