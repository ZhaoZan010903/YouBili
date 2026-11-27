<template>
  <article
    class="group rounded-2xl overflow-hidden transition-all duration-200 hover:-translate-y-1 cursor-pointer flex flex-col"
    style="background-color: var(--yb-surface); border: 1px solid var(--yb-border);"
    @click="router.push(`/video/${video.videoId}`)"
  >
    <!-- 16:9 Thumbnail -->
    <div class="relative aspect-video overflow-hidden bg-black/10">
      <img
        :src="video.videoImgUrl || FALLBACK_COVER"
        :alt="video.title"
        loading="lazy"
        class="w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
      />

      <!-- Top-left category pill -->
      <span
        v-if="video.categoryName"
        class="absolute top-2 left-2 px-2 py-0.5 text-[11px] font-medium rounded-md text-white backdrop-blur-md bg-black/45"
      >
        {{ video.categoryName }}
      </span>

      <!-- Bottom stats overlay -->
      <div
        class="absolute inset-x-0 bottom-0 pt-6 pb-2 px-2.5 flex items-center justify-between text-xs text-white"
        style="background: linear-gradient(180deg, transparent 0%, rgba(0,0,0,0.72) 100%);"
      >
        <div class="flex items-center gap-3">
          <span class="inline-flex items-center gap-1">
            <el-icon :size="13"><VideoPlay /></el-icon>
            {{ formatCount(video.viewCount) }}
          </span>
          <span class="inline-flex items-center gap-1">
            <el-icon :size="13"><ChatDotRound /></el-icon>
            {{ formatCount(video.barrageCount) }}
          </span>
        </div>
        <span class="px-1.5 py-0.5 rounded bg-black/55 font-mono text-[11px]">
          {{ formatDuration(video.duration) }}
        </span>
      </div>
    </div>

    <!-- Meta Info -->
    <div class="p-3 flex-1 flex flex-col justify-between gap-2">
      <h3
        class="text-sm font-semibold leading-snug line-clamp-2 transition-colors group-hover:text-[var(--yb-brand)]"
        style="color: var(--yb-text);"
      >
        {{ video.title }}
      </h3>

      <div class="flex items-center justify-between gap-2 text-xs" style="color: var(--yb-text-muted);">
        <div
          class="flex items-center gap-1.5 min-w-0 hover:text-[var(--yb-brand)] transition-colors"
          @click.stop="router.push(`/space/${video.userId}`)"
        >
          <el-avatar :size="20" :src="video.authorAvatarUrl || FALLBACK_AVATAR" class="shrink-0" />
          <span class="truncate">{{ video.authorNickname }}</span>
        </div>
        <span class="shrink-0">{{ formatRelativeTime(video.uploadDate) }}</span>
      </div>
    </div>
  </article>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import type { VideoCard } from '@/types/api'
import {
  FALLBACK_AVATAR,
  FALLBACK_COVER,
  formatCount,
  formatDuration,
  formatRelativeTime
} from '@/utils/format'

defineProps<{
  video: VideoCard
}>()

const router = useRouter()
</script>
