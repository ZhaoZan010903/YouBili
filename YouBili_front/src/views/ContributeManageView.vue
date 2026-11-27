<template>
  <div class="space-y-6">
    <div class="yb-card p-5 flex flex-wrap items-center justify-between gap-4">
      <div>
        <h1 class="text-xl font-bold">稿件管理</h1>
        <p class="text-xs mt-0.5" style="color: var(--yb-text-muted);">共发布 {{ total }} 篇视频稿件</p>
      </div>

      <div class="flex flex-wrap items-center gap-3">
        <el-segmented v-model="reviewStatus" :options="statusOptions" @change="onStatusChange" />
        <el-button type="primary" round @click="router.push('/contribute')">
          <el-icon class="mr-1"><Plus /></el-icon>
          新建投稿
        </el-button>
      </div>
    </div>

    <div class="yb-card p-4 overflow-x-auto">
      <el-table v-loading="loading" :data="videos" style="width: 100%" empty-text="还没有发布过稿件，点击右上角开始创作吧">
        <el-table-column label="视频稿件" min-width="320">
          <template #default="{ row }">
            <div class="flex items-center gap-3 py-1 cursor-pointer group" @click="router.push(`/video/${row.videoId}`)">
              <img
                :src="row.videoImgUrl || FALLBACK_COVER"
                class="w-28 aspect-video rounded-lg object-cover shrink-0"
                alt=""
              />
              <div class="min-w-0">
                <div class="font-semibold text-sm line-clamp-1 group-hover:text-[var(--yb-brand)]">
                  {{ row.title }}
                </div>
                <div class="text-xs line-clamp-1 mt-1" style="color: var(--yb-text-muted);">
                  {{ row.description || '无简介' }}
                </div>
                <div class="text-[11px] mt-1" style="color: var(--yb-text-muted);">
                  {{ row.uploadDate }}
                </div>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="分区" width="110">
          <template #default="{ row }">
            <el-tag size="small" effect="plain" round>{{ row.categoryName || '未分类' }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="数据表现" width="200">
          <template #default="{ row }">
            <div class="text-xs space-y-1" style="color: var(--yb-text-secondary);">
              <div>播放 {{ formatCount(row.viewCount) }} · 点赞 {{ formatCount(row.likeCount) }}</div>
              <div>弹幕 {{ formatCount(row.barrageCount) }} · 评论 {{ formatCount(row.commentCount) }}</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="审核状态" width="110">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.reviewStatus)" size="small" round>
              {{ statusLabel(row.reviewStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="170" fixed="right">
          <template #default="{ row }">
            <el-button text type="primary" size="small" @click="openEdit(row as VideoCard)">编辑</el-button>
            <el-button text type="danger" size="small" @click="handleDelete(row as VideoCard)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="total > size" class="flex justify-center mt-4">
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

    <!-- Edit Modal -->
    <el-dialog v-model="editVisible" title="编辑稿件信息" width="520px" class="!rounded-2xl">
      <el-form :model="editForm" label-position="top">
        <el-form-item label="视频标题">
          <el-input v-model="editForm.title" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="视频分区">
          <el-select v-model="editForm.categoryId" class="w-full">
            <el-option
              v-for="cat in categories"
              :key="cat.categoryId"
              :label="cat.categoryName"
              :value="cat.categoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="视频简介">
          <el-input v-model="editForm.description" type="textarea" :rows="4" maxlength="1000" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submitEdit">保存修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { categoryApi, videoApi } from '@/api/modules'
import type { CategoryItem, VideoCard } from '@/types/api'
import { FALLBACK_COVER, formatCount } from '@/utils/format'

const router = useRouter()

const loading = ref(true)
const reviewStatus = ref('')
const page = ref(1)
const size = 10
const total = ref(0)
const videos = ref<VideoCard[]>([])
const categories = ref<CategoryItem[]>([])

const editVisible = ref(false)
const saving = ref(false)
const editForm = reactive({
  videoId: 0,
  title: '',
  description: '',
  categoryId: undefined as number | undefined
})

const statusOptions = [
  { label: '全部', value: '' },
  { label: '已通过', value: 'Approved' },
  { label: '审核中', value: 'Pending' },
  { label: '未通过', value: 'Rejected' }
]

function statusLabel(status?: string) {
  if (status === 'Approved') return '已通过'
  if (status === 'Pending') return '审核中'
  if (status === 'Rejected') return '未通过'
  return status || '未知'
}

function statusTagType(status?: string) {
  if (status === 'Approved') return 'success'
  if (status === 'Pending') return 'warning'
  if (status === 'Rejected') return 'danger'
  return 'info'
}

async function loadVideos() {
  loading.value = true
  try {
    const res = await videoApi.myVideos({
      reviewStatus: reviewStatus.value || undefined,
      page: page.value,
      size
    })
    videos.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

function onStatusChange() {
  page.value = 1
  loadVideos()
}

function openEdit(row: VideoCard) {
  editForm.videoId = row.videoId
  editForm.title = row.title
  editForm.description = row.description || ''
  editForm.categoryId = row.categoryId
  editVisible.value = true
}

async function submitEdit() {
  if (!editForm.title.trim()) {
    ElMessage.warning('视频标题不能为空')
    return
  }
  saving.value = true
  try {
    await videoApi.update(editForm.videoId, {
      title: editForm.title.trim(),
      description: editForm.description.trim(),
      categoryId: editForm.categoryId
    })
    editVisible.value = false
    ElMessage.success('稿件信息已更新')
    await loadVideos()
  } finally {
    saving.value = false
  }
}

async function handleDelete(row: VideoCard) {
  try {
    await ElMessageBox.confirm(`确定删除视频稿件「${row.title}」吗？删除后将无法播放。`, '删除稿件', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await videoApi.remove(row.videoId)
    ElMessage.success('稿件已删除')
    await loadVideos()
  } catch {
    // 取消
  }
}

onMounted(async () => {
  categories.value = await categoryApi.listCategories().catch(() => [])
  await loadVideos()
})
</script>
