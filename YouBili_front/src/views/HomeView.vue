<template>
  <div class="space-y-8">
    <!-- Quick Category Pills -->
    <section v-if="categories.length > 0" class="flex items-center gap-2 overflow-x-auto pb-1">
      <button
        type="button"
        class="px-4 py-1.5 rounded-full text-sm font-medium shrink-0 transition-all cursor-pointer"
        style="background: var(--yb-brand); color: #fff;"
        @click="router.push('/index')"
      >
        综合推荐
      </button>
      <button
        v-for="cat in categories"
        :key="cat.categoryId"
        type="button"
        class="px-4 py-1.5 rounded-full text-sm font-medium shrink-0 border transition-colors hover:border-[var(--yb-brand)] hover:text-[var(--yb-brand)] cursor-pointer"
        style="background-color: var(--yb-surface); border-color: var(--yb-border); color: var(--yb-text-secondary);"
        @click="router.push({ path: '/classification', query: { categoryId: String(cat.categoryId) } })"
      >
        {{ cat.categoryName }}
      </button>
    </section>

    <!-- Hero Section: Carousel + Featured 4 Cards -->
    <section class="grid grid-cols-1 xl:grid-cols-12 gap-5">
      <!-- Carousel -->
      <div class="xl:col-span-6">
        <el-skeleton :loading="loadingHero" animated>
          <template #template>
            <el-skeleton-item variant="image" class="!w-full !h-[340px] sm:!h-[380px] !rounded-2xl" />
          </template>
          <el-carousel
            v-if="carouselList.length > 0"
            height="380px"
            trigger="click"
            class="rounded-2xl overflow-hidden shadow-md"
          >
            <el-carousel-item
              v-for="item in carouselList"
              :key="item.videoId"
              class="cursor-pointer relative group"
              @click="router.push(`/video/${item.videoId}`)"
            >
              <img
                :src="item.videoImgUrl || FALLBACK_COVER"
                :alt="item.title"
                class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
              />
              <div
                class="absolute inset-x-0 bottom-0 p-6 text-white"
                style="background: linear-gradient(180deg, transparent 0%, rgba(0,0,0,0.85) 100%);"
              >
                <span
                  v-if="item.categoryName"
                  class="inline-block px-2.5 py-0.5 rounded-full text-xs font-semibold mb-2"
                  style="background-color: var(--yb-brand);"
                >
                  {{ item.categoryName }}
                </span>
                <h2 class="text-xl sm:text-2xl font-bold line-clamp-1 mb-1">{{ item.title }}</h2>
                <div class="flex items-center gap-4 text-xs text-white/80">
                  <span>UP主：{{ item.authorNickname }}</span>
                  <span>{{ formatCount(item.viewCount) }} 播放</span>
                  <span>{{ formatCount(item.likeCount) }} 点赞</span>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
          <div
            v-else
            class="h-[380px] rounded-2xl flex flex-col items-center justify-center yb-card text-center p-6"
          >
            <el-icon :size="44" style="color: var(--yb-brand);"><VideoCamera /></el-icon>
            <h3 class="mt-3 text-lg font-bold">欢迎来到 YouBili 2.0</h3>
            <p class="mt-1 text-sm" style="color: var(--yb-text-muted);">
              暂无精选视频，成为第一个发布投稿的创作者吧！
            </p>
            <el-button type="primary" round class="mt-4" @click="router.push('/contribute')">立即投稿</el-button>
          </div>
        </el-skeleton>
      </div>

      <!-- Featured 4-card Grid -->
      <div class="xl:col-span-6 grid grid-cols-2 gap-4">
        <template v-if="loadingHero">
          <div v-for="n in 4" :key="n" class="yb-card p-2">
            <el-skeleton animated>
              <template #template>
                <el-skeleton-item variant="image" class="!w-full !h-32 !rounded-xl" />
                <el-skeleton-item variant="p" class="mt-2 !w-3/4" />
              </template>
            </el-skeleton>
          </div>
        </template>
        <VideoCard v-for="video in featuredSideList" :key="video.videoId" :video="video" />
      </div>
    </section>

    <!-- Personalized Recommendation Feed -->
    <section>
      <div class="flex items-center justify-between mb-4">
        <div class="flex items-center gap-2.5">
          <span class="w-1.5 h-6 rounded-full" style="background: linear-gradient(180deg, #fb7299, #00aeec);" />
          <h2 class="text-xl font-bold tracking-tight">为你推荐</h2>
          <span class="text-xs px-2 py-0.5 rounded-full" style="background: var(--yb-accent-soft); color: var(--yb-accent);">
            User-CF 协同过滤
          </span>
        </div>

        <el-button round plain :loading="loadingFeed" @click="loadFeed">
          <el-icon class="mr-1"><Refresh /></el-icon>
          换一换
        </el-button>
      </div>

      <div v-if="loadingFeed" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5">
        <div v-for="n in 8" :key="n" class="yb-card p-3">
          <el-skeleton animated>
            <template #template>
              <el-skeleton-item variant="image" class="!w-full !h-40 !rounded-xl" />
              <el-skeleton-item variant="h3" class="mt-3 !w-4/5" />
              <el-skeleton-item variant="text" class="mt-2 !w-1/2" />
            </template>
          </el-skeleton>
        </div>
      </div>

      <el-empty v-else-if="feedList.length === 0" description="暂无推荐视频，快去发布第一条投稿吧">
        <el-button type="primary" round @click="router.push('/contribute')">发布投稿</el-button>
      </el-empty>

      <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5">
        <VideoCard v-for="video in feedList" :key="video.videoId" :video="video" />
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { categoryApi, recommendApi } from '@/api/modules'
import VideoCard from '@/components/VideoCard.vue'
import type { CategoryItem, VideoCard as VideoCardType } from '@/types/api'
import { FALLBACK_COVER, formatCount } from '@/utils/format'

const router = useRouter()

const categories = ref<CategoryItem[]>([])
const carouselList = ref<VideoCardType[]>([])
const feedList = ref<VideoCardType[]>([])
const loadingHero = ref(true)
const loadingFeed = ref(true)

const featuredSideList = computed(() => {
  const pool = carouselList.value.length >= 4 ? carouselList.value.slice(0, 4) : feedList.value.slice(0, 4)
  return pool
})

async function loadHero() {
  loadingHero.value = true
  try {
    const [cats, carousel] = await Promise.all([
      categoryApi.listCategories().catch(() => []),
      recommendApi.carousel(5).catch(() => [])
    ])
    categories.value = cats
    carouselList.value = carousel
  } finally {
    loadingHero.value = false
  }
}

async function loadFeed() {
  loadingFeed.value = true
  try {
    feedList.value = await recommendApi.feed(16)
  } catch {
    feedList.value = []
  } finally {
    loadingFeed.value = false
  }
}

onMounted(() => {
  loadHero()
  loadFeed()
})
</script>
