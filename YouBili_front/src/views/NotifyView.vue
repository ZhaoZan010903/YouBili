<template>
  <div class="max-w-4xl mx-auto space-y-6">
    <div class="yb-card p-5 flex flex-wrap items-center justify-between gap-4">
      <div class="flex items-center gap-2">
        <button
          type="button"
          class="px-5 py-2 rounded-full text-sm font-semibold transition-all cursor-pointer"
          :style="activeTab === 'personal'
            ? { backgroundColor: 'var(--yb-brand)', color: '#fff' }
            : { backgroundColor: 'var(--yb-surface-muted)', color: 'var(--yb-text-secondary)' }"
          @click="switchTab('personal')"
        >
          个人消息
        </button>
        <button
          type="button"
          class="px-5 py-2 rounded-full text-sm font-semibold transition-all cursor-pointer"
          :style="activeTab === 'system'
            ? { backgroundColor: 'var(--yb-brand)', color: '#fff' }
            : { backgroundColor: 'var(--yb-surface-muted)', color: 'var(--yb-text-secondary)' }"
          @click="switchTab('system')"
        >
          系统公告
        </button>
      </div>
      <el-button round plain :loading="loading" @click="loadNotices">刷新</el-button>
    </div>

    <div v-if="loading" class="space-y-3">
      <div v-for="n in 4" :key="n" class="yb-card p-4">
        <el-skeleton :rows="2" animated />
      </div>
    </div>

    <el-empty v-else-if="notices.length === 0" :description="activeTab === 'personal' ? '暂无个人消息' : '暂无系统公告'" />

    <div v-else class="space-y-3">
      <div
        v-for="item in notices"
        :key="item.noticeId"
        class="yb-card p-4 flex items-start justify-between gap-4"
      >
        <div class="flex items-start gap-3 min-w-0">
          <div
            class="w-10 h-10 rounded-xl flex items-center justify-center shrink-0 mt-0.5"
            :style="activeTab === 'system'
              ? { backgroundColor: 'var(--yb-brand-soft)', color: 'var(--yb-brand)' }
              : { backgroundColor: 'var(--yb-accent-soft)', color: 'var(--yb-accent)' }"
          >
            <el-icon :size="18">
              <BellFilled v-if="activeTab === 'system'" />
              <Message v-else />
            </el-icon>
          </div>

          <div class="min-w-0 space-y-1">
            <div class="flex items-center gap-2">
              <span class="font-semibold text-sm">{{ item.systemName || 'YouBili 系统' }}</span>
              <el-tag v-if="activeTab === 'personal' && item.isRead === 0" size="small" type="danger" effect="dark" round>
                未读
              </el-tag>
              <span class="text-xs" style="color: var(--yb-text-muted);">{{ formatRelativeTime(item.noticeTime) }}</span>
            </div>
            <p class="text-sm leading-relaxed" style="color: var(--yb-text-secondary);">{{ item.noticeText }}</p>
          </div>
        </div>

        <div v-if="activeTab === 'personal'" class="shrink-0 flex items-center gap-1">
          <el-button
            v-if="item.isRead === 0"
            text
            size="small"
            type="primary"
            @click="markRead(item)"
          >
            标为已读
          </el-button>
          <el-button
            text
            size="small"
            type="danger"
            @click="deleteItem(item.noticeId)"
          >
            删除
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { socialApi } from '@/api/modules'
import type { NoticeItem } from '@/types/api'
import { formatRelativeTime } from '@/utils/format'

const activeTab = ref<'personal' | 'system'>('personal')
const loading = ref(true)
const notices = ref<NoticeItem[]>([])

async function loadNotices() {
  loading.value = true
  try {
    notices.value = await socialApi.listNotices(activeTab.value)
  } finally {
    loading.value = false
  }
}

function switchTab(tab: 'personal' | 'system') {
  activeTab.value = tab
  loadNotices()
}

async function markRead(item: NoticeItem) {
  await socialApi.markNoticeRead(item.noticeId)
  item.isRead = 1
}

async function deleteItem(noticeId: number) {
  await socialApi.deleteNotice(noticeId)
  notices.value = notices.value.filter((n) => n.noticeId !== noticeId)
  ElMessage.success('通知已删除')
}

onMounted(loadNotices)
</script>
