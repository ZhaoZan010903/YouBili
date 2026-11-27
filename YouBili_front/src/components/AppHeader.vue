<template>
  <header
    class="fixed top-0 left-0 right-0 h-16 z-40 border-b backdrop-blur-xl transition-colors"
    style="background-color: var(--yb-header-bg); border-color: var(--yb-border);"
  >
    <div class="h-full px-4 lg:px-6 flex items-center justify-between gap-4">
      <!-- Left: Sidebar Toggle & Brand -->
      <div class="flex items-center gap-3 shrink-0">
        <button
          type="button"
          class="w-9 h-9 rounded-xl flex items-center justify-center transition-colors hover:bg-[var(--yb-surface-muted)] cursor-pointer"
          style="color: var(--yb-text-secondary);"
          title="展开/收起导航栏"
          @click="uiStore.toggleSidebar()"
        >
          <el-icon :size="20">
            <Expand v-if="uiStore.sidebarCollapsed" />
            <Fold v-else />
          </el-icon>
        </button>

        <router-link to="/index" class="flex items-center gap-2.5 group">
          <div
            class="w-9 h-9 rounded-xl flex items-center justify-center shadow-sm transition-transform group-hover:scale-105"
            style="background: linear-gradient(135deg, #fb7299 0%, #ff5c8a 100%);"
          >
            <img src="/mLogo.png" alt="YouBili" class="w-6 h-6 object-contain brightness-0 invert" />
          </div>
          <div class="hidden sm:flex flex-col leading-none">
            <span class="text-lg font-extrabold tracking-tight" style="color: var(--yb-text);">
              You<span style="color: var(--yb-brand);">Bili</span>
            </span>
            <span class="text-[10px] font-medium tracking-wider uppercase" style="color: var(--yb-text-muted);">
              Video 2.0
            </span>
          </div>
        </router-link>
      </div>

      <!-- Center: Pill Search Bar -->
      <div class="flex-1 max-w-xl relative">
        <el-popover
          :visible="searchDropdownOpen"
          placement="bottom"
          :width="searchBoxWidth"
          :show-arrow="false"
          popper-class="!p-3 !rounded-2xl"
        >
          <template #reference>
            <div
              ref="searchWrapperRef"
              class="flex items-center h-10 rounded-full px-4 border transition-all"
              :style="{
                backgroundColor: 'var(--yb-surface-muted)',
                borderColor: searchFocused ? 'var(--yb-brand)' : 'transparent'
              }"
            >
              <input
                v-model="keyword"
                type="text"
                placeholder="搜索视频、番剧、UP主..."
                class="flex-1 bg-transparent border-none outline-none text-sm"
                style="color: var(--yb-text);"
                @focus="onSearchFocus"
                @blur="onSearchBlur"
                @input="onSearchInput"
                @keydown.enter="submitSearch()"
              />
              <button
                v-if="keyword"
                type="button"
                class="mr-2 text-xs opacity-60 hover:opacity-100 cursor-pointer"
                @mousedown.prevent="keyword = ''; suggestions = []"
              >
                <el-icon><Close /></el-icon>
              </button>
              <button
                type="button"
                class="w-7 h-7 rounded-full flex items-center justify-center transition-colors cursor-pointer"
                style="color: var(--yb-text-secondary);"
                @mousedown.prevent="submitSearch()"
              >
                <el-icon :size="16"><Search /></el-icon>
              </button>
            </div>
          </template>

          <!-- Suggestions -->
          <div v-if="suggestions.length > 0" class="mb-3">
            <div class="text-xs font-semibold mb-2 px-1" style="color: var(--yb-text-muted);">搜索建议</div>
            <div class="flex flex-col gap-1">
              <button
                v-for="item in suggestions"
                :key="item"
                type="button"
                class="text-left px-2.5 py-1.5 rounded-lg text-sm flex items-center gap-2 transition-colors hover:bg-[var(--yb-surface-muted)] cursor-pointer"
                style="color: var(--yb-text);"
                @mousedown.prevent="submitSearch(item)"
              >
                <el-icon :size="14" style="color: var(--yb-text-muted);"><Search /></el-icon>
                <span class="truncate">{{ item }}</span>
              </button>
            </div>
          </div>

          <!-- Local Search History -->
          <div v-if="uiStore.searchHistory.length > 0">
            <div class="flex items-center justify-between mb-2 px-1">
              <span class="text-xs font-semibold" style="color: var(--yb-text-muted);">搜索历史</span>
              <button
                type="button"
                class="text-xs cursor-pointer hover:underline"
                style="color: var(--yb-text-muted);"
                @mousedown.prevent="uiStore.clearSearchHistory()"
              >
                清空
              </button>
            </div>
            <div class="flex flex-wrap gap-2">
              <el-tag
                v-for="item in uiStore.searchHistory"
                :key="item"
                closable
                effect="plain"
                class="cursor-pointer !rounded-full"
                @mousedown.prevent="submitSearch(item)"
                @close="uiStore.removeSearchHistory(item)"
              >
                {{ item }}
              </el-tag>
            </div>
          </div>

          <div
            v-if="suggestions.length === 0 && uiStore.searchHistory.length === 0"
            class="text-center py-4 text-xs"
            style="color: var(--yb-text-muted);"
          >
            输入关键词探索精彩视频内容
          </div>
        </el-popover>
      </div>

      <!-- Right: Actions & User -->
      <div class="flex items-center gap-1.5 sm:gap-2.5 shrink-0">
        <!-- Theme Toggle -->
        <button
          type="button"
          class="w-9 h-9 rounded-xl flex items-center justify-center transition-colors hover:bg-[var(--yb-surface-muted)] cursor-pointer"
          style="color: var(--yb-text-secondary);"
          :title="uiStore.isDark ? '切换亮色主题' : '切换暗夜模式'"
          @click="uiStore.toggleTheme()"
        >
          <el-icon :size="18">
            <Sunny v-if="uiStore.isDark" />
            <Moon v-else />
          </el-icon>
        </button>

        <!-- Favorites Popover -->
        <el-popover placement="bottom" :width="340" trigger="hover" @show="loadPreviewFavorites">
          <template #reference>
            <button
              type="button"
              class="hidden md:flex flex-col items-center justify-center px-2 py-1 rounded-xl transition-colors hover:bg-[var(--yb-surface-muted)] cursor-pointer"
              style="color: var(--yb-text-secondary);"
              @click="goProtected('/collection')"
            >
              <el-icon :size="18"><Star /></el-icon>
              <span class="text-[11px] mt-0.5">收藏</span>
            </button>
          </template>
          <div>
            <div class="flex items-center justify-between mb-2">
              <span class="font-semibold text-sm">最近收藏</span>
              <router-link v-if="authStore.isLoggedIn" to="/collection" class="text-xs" style="color: var(--yb-brand);">
                查看全部
              </router-link>
            </div>
            <div v-if="!authStore.isLoggedIn" class="py-6 text-center">
              <p class="text-xs mb-3" style="color: var(--yb-text-muted);">登录后查看你的收藏夹</p>
              <el-button type="primary" size="small" round @click="authStore.openAuthModal('login')">立即登录</el-button>
            </div>
            <div v-else-if="previewFavorites.length === 0" class="py-6 text-center text-xs" style="color: var(--yb-text-muted);">
              暂无收藏视频
            </div>
            <div v-else class="max-h-72 overflow-y-auto space-y-2">
              <div
                v-for="item in previewFavorites.slice(0, 6)"
                :key="item.favoriteId"
                class="flex gap-2.5 p-1.5 rounded-lg hover:bg-[var(--yb-surface-muted)] cursor-pointer transition-colors"
                @click="router.push(`/video/${item.videoId}`)"
              >
                <img
                  :src="item.videoImgUrl || FALLBACK_COVER"
                  class="w-24 h-14 rounded-md object-cover shrink-0"
                  alt=""
                />
                <div class="min-w-0 flex-1 flex flex-col justify-between">
                  <p class="text-xs font-medium line-clamp-2">{{ item.title }}</p>
                  <span class="text-[11px]" style="color: var(--yb-text-muted);">{{ item.authorNickname }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-popover>

        <!-- History Popover -->
        <el-popover placement="bottom" :width="340" trigger="hover" @show="loadPreviewHistory">
          <template #reference>
            <button
              type="button"
              class="hidden md:flex flex-col items-center justify-center px-2 py-1 rounded-xl transition-colors hover:bg-[var(--yb-surface-muted)] cursor-pointer"
              style="color: var(--yb-text-secondary);"
              @click="goProtected('/history')"
            >
              <el-icon :size="18"><Clock /></el-icon>
              <span class="text-[11px] mt-0.5">历史</span>
            </button>
          </template>
          <div>
            <div class="flex items-center justify-between mb-2">
              <span class="font-semibold text-sm">观看历史</span>
              <router-link v-if="authStore.isLoggedIn" to="/history" class="text-xs" style="color: var(--yb-brand);">
                查看全部
              </router-link>
            </div>
            <div v-if="!authStore.isLoggedIn" class="py-6 text-center">
              <p class="text-xs mb-3" style="color: var(--yb-text-muted);">登录后同步多端播放记录</p>
              <el-button type="primary" size="small" round @click="authStore.openAuthModal('login')">立即登录</el-button>
            </div>
            <div v-else-if="previewHistory.length === 0" class="py-6 text-center text-xs" style="color: var(--yb-text-muted);">
              暂无观看历史
            </div>
            <div v-else class="max-h-72 overflow-y-auto space-y-2">
              <div
                v-for="item in previewHistory.slice(0, 6)"
                :key="item.historyId"
                class="flex gap-2.5 p-1.5 rounded-lg hover:bg-[var(--yb-surface-muted)] cursor-pointer transition-colors"
                @click="router.push(`/video/${item.videoId}`)"
              >
                <img
                  :src="item.videoImgUrl || FALLBACK_COVER"
                  class="w-24 h-14 rounded-md object-cover shrink-0"
                  alt=""
                />
                <div class="min-w-0 flex-1 flex flex-col justify-between">
                  <p class="text-xs font-medium line-clamp-2">{{ item.title }}</p>
                  <span class="text-[11px]" style="color: var(--yb-text-muted);">
                    {{ item.authorNickname }} · {{ formatRelativeTime(item.watchDate) }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </el-popover>

        <!-- Notifications Button -->
        <button
          type="button"
          class="hidden sm:flex flex-col items-center justify-center px-2 py-1 rounded-xl transition-colors hover:bg-[var(--yb-surface-muted)] cursor-pointer"
          style="color: var(--yb-text-secondary);"
          @click="goProtected('/notify')"
        >
          <el-icon :size="18"><Bell /></el-icon>
          <span class="text-[11px] mt-0.5">消息</span>
        </button>

        <!-- User Avatar / Login -->
        <template v-if="!authStore.isLoggedIn">
          <button
            type="button"
            class="h-9 px-4 rounded-full text-sm font-medium border transition-colors cursor-pointer"
            style="border-color: var(--yb-brand); color: var(--yb-brand); background: var(--yb-brand-soft);"
            @click="authStore.openAuthModal('login')"
          >
            登录 / 注册
          </button>
        </template>
        <el-popover v-else placement="bottom-end" :width="280" trigger="hover">
          <template #reference>
            <div class="relative cursor-pointer flex items-center">
              <el-avatar
                :size="36"
                :src="authStore.user?.avatarUrl || FALLBACK_AVATAR"
                class="ring-2 ring-offset-2 ring-[var(--yb-brand)]/40"
              />
            </div>
          </template>
          <div class="p-1">
            <div class="text-center mb-3">
              <div class="flex items-center justify-center gap-1.5">
                <span class="font-bold text-base">{{ authStore.user?.nickname }}</span>
                <span
                  v-if="authStore.isVip"
                  class="px-1.5 py-0.5 text-[10px] font-bold rounded text-white"
                  style="background: linear-gradient(135deg, #fb7299, #ff9966);"
                >
                  大会员
                </span>
              </div>
              <span class="text-xs" style="color: var(--yb-text-muted);">
                LV.{{ authStore.user?.accountLevel ?? 1 }} · @{{ authStore.user?.username }}
              </span>
            </div>

            <div
              class="grid grid-cols-3 text-center py-2.5 mb-3 rounded-xl"
              style="background-color: var(--yb-surface-muted);"
            >
              <div class="cursor-pointer" @click="router.push('/follow')">
                <div class="font-bold text-sm">{{ formatCount(authStore.user?.follows) }}</div>
                <div class="text-[11px]" style="color: var(--yb-text-muted);">关注</div>
              </div>
              <div class="cursor-pointer" @click="router.push('/fans')">
                <div class="font-bold text-sm">{{ formatCount(authStore.user?.fans) }}</div>
                <div class="text-[11px]" style="color: var(--yb-text-muted);">粉丝</div>
              </div>
              <div class="cursor-pointer" @click="router.push(`/space/${authStore.user?.userId}`)">
                <div class="font-bold text-sm">{{ formatCount(authStore.user?.videoCount) }}</div>
                <div class="text-[11px]" style="color: var(--yb-text-muted);">投稿</div>
              </div>
            </div>

            <div class="space-y-1">
              <button
                type="button"
                class="w-full px-3 py-2 rounded-lg text-sm flex items-center gap-2.5 hover:bg-[var(--yb-surface-muted)] cursor-pointer transition-colors"
                @click="router.push(`/space/${authStore.user?.userId}`)"
              >
                <el-icon><User /></el-icon>
                <span>个人空间</span>
              </button>
              <button
                type="button"
                class="w-full px-3 py-2 rounded-lg text-sm flex items-center gap-2.5 hover:bg-[var(--yb-surface-muted)] cursor-pointer transition-colors"
                @click="router.push('/contribute-manage')"
              >
                <el-icon><FolderOpened /></el-icon>
                <span>稿件管理</span>
              </button>
              <button
                type="button"
                class="w-full px-3 py-2 rounded-lg text-sm flex items-center gap-2.5 hover:bg-[var(--yb-surface-muted)] cursor-pointer transition-colors"
                @click="router.push('/orders')"
              >
                <el-icon><Tickets /></el-icon>
                <span>我的订单</span>
              </button>
              <el-divider class="!my-2" />
              <button
                type="button"
                class="w-full px-3 py-2 rounded-lg text-sm flex items-center gap-2.5 text-rose-500 hover:bg-rose-500/10 cursor-pointer transition-colors"
                @click="handleLogout"
              >
                <el-icon><SwitchButton /></el-icon>
                <span>退出登录</span>
              </button>
            </div>
          </div>
        </el-popover>

        <!-- Contribute CTA -->
        <button
          type="button"
          class="yb-brand-btn h-9 px-4 rounded-full text-sm font-semibold flex items-center gap-1.5 cursor-pointer"
          @click="goProtected('/contribute')"
        >
          <el-icon><Upload /></el-icon>
          <span class="hidden sm:inline">投稿</span>
        </button>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { interactionApi, searchApi } from '@/api/modules'
import { useAuthStore } from '@/stores/auth'
import { useUiStore } from '@/stores/ui'
import type { FavoriteItem, HistoryItem } from '@/types/api'
import { FALLBACK_AVATAR, FALLBACK_COVER, formatCount, formatRelativeTime } from '@/utils/format'

const router = useRouter()
const authStore = useAuthStore()
const uiStore = useUiStore()

const searchWrapperRef = ref<HTMLElement | null>(null)
const keyword = ref('')
const searchFocused = ref(false)
const suggestions = ref<string[]>([])
const previewFavorites = ref<FavoriteItem[]>([])
const previewHistory = ref<HistoryItem[]>([])
let suggestTimer: ReturnType<typeof setTimeout> | null = null

const searchBoxWidth = computed(() => searchWrapperRef.value?.offsetWidth || 440)
const searchDropdownOpen = computed(
  () => searchFocused.value && (suggestions.value.length > 0 || uiStore.searchHistory.length > 0)
)

function onSearchFocus() {
  searchFocused.value = true
}

function onSearchBlur() {
  setTimeout(() => {
    searchFocused.value = false
  }, 150)
}

function onSearchInput() {
  if (suggestTimer) clearTimeout(suggestTimer)
  const val = keyword.value.trim()
  if (!val) {
    suggestions.value = []
    return
  }
  suggestTimer = setTimeout(async () => {
    try {
      suggestions.value = await searchApi.suggest(val)
    } catch {
      suggestions.value = []
    }
  }, 220)
}

function submitSearch(target?: string) {
  const q = (target ?? keyword.value).trim()
  if (!q) return
  keyword.value = q
  searchFocused.value = false
  uiStore.addSearchHistory(q)
  router.push({ path: '/search', query: { q } })
}

function goProtected(path: string) {
  if (!authStore.isLoggedIn) {
    authStore.openAuthModal('login')
    return
  }
  router.push(path)
}

async function loadPreviewFavorites() {
  if (!authStore.isLoggedIn) return
  try {
    previewFavorites.value = await interactionApi.listFavorites()
  } catch {
    previewFavorites.value = []
  }
}

async function loadPreviewHistory() {
  if (!authStore.isLoggedIn) return
  try {
    previewHistory.value = await interactionApi.listHistory()
  } catch {
    previewHistory.value = []
  }
}

async function handleLogout() {
  await authStore.logout()
  previewFavorites.value = []
  previewHistory.value = []
  ElMessage.success('已安全退出登录')
  router.push('/index')
}
</script>
