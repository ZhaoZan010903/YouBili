<template>
  <div class="space-y-6">
    <div class="yb-card p-5 flex flex-wrap items-center justify-between gap-4">
      <div>
        <h1 class="text-xl font-bold">我的收藏夹</h1>
        <p class="text-xs mt-0.5" style="color: var(--yb-text-muted);">共收藏 {{ list.length }} 个视频</p>
      </div>
      <div class="flex items-center gap-2 w-full sm:w-72">
        <el-input
          v-model="keyword"
          placeholder="搜索收藏的视频或 UP 主"
          clearable
          prefix-icon="Search"
          @keyup.enter="loadList"
          @clear="loadList"
        />
        <el-button type="primary" @click="loadList">搜索</el-button>
      </div>
    </div>

    <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5">
      <div v-for="n in 4" :key="n" class="yb-card p-3">
        <el-skeleton :rows="3" animated />
      </div>
    </div>

    <el-empty v-else-if="list.length === 0" description="收藏夹还是空的，去发现喜欢的视频吧">
      <el-button type="primary" round @click="router.push('/index')">去逛逛</el-button>
    </el-empty>

    <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5">
      <article
        v-for="item in list"
        :key="item.favoriteId"
        class="yb-card overflow-hidden flex flex-col group cursor-pointer hover:-translate-y-1 transition-all"
        @click="router.push(`/video/${item.videoId}`)"
      >
        <div class="relative aspect-video overflow-hidden bg-black/10">
          <img
            :src="item.videoImgUrl || FALLBACK_COVER"
            :alt="item.title"
            class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
          />
          <span class="absolute bottom-1.5 right-1.5 px-1.5 py-0.5 rounded bg-black/70 text-white font-mono text-[11px]">
            {{ formatDuration(item.duration) }}
          </span>
        </div>

        <div class="p-3.5 flex-1 flex flex-col justify-between gap-3">
          <div>
            <h3 class="font-semibold text-sm line-clamp-2 group-hover:text-[var(--yb-brand)] transition-colors">
              {{ item.title }}
            </h3>
            <div class="flex items-center gap-2 mt-2 text-xs" style="color: var(--yb-text-muted);">
              <el-avatar :size="20" :src="item.authorAvatarUrl || FALLBACK_AVATAR" />
              <span class="truncate">{{ item.authorNickname }}</span>
            </div>
          </div>

          <div class="flex items-center justify-between pt-2 border-t text-xs" style="border-color: var(--yb-border); color: var(--yb-text-muted);">
            <span>收藏于 {{ formatRelativeTime(item.favoriteDate) }}</span>
            <el-button
              type="danger"
              text
              size="small"
              @click.stop="removeItem(item)"
            >
              移出
            </el-button>
          </div>
        </div>
      </article>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { interactionApi } from '@/api/modules'
import type { FavoriteItem } from '@/types/api'
import { FALLBACK_AVATAR, FALLBACK_COVER, formatDuration, formatRelativeTime } from '@/utils/format'

const router = useRouter()
const loading = ref(true)
const keyword = ref('')
const list = ref<FavoriteItem[]>([])

async function loadList() {
  loading.value = true
  try {
    list.value = await interactionApi.listFavorites(keyword.value.trim() || undefined)
  } finally {
    loading.value = false
  }
}

async function removeItem(item: FavoriteItem) {
  try {
    await ElMessageBox.confirm(`确定将「${item.title}」移出收藏夹吗？`, '移出收藏', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await interactionApi.removeFavoriteById(item.favoriteId)
    list.value = list.value.filter((f) => f.favoriteId !== item.favoriteId)
    ElMessage.success('已移出收藏夹')
  } catch {
    // 用户取消
  }
}

onMounted(loadList)
</script>
