<template>
  <div class="space-y-6">
    <!-- Search Summary Header -->
    <div class="yb-card p-5 flex flex-wrap items-center justify-between gap-4">
      <div>
        <h1 class="text-xl font-bold">
          搜索结果：<span style="color: var(--yb-brand);">{{ keyword || '全部视频' }}</span>
        </h1>
        <p class="text-xs mt-1" style="color: var(--yb-text-muted);">
          共找到 <strong>{{ total }}</strong> 条相关视频
        </p>
      </div>

      <el-segmented v-model="sort" :options="sortOptions" @change="onSortChange" />
    </div>

    <!-- Results -->
    <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5">
      <div v-for="n in 8" :key="n" class="yb-card p-3">
        <el-skeleton :rows="3" animated />
      </div>
    </div>

    <el-empty v-else-if="videos.length === 0" :description="`没有找到与「${keyword}」相关的视频`">
      <el-button type="primary" round @click="router.push('/index')">返回首页探索</el-button>
    </el-empty>

    <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5">
      <VideoCard v-for="video in videos" :key="video.videoId" :video="video" />
    </div>

    <div v-if="total > size" class="flex justify-center pt-2">
      <el-pagination
        v-model:current-page="page"
        :page-size="size"
        :total="total"
        layout="prev, pager, next"
        background
        @current-change="executeSearch"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { searchApi } from '@/api/modules'
import VideoCard from '@/components/VideoCard.vue'
import type { VideoCard as VideoCardType } from '@/types/api'

const route = useRoute()
const router = useRouter()

const keyword = computed(() => String(route.query.q ?? route.query.value ?? '').trim())
const sort = ref('latest')
const page = ref(1)
const size = 16
const total = ref(0)
const videos = ref<VideoCardType[]>([])
const loading = ref(false)

const sortOptions = [
  { label: '最新发布', value: 'latest' },
  { label: '最多播放', value: 'views' },
  { label: '最多点赞', value: 'likes' }
]

async function executeSearch() {
  loading.value = true
  try {
    const res = await searchApi.videos({
      keyword: keyword.value,
      page: page.value,
      size,
      sort: sort.value
    })
    videos.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

function onSortChange() {
  page.value = 1
  executeSearch()
}

watch(
  keyword,
  () => {
    page.value = 1
    executeSearch()
  },
  { immediate: true }
)
</script>
