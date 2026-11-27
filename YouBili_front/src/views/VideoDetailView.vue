<template>
  <div v-loading="loading" class="grid grid-cols-1 xl:grid-cols-12 gap-6 items-start">
    <!-- Left Theater & Discussion -->
    <div class="xl:col-span-8 space-y-5 min-w-0">
      <!-- Video Title & Meta -->
      <div v-if="video">
        <h1 class="text-xl sm:text-2xl font-bold leading-snug mb-2" style="color: var(--yb-text);">
          {{ video.title }}
        </h1>
        <div class="flex flex-wrap items-center gap-4 text-xs sm:text-sm" style="color: var(--yb-text-muted);">
          <span class="inline-flex items-center gap-1">
            <el-icon><VideoPlay /></el-icon>
            {{ formatCount(video.viewCount) }} 播放
          </span>
          <span class="inline-flex items-center gap-1">
            <el-icon><ChatDotRound /></el-icon>
            {{ formatCount(video.barrageCount) }} 弹幕
          </span>
          <span class="inline-flex items-center gap-1">
            <el-icon><Clock /></el-icon>
            {{ video.uploadDate }}
          </span>
          <el-tag v-if="video.categoryName" size="small" effect="plain" round>
            {{ video.categoryName }}
          </el-tag>
        </div>
      </div>

      <!-- Custom Danmaku Video Player -->
      <DanmakuPlayer
        v-if="video"
        :src="video.videoSrcUrl"
        :poster="video.videoImgUrl"
        :qualities="video.qualities"
        :barrages="barrages"
        @send-barrage="handleSendBarrage"
        @watch-progress="handleWatchProgress"
      />

      <!-- Interactive Action Bar -->
      <div
        v-if="video"
        class="yb-card p-4 flex flex-wrap items-center justify-between gap-3"
      >
        <div class="flex flex-wrap items-center gap-2.5">
          <button
            type="button"
            class="px-4 py-2 rounded-full text-sm font-semibold flex items-center gap-1.5 border transition-all cursor-pointer"
            :style="video.liked
              ? { backgroundColor: 'var(--yb-brand-soft)', borderColor: 'var(--yb-brand)', color: 'var(--yb-brand)' }
              : { backgroundColor: 'var(--yb-surface-muted)', borderColor: 'transparent', color: 'var(--yb-text)' }"
            @click="toggleLike"
          >
            <el-icon :size="17"><CaretTop /></el-icon>
            <span>点赞 {{ formatCount(video.likeCount) }}</span>
          </button>

          <button
            type="button"
            class="px-4 py-2 rounded-full text-sm font-semibold flex items-center gap-1.5 border transition-all cursor-pointer"
            :style="video.disliked
              ? { backgroundColor: 'rgba(148,153,160,0.18)', borderColor: 'var(--yb-text-muted)', color: 'var(--yb-text)' }
              : { backgroundColor: 'var(--yb-surface-muted)', borderColor: 'transparent', color: 'var(--yb-text-secondary)' }"
            @click="toggleDislike"
          >
            <el-icon :size="17"><CaretBottom /></el-icon>
            <span>点踩 {{ formatCount(video.dislikeCount) }}</span>
          </button>

          <button
            type="button"
            class="px-4 py-2 rounded-full text-sm font-semibold flex items-center gap-1.5 border transition-all cursor-pointer"
            :style="video.favorited
              ? { backgroundColor: 'var(--yb-accent-soft)', borderColor: 'var(--yb-accent)', color: 'var(--yb-accent)' }
              : { backgroundColor: 'var(--yb-surface-muted)', borderColor: 'transparent', color: 'var(--yb-text)' }"
            @click="toggleFavorite"
          >
            <el-icon :size="17"><StarFilled v-if="video.favorited" /><Star v-else /></el-icon>
            <span>收藏 {{ formatCount(video.favoriteCount) }}</span>
          </button>
        </div>

        <div class="flex items-center gap-2">
          <el-button round plain size="small" @click="copyShareLink">
            <el-icon class="mr-1"><Share /></el-icon>
            分享
          </el-button>
          <el-button round plain size="small" type="danger" @click="goReport">
            <el-icon class="mr-1"><Warning /></el-icon>
            举报
          </el-button>
        </div>
      </div>

      <!-- Description & Tags -->
      <div v-if="video" class="yb-card p-5 space-y-3">
        <p class="text-sm leading-relaxed whitespace-pre-line" style="color: var(--yb-text-secondary);">
          {{ video.description || 'UP主很懒，还没有填写简介~' }}
        </p>
        <div v-if="video.tags.length > 0" class="flex flex-wrap gap-2 pt-2 border-t" style="border-color: var(--yb-border);">
          <el-tag
            v-for="tag in video.tags"
            :key="tag.tagId"
            round
            effect="plain"
            class="cursor-pointer"
            @click="router.push({ path: '/search', query: { q: tag.tagName } })"
          >
            # {{ tag.tagName }}
          </el-tag>
        </div>
      </div>

      <!-- Comments Section -->
      <section v-if="video" class="yb-card p-5 space-y-5">
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-3">
            <h2 class="text-lg font-bold">评论</h2>
            <span class="text-sm" style="color: var(--yb-text-muted);">{{ commentTotal }}</span>
          </div>
          <div class="flex items-center gap-2 text-sm">
            <button
              type="button"
              class="px-3 py-1 rounded-full cursor-pointer transition-colors"
              :style="commentSort === 'hot' ? { backgroundColor: 'var(--yb-brand-soft)', color: 'var(--yb-brand)', fontWeight: 600 } : { color: 'var(--yb-text-muted)' }"
              @click="changeCommentSort('hot')"
            >
              最热
            </button>
            <button
              type="button"
              class="px-3 py-1 rounded-full cursor-pointer transition-colors"
              :style="commentSort === 'latest' ? { backgroundColor: 'var(--yb-brand-soft)', color: 'var(--yb-brand)', fontWeight: 600 } : { color: 'var(--yb-text-muted)' }"
              @click="changeCommentSort('latest')"
            >
              最新
            </button>
          </div>
        </div>

        <!-- New Comment Box -->
        <div class="flex gap-3">
          <el-avatar :size="40" :src="authStore.user?.avatarUrl || FALLBACK_AVATAR" class="shrink-0" />
          <div class="flex-1 space-y-2">
            <el-input
              v-model="newCommentText"
              type="textarea"
              :rows="3"
              maxlength="500"
              show-word-limit
              :placeholder="authStore.isLoggedIn ? '发一条友善的评论吧...' : '登录后即可发表评论'"
            />
            <div class="flex justify-end">
              <el-button type="primary" round :loading="postingComment" @click="submitComment">
                发布评论
              </el-button>
            </div>
          </div>
        </div>

        <el-divider class="!my-2" />

        <!-- Comment List -->
        <el-empty v-if="comments.length === 0" description="还没有评论，快来抢沙发吧！" />
        <div v-else class="space-y-5">
          <div
            v-for="comment in comments"
            :key="comment.commentId"
            class="flex gap-3 pb-5 border-b last:border-b-0 last:pb-0"
            style="border-color: var(--yb-border);"
          >
            <el-avatar
              :size="40"
              :src="comment.avatarUrl || FALLBACK_AVATAR"
              class="shrink-0 cursor-pointer"
              @click="router.push(`/space/${comment.userId}`)"
            />
            <div class="flex-1 min-w-0">
              <div class="flex items-center justify-between gap-2">
                <span
                  class="text-sm font-semibold cursor-pointer hover:underline"
                  style="color: var(--yb-brand);"
                  @click="router.push(`/space/${comment.userId}`)"
                >
                  {{ comment.nickname }}
                </span>
                <button
                  v-if="authStore.user?.userId === comment.userId"
                  type="button"
                  class="text-xs text-rose-500 hover:underline cursor-pointer"
                  @click="deleteMyComment(comment.commentId)"
                >
                  删除
                </button>
              </div>
              <p class="mt-1.5 text-sm leading-relaxed whitespace-pre-wrap" style="color: var(--yb-text);">
                {{ comment.commentText }}
              </p>
              <div class="mt-2 flex items-center gap-4 text-xs" style="color: var(--yb-text-muted);">
                <span>{{ formatRelativeTime(comment.gmtCreate) }}</span>
                <button
                  type="button"
                  class="hover:text-[var(--yb-brand)] cursor-pointer"
                  @click="openReplyBox(comment.commentId, null, comment.nickname)"
                >
                  回复
                </button>
              </div>

              <!-- Replies -->
              <div
                v-if="comment.replies.length > 0"
                class="mt-3 p-3 rounded-xl space-y-3"
                style="background-color: var(--yb-surface-muted);"
              >
                <div
                  v-for="reply in visibleReplies(comment)"
                  :key="reply.replyId"
                  class="flex gap-2.5 text-sm"
                >
                  <el-avatar :size="28" :src="reply.avatarUrl || FALLBACK_AVATAR" class="shrink-0" />
                  <div class="flex-1 min-w-0">
                    <div class="leading-relaxed">
                      <span class="font-semibold" style="color: var(--yb-brand);">{{ reply.nickname }}</span>
                      <span v-if="reply.replyToNickname" class="mx-1 text-xs" style="color: var(--yb-text-muted);">
                        回复 <span style="color: var(--yb-accent);">@{{ reply.replyToNickname }}</span>
                      </span>
                      <span class="ml-1.5" style="color: var(--yb-text);">{{ reply.replyText }}</span>
                    </div>
                    <div class="mt-1 flex items-center gap-4 text-xs" style="color: var(--yb-text-muted);">
                      <span>{{ formatRelativeTime(reply.gmtCreate) }}</span>
                      <button
                        type="button"
                        class="hover:text-[var(--yb-brand)] cursor-pointer"
                        @click="openReplyBox(comment.commentId, reply.replyId, reply.nickname)"
                      >
                        回复
                      </button>
                    </div>
                  </div>
                </div>

                <button
                  v-if="comment.replies.length > 3 && !expandedComments.has(comment.commentId)"
                  type="button"
                  class="text-xs font-medium cursor-pointer"
                  style="color: var(--yb-accent);"
                  @click="expandedComments.add(comment.commentId)"
                >
                  展开全部 {{ comment.replies.length }} 条回复
                </button>
              </div>

              <!-- Inline Reply Composer -->
              <div v-if="replyTarget?.commentId === comment.commentId" class="mt-3 flex gap-2">
                <el-input
                  v-model="replyText"
                  :placeholder="`回复 @${replyTarget.targetNickname}...`"
                  maxlength="500"
                  @keyup.enter="submitReply"
                />
                <el-button type="primary" :loading="postingReply" @click="submitReply">发送</el-button>
                <el-button @click="replyTarget = null">取消</el-button>
              </div>
            </div>
          </div>
        </div>

        <div v-if="commentTotal > commentSize" class="flex justify-center pt-2">
          <el-pagination
            v-model:current-page="commentPage"
            :page-size="commentSize"
            :total="commentTotal"
            layout="prev, pager, next"
            background
            @current-change="loadComments"
          />
        </div>
      </section>
    </div>

    <!-- Right Rail: Author Card + Danmaku List + Related Videos -->
    <aside class="xl:col-span-4 space-y-5">
      <!-- Author Profile Card -->
      <div v-if="video" class="yb-card p-4 flex items-center justify-between gap-3">
        <div
          class="flex items-center gap-3 min-w-0 cursor-pointer"
          @click="router.push(`/space/${video.userId}`)"
        >
          <el-avatar :size="48" :src="video.authorAvatarUrl || FALLBACK_AVATAR" class="shrink-0" />
          <div class="min-w-0">
            <div class="font-bold text-base truncate hover:text-[var(--yb-brand)] transition-colors">
              {{ video.authorNickname }}
            </div>
            <div class="text-xs mt-0.5" style="color: var(--yb-text-muted);">
              粉丝 {{ formatCount(video.authorFans) }} · 关注 {{ formatCount(video.authorFollows) }}
            </div>
          </div>
        </div>

        <el-button
          v-if="authStore.user?.userId !== video.userId"
          :type="video.followingAuthor ? 'default' : 'primary'"
          round
          size="small"
          @click="toggleFollowAuthor"
        >
          {{ video.followingAuthor ? '已关注' : '+ 关注' }}
        </el-button>
        <el-button v-else round size="small" plain @click="router.push(`/space/${video.userId}`)">
          我的空间
        </el-button>
      </div>

      <!-- Danmaku List Accordion -->
      <div class="yb-card overflow-hidden">
        <button
          type="button"
          class="w-full px-4 py-3 flex items-center justify-between text-sm font-semibold cursor-pointer"
          @click="showDanmakuList = !showDanmakuList"
        >
          <span>弹幕列表 ({{ barrages.length }})</span>
          <el-icon><ArrowDown v-if="!showDanmakuList" /><ArrowUp v-else /></el-icon>
        </button>
        <div v-if="showDanmakuList" class="max-h-64 overflow-y-auto border-t px-4 py-2 space-y-1.5 text-xs" style="border-color: var(--yb-border);">
          <div v-if="barrages.length === 0" class="py-4 text-center" style="color: var(--yb-text-muted);">
            暂无弹幕
          </div>
          <div v-for="(dm, idx) in barrages" :key="idx" class="flex items-center gap-3 py-1">
            <span class="font-mono shrink-0" style="color: var(--yb-text-muted);">{{ formatSeconds(dm.time) }}</span>
            <span class="truncate flex-1" style="color: var(--yb-text);">{{ dm.text }}</span>
          </div>
        </div>
      </div>

      <!-- Related Videos -->
      <div class="yb-card p-4 space-y-3">
        <h3 class="text-base font-bold">接下来播放</h3>
        <div v-if="relatedVideos.length === 0" class="py-6 text-center text-xs" style="color: var(--yb-text-muted);">
          暂无相关推荐
        </div>
        <div v-else class="space-y-3">
          <div
            v-for="item in relatedVideos"
            :key="item.videoId"
            class="flex gap-3 p-1.5 rounded-xl hover:bg-[var(--yb-surface-muted)] cursor-pointer transition-colors group"
            @click="router.push(`/video/${item.videoId}`)"
          >
            <div class="relative w-36 aspect-video rounded-lg overflow-hidden shrink-0 bg-black/10">
              <img
                :src="item.videoImgUrl || FALLBACK_COVER"
                :alt="item.title"
                class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
              />
              <span class="absolute bottom-1 right-1 px-1 py-0.5 rounded bg-black/65 text-white font-mono text-[10px]">
                {{ formatDuration(item.duration) }}
              </span>
            </div>
            <div class="min-w-0 flex-1 flex flex-col justify-between py-0.5">
              <h4 class="text-sm font-medium line-clamp-2 group-hover:text-[var(--yb-brand)] transition-colors">
                {{ item.title }}
              </h4>
              <div class="text-xs space-y-0.5" style="color: var(--yb-text-muted);">
                <div class="truncate">{{ item.authorNickname }}</div>
                <div>{{ formatCount(item.viewCount) }} 播放 · {{ formatCount(item.barrageCount) }} 弹幕</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </aside>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  barrageApi,
  interactionApi,
  recommendApi,
  socialApi,
  userApi,
  videoApi
} from '@/api/modules'
import DanmakuPlayer from '@/components/DanmakuPlayer.vue'
import { useAuthStore } from '@/stores/auth'
import type { BarrageItem, CommentItem, VideoCard, VideoDetail } from '@/types/api'
import {
  FALLBACK_AVATAR,
  FALLBACK_COVER,
  formatCount,
  formatDuration,
  formatRelativeTime,
  formatSeconds
} from '@/utils/format'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const loading = ref(true)
const video = ref<VideoDetail | null>(null)
const barrages = ref<BarrageItem[]>([])
const relatedVideos = ref<VideoCard[]>([])
const showDanmakuList = ref(false)

// Comments state
const comments = ref<CommentItem[]>([])
const commentTotal = ref(0)
const commentPage = ref(1)
const commentSize = 15
const commentSort = ref<'hot' | 'latest'>('hot')
const newCommentText = ref('')
const postingComment = ref(false)
const expandedComments = reactive(new Set<number>())

// Reply state
const replyTarget = ref<{ commentId: number; parentReplyId: number | null; targetNickname: string } | null>(null)
const replyText = ref('')
const postingReply = ref(false)

const currentVideoId = computed(() => Number(route.params.videoId))

function requireLogin(): boolean {
  if (!authStore.isLoggedIn) {
    authStore.openAuthModal('login')
    return false
  }
  return true
}

async function loadAll(videoId: number) {
  if (!videoId || Number.isNaN(videoId)) return
  loading.value = true
  commentPage.value = 1
  expandedComments.clear()
  replyTarget.value = null

  try {
    const [detailData, barrageData, relatedData] = await Promise.all([
      videoApi.detail(videoId),
      barrageApi.list(videoId).catch(() => []),
      recommendApi.related(videoId, 8).catch(() => [])
    ])
    video.value = detailData
    barrages.value = barrageData
    relatedVideos.value = relatedData
    document.title = `${detailData.title} - YouBili`

    await loadComments()

    if (authStore.isLoggedIn) {
      interactionApi.recordWatch(videoId, 0).catch(() => {})
    }
  } finally {
    loading.value = false
  }
}

async function loadComments() {
  const res = await socialApi.listComments(currentVideoId.value, {
    page: commentPage.value,
    size: commentSize,
    sort: commentSort.value
  })
  comments.value = res.records
  commentTotal.value = res.total
}

function changeCommentSort(sort: 'hot' | 'latest') {
  commentSort.value = sort
  commentPage.value = 1
  loadComments()
}

function visibleReplies(comment: CommentItem) {
  return expandedComments.has(comment.commentId) ? comment.replies : comment.replies.slice(0, 3)
}

async function handleSendBarrage(payload: {
  time: number
  text: string
  color: string
  type: 'scroll' | 'top' | 'bottom'
}) {
  if (!requireLogin() || !video.value) return
  const saved = await barrageApi.send(video.value.videoId, payload)
  barrages.value.push(saved)
  video.value.barrageCount += 1
  ElMessage.success('弹幕发送成功')
}

function handleWatchProgress(seconds: number) {
  if (authStore.isLoggedIn && video.value) {
    interactionApi.recordWatch(video.value.videoId, seconds).catch(() => {})
  }
}

async function toggleLike() {
  if (!requireLogin() || !video.value) return
  const state = video.value.liked
    ? await interactionApi.cancelLike(video.value.videoId)
    : await interactionApi.like(video.value.videoId)
  applyReaction(state)
}

async function toggleDislike() {
  if (!requireLogin() || !video.value) return
  const state = video.value.disliked
    ? await interactionApi.cancelDislike(video.value.videoId)
    : await interactionApi.dislike(video.value.videoId)
  applyReaction(state)
}

async function toggleFavorite() {
  if (!requireLogin() || !video.value) return
  const state = video.value.favorited
    ? await interactionApi.removeFavoriteByVideo(video.value.videoId)
    : await interactionApi.addFavorite(video.value.videoId)
  applyReaction(state)
  ElMessage.success(state.favorited ? '已添加到收藏夹' : '已取消收藏')
}

function applyReaction(state: {
  liked: boolean
  disliked: boolean
  favorited: boolean
  likeCount: number
  dislikeCount: number
  favoriteCount: number
}) {
  if (!video.value) return
  video.value.liked = state.liked
  video.value.disliked = state.disliked
  video.value.favorited = state.favorited
  video.value.likeCount = state.likeCount
  video.value.dislikeCount = state.dislikeCount
  video.value.favoriteCount = state.favoriteCount
}

async function toggleFollowAuthor() {
  if (!requireLogin() || !video.value) return
  if (video.value.followingAuthor) {
    await userApi.unfollow(video.value.userId)
    video.value.followingAuthor = false
    video.value.authorFans = Math.max(0, video.value.authorFans - 1)
    ElMessage.success('已取消关注')
  } else {
    await userApi.follow(video.value.userId)
    video.value.followingAuthor = true
    video.value.authorFans += 1
    ElMessage.success('关注成功')
  }
}

async function submitComment() {
  if (!requireLogin() || !video.value) return
  const text = newCommentText.value.trim()
  if (!text) {
    ElMessage.warning('请输入评论内容')
    return
  }
  postingComment.value = true
  try {
    await socialApi.addComment(video.value.videoId, text)
    newCommentText.value = ''
    video.value.commentCount += 1
    commentPage.value = 1
    await loadComments()
    ElMessage.success('评论发布成功')
  } finally {
    postingComment.value = false
  }
}

function openReplyBox(commentId: number, parentReplyId: number | null, targetNickname: string) {
  if (!requireLogin()) return
  replyTarget.value = { commentId, parentReplyId, targetNickname }
  replyText.value = ''
}

async function submitReply() {
  if (!requireLogin() || !replyTarget.value || !video.value) return
  const text = replyText.value.trim()
  if (!text) {
    ElMessage.warning('请输入回复内容')
    return
  }
  postingReply.value = true
  try {
    await socialApi.addReply(replyTarget.value.commentId, text, replyTarget.value.parentReplyId)
    expandedComments.add(replyTarget.value.commentId)
    replyTarget.value = null
    replyText.value = ''
    video.value.commentCount += 1
    await loadComments()
    ElMessage.success('回复成功')
  } finally {
    postingReply.value = false
  }
}

async function deleteMyComment(commentId: number) {
  await socialApi.deleteComment(commentId)
  if (video.value) {
    video.value.commentCount = Math.max(0, video.value.commentCount - 1)
  }
  await loadComments()
  ElMessage.success('评论已删除')
}

async function copyShareLink() {
  try {
    await navigator.clipboard.writeText(window.location.href)
    ElMessage.success('视频链接已复制到剪贴板')
  } catch {
    ElMessage.info(window.location.href)
  }
}

function goReport() {
  if (!requireLogin() || !video.value) return
  router.push({
    path: '/report',
    query: { targetType: 'VIDEO', targetId: String(video.value.videoId), title: video.value.title }
  })
}

watch(
  currentVideoId,
  (id) => {
    if (id) loadAll(id)
  },
  { immediate: true }
)
</script>
