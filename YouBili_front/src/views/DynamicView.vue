<template>
  <div class="max-w-3xl mx-auto space-y-6">
    <div class="yb-card p-5 flex items-center justify-between">
      <div>
        <h1 class="text-xl font-bold">关注动态</h1>
        <p class="text-xs mt-0.5" style="color: var(--yb-text-muted);">实时追踪你关注的 UP 主最新投稿</p>
      </div>
      <el-button round plain :loading="loading" @click="loadDynamics">
        <el-icon class="mr-1"><Refresh /></el-icon>
        刷新动态
      </el-button>
    </div>

    <div v-if="loading" class="space-y-4">
      <div v-for="n in 3" :key="n" class="yb-card p-5">
        <el-skeleton :rows="4" animated />
      </div>
    </div>

    <el-empty v-else-if="items.length === 0" description="暂无关注动态，先去关注几个喜欢的 UP 主吧">
      <el-button type="primary" round @click="router.push('/hot')">发现热门 UP 主</el-button>
    </el-empty>

    <div v-else class="space-y-4">
      <article v-for="item in items" :key="item.videoId" class="yb-card p-5 space-y-4">
        <!-- Author Header -->
        <div class="flex items-center justify-between">
          <div
            class="flex items-center gap-3 cursor-pointer"
            @click="router.push(`/space/${item.userId}`)"
          >
            <el-avatar :size="44" :src="item.authorAvatarUrl || FALLBACK_AVATAR" />
            <div>
              <div class="font-bold text-sm hover:text-[var(--yb-brand)] transition-colors">
                {{ item.authorNickname }}
              </div>
              <div class="text-xs" style="color: var(--yb-text-muted);">
                {{ formatRelativeTime(item.uploadDate) }} · 投稿了视频
              </div>
            </div>
          </div>
          <el-tag v-if="item.categoryName" size="small" effect="plain" round>{{ item.categoryName }}</el-tag>
        </div>

        <!-- Embedded Video Card -->
        <div
          class="flex flex-col sm:flex-row gap-4 p-3 rounded-xl border transition-colors hover:border-[var(--yb-brand)] cursor-pointer group"
          style="background-color: var(--yb-surface-muted); border-color: var(--yb-border);"
          @click="router.push(`/video/${item.videoId}`)"
        >
          <div class="relative w-full sm:w-56 aspect-video rounded-lg overflow-hidden shrink-0 bg-black/10">
            <img
              :src="item.videoImgUrl || FALLBACK_COVER"
              :alt="item.title"
              class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
            />
            <span class="absolute bottom-1.5 right-1.5 px-1.5 py-0.5 rounded bg-black/70 text-white font-mono text-[11px]">
              {{ formatDuration(item.duration) }}
            </span>
          </div>
          <div class="flex-1 min-w-0 flex flex-col justify-between py-0.5">
            <div>
              <h3 class="font-bold text-base line-clamp-2 group-hover:text-[var(--yb-brand)] transition-colors">
                {{ item.title }}
              </h3>
              <p class="text-xs mt-1.5 line-clamp-2" style="color: var(--yb-text-secondary);">
                {{ item.description || '点击观看精彩视频内容' }}
              </p>
            </div>
            <div class="flex items-center gap-5 text-xs pt-2" style="color: var(--yb-text-muted);">
              <span class="inline-flex items-center gap-1">
                <el-icon><VideoPlay /></el-icon> {{ formatCount(item.viewCount) }}
              </span>
              <span class="inline-flex items-center gap-1">
                <el-icon><ChatDotRound /></el-icon> {{ formatCount(item.commentCount) }}
              </span>
              <span class="inline-flex items-center gap-1">
                <el-icon><CaretTop /></el-icon> {{ formatCount(item.likeCount) }}
              </span>
            </div>
          </div>
        </div>
      </article>
    </div>

    <div v-if="total > size" class="flex justify-center">
      <el-pagination
        v-model:current-page="page"
        :page-size="size"
        :total="total"
        layout="prev, pager, next"
        background
        @current-change="loadDynamics"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { socialApi } from '@/api/modules'
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
const items = ref<VideoCard[]>([])
const page = ref(1)
const size = 15
const total = ref(0)

async function loadDynamics() {
  loading.value = true
  try {
    const res = await socialApi.listDynamics({ page: page.value, size })
    items.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

onMounted(loadDynamics)
</script>
