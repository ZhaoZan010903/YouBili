<template>
  <div class="space-y-6 max-w-5xl mx-auto">
    <!-- Banner -->
    <div
      class="rounded-2xl p-6 text-white flex flex-wrap items-center justify-between gap-4 shadow-md"
      style="background: linear-gradient(135deg, #fb7299 0%, #ff7b54 55%, #ffb26b 100%);"
    >
      <div>
        <div class="inline-flex items-center gap-2 text-xs font-semibold px-2.5 py-0.5 rounded-full bg-white/20 mb-2">
          <el-icon><TrendCharts /></el-icon>
          实时热度榜 · 重力衰减算法
        </div>
        <h1 class="text-2xl sm:text-3xl font-extrabold tracking-tight">全站热门排行</h1>
        <p class="text-sm text-white/85 mt-1">综合播放、点赞、收藏、弹幕、评论与时间衰减动态计算</p>
      </div>
      <el-button round class="!bg-white/20 !border-white/30 !text-white hover:!bg-white/30" :loading="loading" @click="loadHot">
        <el-icon class="mr-1"><Refresh /></el-icon>
        刷新榜单
      </el-button>
    </div>

    <!-- List -->
    <div v-if="loading" class="space-y-4">
      <div v-for="n in 5" :key="n" class="yb-card p-4">
        <el-skeleton :rows="3" animated />
      </div>
    </div>

    <el-empty v-else-if="list.length === 0" description="暂无热门排行数据" />

    <div v-else class="space-y-4">
      <article
        v-for="(item, idx) in list"
        :key="item.videoId"
        class="yb-card p-4 flex flex-col sm:flex-row gap-4 hover:-translate-y-0.5 transition-all cursor-pointer group"
        @click="router.push(`/video/${item.videoId}`)"
      >
        <!-- Rank Badge -->
        <div class="flex sm:flex-col items-center justify-center sm:w-12 shrink-0 gap-2">
          <span
            class="w-9 h-9 rounded-xl flex items-center justify-center font-black text-base"
            :style="rankBadgeStyle(idx)"
          >
            {{ idx + 1 }}
          </span>
          <span v-if="item.heatScore !== undefined" class="text-[11px] font-mono" style="color: var(--yb-text-muted);">
            {{ item.heatScore }}
          </span>
        </div>

        <!-- Thumbnail -->
        <div class="relative w-full sm:w-60 aspect-video rounded-xl overflow-hidden shrink-0 bg-black/10">
          <img
            :src="item.videoImgUrl || FALLBACK_COVER"
            :alt="item.title"
            class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
          />
          <span class="absolute bottom-1.5 right-1.5 px-1.5 py-0.5 rounded bg-black/65 text-white font-mono text-xs">
            {{ formatDuration(item.duration) }}
          </span>
        </div>

        <!-- Info -->
        <div class="flex-1 min-w-0 flex flex-col justify-between gap-2">
          <div>
            <h2 class="text-lg font-bold line-clamp-2 group-hover:text-[var(--yb-brand)] transition-colors">
              {{ item.title }}
            </h2>
            <p class="mt-1 text-xs line-clamp-2" style="color: var(--yb-text-secondary);">
              {{ item.description || '精彩内容不容错过' }}
            </p>
          </div>

          <div class="flex flex-wrap items-center justify-between gap-3 pt-2 text-xs" style="color: var(--yb-text-muted);">
            <div
              class="flex items-center gap-2 hover:text-[var(--yb-brand)]"
              @click.stop="router.push(`/space/${item.userId}`)"
            >
              <el-avatar :size="24" :src="item.authorAvatarUrl || FALLBACK_AVATAR" />
              <span class="font-medium">{{ item.authorNickname }}</span>
              <span>· {{ formatRelativeTime(item.uploadDate) }}</span>
            </div>

            <div class="flex items-center gap-4">
              <span class="inline-flex items-center gap-1">
                <el-icon><VideoPlay /></el-icon> {{ formatCount(item.viewCount) }}
              </span>
              <span class="inline-flex items-center gap-1">
                <el-icon><ChatDotRound /></el-icon> {{ formatCount(item.barrageCount) }}
              </span>
              <span class="inline-flex items-center gap-1" style="color: var(--yb-brand);">
                <el-icon><CaretTop /></el-icon> {{ formatCount(item.likeCount) }}
              </span>
            </div>
          </div>
        </div>
      </article>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { recommendApi } from '@/api/modules'
import type { VideoCard } from '@/types/api'
import {
  FALLBACK_AVATAR,
  FALLBACK_COVER,
  formatCount,
  formatDuration,
  formatRelativeTime
} from '@/utils/format'

const router = useRouter()
const loading = ref(true)
const list = ref<VideoCard[]>([])

function rankBadgeStyle(idx: number) {
  if (idx === 0) return { background: 'linear-gradient(135deg, #f59e0b, #fbbf24)', color: '#fff' }
  if (idx === 1) return { background: 'linear-gradient(135deg, #64748b, #94a3b8)', color: '#fff' }
  if (idx === 2) return { background: 'linear-gradient(135deg, #b45309, #d97706)', color: '#fff' }
  return { backgroundColor: 'var(--yb-surface-muted)', color: 'var(--yb-text-secondary)' }
}

async function loadHot() {
  loading.value = true
  try {
    list.value = await recommendApi.hot(20)
  } finally {
    loading.value = false
  }
}

onMounted(loadHot)
</script>
