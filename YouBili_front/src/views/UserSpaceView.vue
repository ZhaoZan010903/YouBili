<template>
  <div v-loading="loading" class="space-y-6">
    <!-- Hero Profile Banner -->
    <section v-if="space" class="yb-card overflow-hidden">
      <div
        class="h-36 sm:h-44 relative"
        style="background: linear-gradient(125deg, #fb7299 0%, #c084fc 50%, #00aeec 100%);"
      />

      <div class="px-6 pb-6 -mt-12 relative flex flex-col md:flex-row md:items-end justify-between gap-4">
        <div class="flex flex-col sm:flex-row sm:items-end gap-4">
          <el-avatar
            :size="92"
            :src="space.profile.avatarUrl || FALLBACK_AVATAR"
            class="ring-4 shrink-0"
            style="--tw-ring-color: var(--yb-surface);"
          />
          <div class="pb-1">
            <div class="flex flex-wrap items-center gap-2">
              <h1 class="text-2xl font-extrabold">{{ space.profile.nickname }}</h1>
              <el-tag size="small" effect="plain" round>LV.{{ space.profile.accountLevel }}</el-tag>
              <span
                v-if="space.profile.membershipStatus === 'Active'"
                class="px-2 py-0.5 text-xs font-bold rounded-full text-white"
                style="background: linear-gradient(135deg, #fb7299, #ff9966);"
              >
                大会员
              </span>
            </div>
            <p class="text-xs mt-1" style="color: var(--yb-text-muted);">
              UID: {{ space.profile.userId }}
              <span v-if="space.profile.birthday"> · 生日：{{ space.profile.birthday }}</span>
            </p>
          </div>
        </div>

        <div class="flex flex-wrap items-center gap-5">
          <div class="flex items-center gap-6 text-center">
            <div>
              <div class="font-bold text-lg">{{ formatCount(space.profile.follows) }}</div>
              <div class="text-xs" style="color: var(--yb-text-muted);">关注</div>
            </div>
            <div>
              <div class="font-bold text-lg">{{ formatCount(space.profile.fans) }}</div>
              <div class="text-xs" style="color: var(--yb-text-muted);">粉丝</div>
            </div>
            <div>
              <div class="font-bold text-lg">{{ formatCount(space.profile.totalLikes) }}</div>
              <div class="text-xs" style="color: var(--yb-text-muted);">获赞</div>
            </div>
          </div>

          <el-button
            v-if="space.isSelf"
            round
            type="primary"
            plain
            @click="openEditDialog"
          >
            编辑资料
          </el-button>
          <el-button
            v-else
            round
            :type="space.isFollowing ? 'default' : 'primary'"
            @click="toggleFollow"
          >
            {{ space.isFollowing ? '已关注' : '+ 关注' }}
          </el-button>
        </div>
      </div>
    </section>

    <!-- Content Tabs -->
    <section v-if="space" class="yb-card p-5 space-y-5">
      <el-tabs v-model="activeTab">
        <el-tab-pane :label="`投稿视频 (${space.videos.length})`" name="videos">
          <el-empty v-if="space.videos.length === 0" description="还没有发布视频投稿" />
          <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5 pt-2">
            <VideoCard v-for="v in space.videos" :key="v.videoId" :video="v" />
          </div>
        </el-tab-pane>

        <el-tab-pane :label="`收藏 (${space.favorites.length})`" name="favorites">
          <el-empty v-if="space.favorites.length === 0" description="暂无公开收藏" />
          <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5 pt-2">
            <VideoCard v-for="v in space.favorites" :key="v.videoId" :video="v" />
          </div>
        </el-tab-pane>

        <el-tab-pane v-if="space.isSelf" :label="`最近观看 (${space.histories.length})`" name="histories">
          <el-empty v-if="space.histories.length === 0" description="暂无观看记录" />
          <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5 pt-2">
            <VideoCard v-for="v in space.histories" :key="v.videoId" :video="v" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </section>

    <!-- Edit Profile Dialog -->
    <el-dialog v-model="editDialogVisible" title="编辑个人资料" width="440px" class="!rounded-2xl">
      <el-form :model="editForm" label-position="top">
        <el-form-item label="昵称">
          <el-input v-model="editForm.nickname" maxlength="32" show-word-limit />
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker
            v-model="editForm.birthday"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="选择生日"
            class="!w-full"
          />
        </el-form-item>
        <el-form-item label="头像 URL">
          <el-input v-model="editForm.avatarUrl" placeholder="输入图片链接" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="savingProfile" @click="saveProfile">保存修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { userApi } from '@/api/modules'
import VideoCard from '@/components/VideoCard.vue'
import { useAuthStore } from '@/stores/auth'
import type { UserSpace } from '@/types/api'
import { FALLBACK_AVATAR, formatCount } from '@/utils/format'

const route = useRoute()
const authStore = useAuthStore()

const loading = ref(true)
const space = ref<UserSpace | null>(null)
const activeTab = ref('videos')
const editDialogVisible = ref(false)
const savingProfile = ref(false)

const editForm = reactive({
  nickname: '',
  birthday: '' as string | null,
  avatarUrl: ''
})

const targetUserId = computed(() => Number(route.params.userId))

async function loadSpace(userId: number) {
  if (!userId || Number.isNaN(userId)) return
  loading.value = true
  try {
    space.value = await userApi.space(userId)
    document.title = `${space.value.profile.nickname} 的空间 - YouBili`
  } finally {
    loading.value = false
  }
}

function openEditDialog() {
  if (!space.value) return
  editForm.nickname = space.value.profile.nickname
  editForm.birthday = space.value.profile.birthday ?? null
  editForm.avatarUrl = space.value.profile.avatarUrl
  editDialogVisible.value = true
}

async function saveProfile() {
  savingProfile.value = true
  try {
    const updated = await userApi.updateProfile({
      nickname: editForm.nickname.trim(),
      birthday: editForm.birthday,
      avatarUrl: editForm.avatarUrl.trim()
    })
    if (space.value) {
      space.value.profile = updated
    }
    await authStore.refreshProfile()
    editDialogVisible.value = false
    ElMessage.success('个人资料已更新')
  } finally {
    savingProfile.value = false
  }
}

async function toggleFollow() {
  if (!authStore.isLoggedIn) {
    authStore.openAuthModal('login')
    return
  }
  if (!space.value) return
  if (space.value.isFollowing) {
    await userApi.unfollow(space.value.profile.userId)
    space.value.isFollowing = false
    space.value.profile.fans = Math.max(0, space.value.profile.fans - 1)
    ElMessage.success('已取消关注')
  } else {
    await userApi.follow(space.value.profile.userId)
    space.value.isFollowing = true
    space.value.profile.fans += 1
    ElMessage.success('关注成功')
  }
}

watch(
  targetUserId,
  (id) => {
    if (id) loadSpace(id)
  },
  { immediate: true }
)
</script>
