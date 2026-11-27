<template>
  <div class="space-y-6">
    <!-- Category & Sort Header -->
    <div class="yb-card p-5 space-y-4">
      <div class="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 class="text-xl font-bold">分区频道</h1>
          <p class="text-xs mt-0.5" style="color: var(--yb-text-muted);">按兴趣探索不同分区的优质投稿</p>
        </div>

        <el-segmented v-model="sort" :options="sortOptions" @change="onFilterChange" />
      </div>

      <div class="flex flex-wrap gap-2">
        <button
          type="button"
          class="px-4 py-1.5 rounded-full text-sm font-medium transition-all cursor-pointer border"
          :style="activeCategoryId === 0
            ? { backgroundColor: 'var(--yb-brand)', borderColor: 'var(--yb-brand)', color: '#fff' }
            : { backgroundColor: 'var(--yb-surface-muted)', borderColor: 'transparent', color: 'var(--yb-text-secondary)' }"
          @click="selectCategory(0)"
        >
          全部分区
        </button>
        <button
          v-for="cat in categories"
          :key="cat.categoryId"
          type="button"
          class="px-4 py-1.5 rounded-full text-sm font-medium transition-all cursor-pointer border"
          :style="activeCategoryId === cat.categoryId
            ? { backgroundColor: 'var(--yb-brand)', borderColor: 'var(--yb-brand)', color: '#fff' }
            : { backgroundColor: 'var(--yb-surface-muted)', borderColor: 'transparent', color: 'var(--yb-text-secondary)' }"
          @click="selectCategory(cat.categoryId)"
        >
          {{ cat.categoryName }}
        </button>
      </div>
    </div>

    <!-- Videos Grid -->
    <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5">
      <div v-for="n in 8" :key="n" class="yb-card p-3">
        <el-skeleton :rows="3" animated />
      </div>
    </div>

    <el-empty v-else-if="videos.length === 0" description="该分区暂时还没有视频哦" />

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
        @current-change="loadVideos"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { categoryApi } from '@/api/modules'
import VideoCard from '@/components/VideoCard.vue'
import type { CategoryItem, VideoCard as VideoCardType } from '@/types/api'

const route = useRoute()
const router = useRouter()

const categories = ref<CategoryItem[]>([])
const activeCategoryId = ref<number>(0)
const sort = ref('latest')
const page = ref(1)
const size = 16
const total = ref(0)
const videos = ref<VideoCardType[]>([])
const loading = ref(true)

const sortOptions = [
  { label: '最新发布', value: 'latest' },
  { label: '最多播放', value: 'views' },
  { label: '最多点赞', value: 'likes' }
]

async function loadVideos() {
  loading.value = true
  try {
    const res = await categoryApi.listCategoryVideos(activeCategoryId.value, {
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

function selectCategory(id: number) {
  activeCategoryId.value = id
  page.value = 1
  router.replace({ query: id > 0 ? { categoryId: String(id) } : {} })
  loadVideos()
}

function onFilterChange() {
  page.value = 1
  loadVideos()
}

watch(
  () => route.query.categoryId,
  (val) => {
    const parsed = Number(val)
    activeCategoryId.value = Number.isFinite(parsed) && parsed > 0 ? parsed : 0
  },
  { immediate: true }
)

onMounted(async () => {
  categories.value = await categoryApi.listCategories().catch(() => [])
  await loadVideos()
})
</script>
