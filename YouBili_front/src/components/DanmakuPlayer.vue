<template>
  <div
    ref="containerRef"
    class="rounded-2xl overflow-hidden shadow-lg border"
    :class="webFullscreen ? 'fixed inset-0 z-50 rounded-none flex flex-col bg-black' : ''"
    style="border-color: var(--yb-border); background-color: var(--yb-surface);"
  >
    <!-- Video Stage -->
    <div
      class="relative bg-black select-none group overflow-hidden"
      :class="webFullscreen ? 'flex-1' : 'aspect-video'"
      @dblclick="toggleFullscreen"
    >
      <video
        ref="videoRef"
        class="w-full h-full object-contain transition-transform duration-300"
        :class="mirrored ? '-scale-x-100' : ''"
        :poster="poster"
        playsinline
        @click="togglePlay"
        @loadedmetadata="onLoadedMetadata"
        @timeupdate="onTimeUpdate"
        @progress="onProgress"
        @play="isPlaying = true"
        @pause="isPlaying = false"
        @ended="isPlaying = false"
      />

      <!-- Danmaku Overlay Layer -->
      <div
        v-show="danmakuEnabled"
        class="absolute inset-0 pointer-events-none overflow-hidden"
        :style="{ opacity: danmakuOpacity / 100 }"
      >
        <div
          v-for="item in activeDanmaku"
          :key="item.uid"
          class="absolute whitespace-nowrap font-bold text-base sm:text-lg drop-shadow-[0_1.5px_2px_rgba(0,0,0,0.9)]"
          :class="item.type === 'scroll' ? 'yb-dm-scroll' : 'yb-dm-fixed left-1/2 -translate-x-1/2'"
          :style="{
            color: item.color,
            top: `${item.topPercent}%`,
            animationPlayState: isPlaying ? 'running' : 'paused'
          }"
          @animationend="removeActiveDanmaku(item.uid)"
        >
          {{ item.text }}
        </div>
      </div>

      <!-- Center Big Play Button -->
      <button
        v-if="!isPlaying"
        type="button"
        class="absolute inset-0 m-auto w-16 h-16 rounded-full bg-black/55 backdrop-blur-md text-white flex items-center justify-center transition-transform hover:scale-110 cursor-pointer"
        @click="togglePlay"
      >
        <el-icon :size="32"><CaretRight /></el-icon>
      </button>

      <!-- Bottom Control Overlay -->
      <div
        class="absolute inset-x-0 bottom-0 px-4 pt-10 pb-3 transition-opacity duration-200"
        style="background: linear-gradient(180deg, transparent 0%, rgba(0,0,0,0.82) 100%);"
      >
        <!-- Progress Bar -->
        <div
          ref="progressRef"
          class="relative h-1.5 hover:h-2.5 transition-all rounded-full bg-white/25 cursor-pointer mb-3 group/bar"
          @click="seekByClick"
        >
          <div
            class="absolute inset-y-0 left-0 rounded-full bg-white/40"
            :style="{ width: `${bufferedPercent}%` }"
          />
          <div
            class="absolute inset-y-0 left-0 rounded-full"
            :style="{ width: `${playedPercent}%`, background: 'linear-gradient(90deg, #fb7299, #00aeec)' }"
          />
        </div>

        <!-- Controls Row -->
        <div class="flex items-center justify-between gap-3 text-white text-xs sm:text-sm">
          <div class="flex items-center gap-3">
            <button type="button" class="hover:text-[#fb7299] cursor-pointer" @click="togglePlay">
              <el-icon :size="20">
                <VideoPause v-if="isPlaying" />
                <VideoPlay v-else />
              </el-icon>
            </button>
            <span class="font-mono text-xs text-white/90">
              {{ formatSeconds(currentTime) }} / {{ formatSeconds(duration) }}
            </span>
          </div>

          <div class="flex items-center gap-3">
            <!-- Quality Selector -->
            <el-dropdown v-if="qualities.length > 0" trigger="click" @command="switchQuality">
              <span class="text-xs font-semibold text-white/90 hover:text-[#fb7299] cursor-pointer">
                {{ currentQualityLabel }}
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    v-for="(q, idx) in qualities"
                    :key="q.url"
                    :command="idx"
                    :class="idx === currentQualityIdx ? '!text-[#fb7299] font-bold' : ''"
                  >
                    {{ q.label }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>

            <!-- Playback Speed -->
            <el-dropdown trigger="click" @command="changeSpeed">
              <span class="text-xs font-semibold text-white/90 hover:text-[#fb7299] cursor-pointer">
                {{ playbackRate === 1 ? '倍速' : `${playbackRate}x` }}
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item v-for="rate in [2, 1.5, 1.25, 1, 0.75, 0.5]" :key="rate" :command="rate">
                    {{ rate }}x
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>

            <!-- Volume -->
            <div class="hidden sm:flex items-center gap-1.5">
              <button type="button" class="hover:text-[#fb7299] cursor-pointer" @click="toggleMute">
                <el-icon :size="17">
                  <Mute v-if="muted || volume === 0" />
                  <Microphone v-else />
                </el-icon>
              </button>
              <input
                v-model.number="volume"
                type="range"
                min="0"
                max="100"
                class="w-16 accent-[#fb7299] cursor-pointer"
                @input="applyVolume"
              />
            </div>

            <!-- Mirror Toggle -->
            <button
              type="button"
              class="text-xs hover:text-[#fb7299] cursor-pointer hidden md:inline"
              :class="mirrored ? 'text-[#fb7299] font-bold' : 'text-white/80'"
              title="画面镜像"
              @click="mirrored = !mirrored"
            >
              镜像
            </button>

            <!-- Picture in Picture -->
            <button type="button" class="hover:text-[#fb7299] cursor-pointer hidden sm:inline" title="画中画" @click="togglePiP">
              <el-icon :size="17"><CopyDocument /></el-icon>
            </button>

            <!-- Web Fullscreen -->
            <button
              type="button"
              class="hover:text-[#fb7299] cursor-pointer"
              :title="webFullscreen ? '退出网页全屏' : '网页全屏'"
              @click="webFullscreen = !webFullscreen"
            >
              <el-icon :size="17"><Monitor /></el-icon>
            </button>

            <!-- Native Fullscreen -->
            <button type="button" class="hover:text-[#fb7299] cursor-pointer" title="全屏" @click="toggleFullscreen">
              <el-icon :size="17"><FullScreen /></el-icon>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Danmaku Control & Send Bar -->
    <div
      class="px-4 py-2.5 flex flex-wrap items-center justify-between gap-3 border-t"
      style="background-color: var(--yb-surface); border-color: var(--yb-border);"
    >
      <div class="flex items-center gap-3 text-xs" style="color: var(--yb-text-secondary);">
        <span>已装填 <strong>{{ barrages.length }}</strong> 条弹幕</span>
        <el-switch v-model="danmakuEnabled" size="small" active-text="弹幕" />
        <el-popover placement="top" :width="230" trigger="click">
          <template #reference>
            <button
              type="button"
              class="px-2 py-1 rounded-lg text-xs hover:bg-[var(--yb-surface-muted)] cursor-pointer flex items-center gap-1"
            >
              <el-icon><Setting /></el-icon>
              样式
            </button>
          </template>
          <div class="space-y-3">
            <div>
              <div class="text-xs font-medium mb-1.5">弹幕位置</div>
              <el-radio-group v-model="dmType" size="small">
                <el-radio-button value="scroll">滚动</el-radio-button>
                <el-radio-button value="top">顶部</el-radio-button>
                <el-radio-button value="bottom">底部</el-radio-button>
              </el-radio-group>
            </div>
            <div>
              <div class="text-xs font-medium mb-1.5">弹幕颜色</div>
              <div class="flex flex-wrap gap-1.5">
                <button
                  v-for="c in colorPalette"
                  :key="c"
                  type="button"
                  class="w-6 h-6 rounded-full border-2 cursor-pointer transition-transform hover:scale-110"
                  :style="{ backgroundColor: c, borderColor: dmColor === c ? '#fb7299' : '#cbd5e1' }"
                  @click="dmColor = c"
                />
              </div>
            </div>
            <div>
              <div class="text-xs font-medium mb-1">不透明度 {{ danmakuOpacity }}%</div>
              <el-slider v-model="danmakuOpacity" :min="20" :max="100" size="small" />
            </div>
          </div>
        </el-popover>
      </div>

      <form class="flex-1 min-w-[240px] max-w-lg flex items-center gap-2" @submit.prevent="handleSendDanmaku">
        <input
          v-model="dmText"
          type="text"
          maxlength="100"
          placeholder="发个友善的弹幕见证当下..."
          class="flex-1 h-9 px-3.5 rounded-full text-sm border outline-none transition-colors"
          style="background-color: var(--yb-surface-muted); border-color: var(--yb-border); color: var(--yb-text);"
        />
        <button
          type="submit"
          class="yb-brand-btn h-9 px-5 rounded-full text-sm font-semibold shrink-0 cursor-pointer"
        >
          发送
        </button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import Hls from 'hls.js'
import type { BarrageItem, QualityOption } from '@/types/api'
import { formatSeconds } from '@/utils/format'

const props = defineProps<{
  src: string
  poster?: string
  qualities: QualityOption[]
  barrages: BarrageItem[]
}>()

const emit = defineEmits<{
  (e: 'send-barrage', payload: { time: number; text: string; color: string; type: 'scroll' | 'top' | 'bottom' }): void
  (e: 'watch-progress', seconds: number): void
}>()

const containerRef = ref<HTMLElement | null>(null)
const videoRef = ref<HTMLVideoElement | null>(null)
const progressRef = ref<HTMLElement | null>(null)

const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(0)
const bufferedPercent = ref(0)
const volume = ref(80)
const muted = ref(false)
const playbackRate = ref(1)
const mirrored = ref(false)
const webFullscreen = ref(false)
const currentQualityIdx = ref(0)

// Danmaku state
const danmakuEnabled = ref(true)
const danmakuOpacity = ref(90)
const dmText = ref('')
const dmColor = ref('#FFFFFF')
const dmType = ref<'scroll' | 'top' | 'bottom'>('scroll')
const colorPalette = ['#FFFFFF', '#FB7299', '#00AEEC', '#FFD700', '#4ADE80', '#C084FC', '#FF6B6B', '#FB923C']

interface ActiveDanmaku extends BarrageItem {
  uid: number
  topPercent: number
}

const activeDanmaku = ref<ActiveDanmaku[]>([])
let uidSeq = 1
let lastCheckedSecond = -1
let lastReportedSecond = -1
let trackCursor = 0
let hlsInstance: Hls | null = null

const playedPercent = computed(() => (duration.value > 0 ? (currentTime.value / duration.value) * 100 : 0))
const currentQualityLabel = computed(
  () => props.qualities[currentQualityIdx.value]?.label || '原画'
)

function attachSource(url: string, resumeAt = 0, autoPlay = false) {
  const video = videoRef.value
  if (!video || !url) return

  if (hlsInstance) {
    hlsInstance.destroy()
    hlsInstance = null
  }

  if (url.includes('.m3u8') && Hls.isSupported()) {
    hlsInstance = new Hls()
    hlsInstance.loadSource(url)
    hlsInstance.attachMedia(video)
    hlsInstance.on(Hls.Events.MANIFEST_PARSED, () => {
      if (resumeAt > 0) video.currentTime = resumeAt
      if (autoPlay) video.play().catch(() => {})
    })
  } else {
    video.src = url
    video.load()
    if (resumeAt > 0) {
      video.addEventListener(
        'loadedmetadata',
        () => {
          video.currentTime = resumeAt
          if (autoPlay) video.play().catch(() => {})
        },
        { once: true }
      )
    } else if (autoPlay) {
      video.play().catch(() => {})
    }
  }
}

watch(
  () => props.src,
  (newSrc) => {
    currentQualityIdx.value = 0
    activeDanmaku.value = []
    lastCheckedSecond = -1
    if (newSrc) attachSource(newSrc)
  }
)

function togglePlay() {
  const video = videoRef.value
  if (!video) return
  if (video.paused) {
    video.play().catch(() => {})
  } else {
    video.pause()
  }
}

function onLoadedMetadata() {
  if (!videoRef.value) return
  duration.value = videoRef.value.duration || 0
  applyVolume()
}

function onTimeUpdate() {
  const video = videoRef.value
  if (!video) return
  currentTime.value = video.currentTime

  // Trigger barrages matching current playback second
  const currentSec = Math.floor(video.currentTime)
  if (currentSec !== lastCheckedSecond && danmakuEnabled.value) {
    const prevSec = lastCheckedSecond
    lastCheckedSecond = currentSec
    if (prevSec >= 0 && currentSec >= prevSec && currentSec - prevSec <= 2) {
      for (const b of props.barrages) {
        if (b.time > prevSec && b.time <= currentSec) {
          spawnDanmaku(b)
        }
      }
    }
  }

  // Report watch progress every 15 seconds
  if (currentSec > 0 && currentSec % 15 === 0 && currentSec !== lastReportedSecond) {
    lastReportedSecond = currentSec
    emit('watch-progress', currentSec)
  }
}

function onProgress() {
  const video = videoRef.value
  if (!video || !duration.value || video.buffered.length === 0) return
  const end = video.buffered.end(video.buffered.length - 1)
  bufferedPercent.value = Math.min(100, (end / duration.value) * 100)
}

function seekByClick(event: MouseEvent) {
  const bar = progressRef.value
  const video = videoRef.value
  if (!bar || !video || !duration.value) return
  const rect = bar.getBoundingClientRect()
  const ratio = Math.min(1, Math.max(0, (event.clientX - rect.left) / rect.width))
  video.currentTime = ratio * duration.value
  lastCheckedSecond = Math.floor(video.currentTime)
}

function applyVolume() {
  if (!videoRef.value) return
  videoRef.value.volume = volume.value / 100
  videoRef.value.muted = muted.value
}

function toggleMute() {
  muted.value = !muted.value
  applyVolume()
}

function changeSpeed(rate: number) {
  playbackRate.value = rate
  if (videoRef.value) {
    videoRef.value.playbackRate = rate
  }
}

function switchQuality(idx: number) {
  const option = props.qualities[idx]
  if (!option || idx === currentQualityIdx.value) return
  const resumeAt = videoRef.value?.currentTime || 0
  const wasPlaying = isPlaying.value
  currentQualityIdx.value = idx
  attachSource(option.url, resumeAt, wasPlaying)
}

async function togglePiP() {
  try {
    if (document.pictureInPictureElement) {
      await document.exitPictureInPicture()
    } else if (videoRef.value && document.pictureInPictureEnabled) {
      await videoRef.value.requestPictureInPicture()
    }
  } catch {
    // 浏览器不支持画中画时静默忽略
  }
}

async function toggleFullscreen() {
  const el = containerRef.value
  if (!el) return
  if (document.fullscreenElement) {
    await document.exitFullscreen()
  } else {
    await el.requestFullscreen()
  }
}

function spawnDanmaku(item: BarrageItem) {
  const topPercent =
    item.type === 'top'
      ? 8 + (trackCursor % 4) * 7
      : item.type === 'bottom'
        ? 72 - (trackCursor % 3) * 7
        : 6 + (trackCursor % 8) * 9
  trackCursor += 1

  activeDanmaku.value.push({
    ...item,
    uid: uidSeq++,
    topPercent
  })
}

function removeActiveDanmaku(uid: number) {
  activeDanmaku.value = activeDanmaku.value.filter((d) => d.uid !== uid)
}

function handleSendDanmaku() {
  const text = dmText.value.trim()
  if (!text) return
  const payload = {
    time: Number((videoRef.value?.currentTime || 0).toFixed(2)),
    text,
    color: dmColor.value,
    type: dmType.value
  }
  spawnDanmaku(payload)
  emit('send-barrage', payload)
  dmText.value = ''
}

function onKeyDown(e: KeyboardEvent) {
  const tag = (e.target as HTMLElement)?.tagName
  if (tag === 'INPUT' || tag === 'TEXTAREA') return
  const video = videoRef.value
  if (!video) return

  if (e.code === 'Space') {
    e.preventDefault()
    togglePlay()
  } else if (e.code === 'ArrowRight') {
    video.currentTime = Math.min(duration.value, video.currentTime + 5)
  } else if (e.code === 'ArrowLeft') {
    video.currentTime = Math.max(0, video.currentTime - 5)
  } else if (e.key.toLowerCase() === 'm') {
    toggleMute()
  } else if (e.key.toLowerCase() === 'f') {
    toggleFullscreen()
  }
}

onMounted(() => {
  if (props.src) attachSource(props.src)
  window.addEventListener('keydown', onKeyDown)
})

onUnmounted(() => {
  window.removeEventListener('keydown', onKeyDown)
  if (hlsInstance) hlsInstance.destroy()
})
</script>

<style scoped>
@keyframes dm-scroll {
  from {
    left: 100%;
    transform: translateX(0);
  }
  to {
    left: 0;
    transform: translateX(-110%);
  }
}

@keyframes dm-fixed {
  0% {
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}

.yb-dm-scroll {
  animation: dm-scroll 8.5s linear forwards;
}

.yb-dm-fixed {
  animation: dm-fixed 4s ease forwards;
}
</style>
