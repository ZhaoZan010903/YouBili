<template>
  <div class="max-w-4xl mx-auto space-y-6">
    <div class="yb-card p-6">
      <div class="mb-6">
        <h1 class="text-2xl font-extrabold">创作中心 · 发布视频</h1>
        <p class="text-xs mt-1" style="color: var(--yb-text-muted);">
          支持 MP4 / WebM / MOV 视频与 JPG / PNG / WebP 封面上传，最大单文件 500MB
        </p>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" class="space-y-2">
        <!-- Step 1: Video Upload -->
        <el-form-item label="1. 上传视频文件" prop="videoSrcUrl">
          <div class="w-full">
            <el-upload
              drag
              action="#"
              accept=".mp4,.webm,.mov"
              :limit="1"
              :show-file-list="false"
              :http-request="handleVideoUpload"
              class="w-full"
            >
              <el-icon class="el-icon--upload" :size="44"><UploadFilled /></el-icon>
              <div class="el-upload__text">
                将视频文件拖到此处，或 <em>点击选择文件</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  推荐 16:9 比例 1080P MP4 格式，上传时会自动解析视频时长
                </div>
              </template>
            </el-upload>

            <div v-if="uploadingVideo || form.videoSrcUrl" class="mt-3 p-3.5 rounded-xl border space-y-2" style="background-color: var(--yb-surface-muted); border-color: var(--yb-border);">
              <div class="flex items-center justify-between text-xs">
                <span class="font-medium truncate">{{ uploadedVideoName || '已上传视频' }}</span>
                <span v-if="form.duration" class="font-mono" style="color: var(--yb-text-muted);">
                  时长 {{ form.duration }}
                </span>
              </div>
              <el-progress
                :percentage="uploadProgress"
                :status="form.videoSrcUrl ? 'success' : undefined"
                :stroke-width="10"
              />
            </div>
          </div>
        </el-form-item>

        <!-- Step 2: Cover Upload -->
        <el-form-item label="2. 上传视频封面" prop="videoImgUrl">
          <div class="flex flex-col sm:flex-row items-start gap-4 w-full">
            <div
              class="w-full sm:w-64 aspect-video rounded-xl overflow-hidden border flex items-center justify-center shrink-0"
              style="background-color: var(--yb-surface-muted); border-color: var(--yb-border);"
            >
              <img
                v-if="form.videoImgUrl"
                :src="form.videoImgUrl"
                alt="视频封面预览"
                class="w-full h-full object-cover"
              />
              <div v-else class="text-center p-4 text-xs" style="color: var(--yb-text-muted);">
                <el-icon :size="28"><Picture /></el-icon>
                <div class="mt-1">16:9 封面预览</div>
              </div>
            </div>

            <div class="space-y-2">
              <el-upload
                action="#"
                accept=".jpg,.jpeg,.png,.webp"
                :show-file-list="false"
                :http-request="handleCoverUpload"
              >
                <el-button type="primary" plain round :loading="uploadingCover">
                  <el-icon class="mr-1"><Upload /></el-icon>
                  {{ form.videoImgUrl ? '更换封面' : '选择封面图片' }}
                </el-button>
              </el-upload>
              <p class="text-xs" style="color: var(--yb-text-muted);">
                支持 JPG、PNG、WebP 格式，建议分辨率 1280×720 以上
              </p>
            </div>
          </div>
        </el-form-item>

        <!-- Step 3: Metadata -->
        <el-form-item label="3. 视频标题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="起一个吸引人的标题（2-100字）"
            maxlength="100"
            show-word-limit
            size="large"
          />
        </el-form-item>

        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <el-form-item label="4. 视频分区" prop="categoryId">
            <el-select v-model="form.categoryId" placeholder="请选择视频分区" class="w-full" size="large">
              <el-option
                v-for="cat in categories"
                :key="cat.categoryId"
                :label="cat.categoryName"
                :value="cat.categoryId"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="5. 视频标签（可多选）" prop="tagIds">
            <el-select
              v-model="form.tagIds"
              multiple
              collapse-tags
              collapse-tags-tooltip
              placeholder="选择内容标签"
              class="w-full"
              size="large"
            >
              <el-option
                v-for="tag in tags"
                :key="tag.tagId"
                :label="tag.tagName"
                :value="tag.tagId"
              />
            </el-select>
          </el-form-item>
        </div>

        <el-form-item label="6. 视频简介" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="介绍一下视频的主要内容、亮点或参考资料..."
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>

        <div class="pt-4 flex items-center justify-end gap-3">
          <el-button round @click="resetForm">重置表单</el-button>
          <el-button
            type="primary"
            round
            size="large"
            :loading="publishing"
            @click="submitPublish"
          >
            立即发布投稿
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules, type UploadRequestOptions } from 'element-plus'
import { categoryApi, videoApi } from '@/api/modules'
import { useAuthStore } from '@/stores/auth'
import type { CategoryItem, TagItem } from '@/types/api'

const router = useRouter()
const authStore = useAuthStore()

const formRef = ref<FormInstance>()
const categories = ref<CategoryItem[]>([])
const tags = ref<TagItem[]>([])
const uploadingVideo = ref(false)
const uploadingCover = ref(false)
const uploadProgress = ref(0)
const uploadedVideoName = ref('')
const publishing = ref(false)

const form = reactive({
  title: '',
  description: '',
  videoSrcUrl: '',
  videoImgUrl: '',
  duration: '00:00:00',
  categoryId: undefined as number | undefined,
  tagIds: [] as number[]
})

const rules: FormRules = {
  videoSrcUrl: [{ required: true, message: '请先上传视频文件', trigger: 'change' }],
  videoImgUrl: [{ required: true, message: '请先上传视频封面', trigger: 'change' }],
  title: [
    { required: true, message: '请输入视频标题', trigger: 'blur' },
    { min: 2, max: 100, message: '标题长度需在 2-100 字之间', trigger: 'blur' }
  ],
  categoryId: [{ required: true, message: '请选择视频分区', trigger: 'change' }],
  description: [{ required: true, message: '请输入视频简介', trigger: 'blur' }]
}

function detectVideoDuration(file: File): Promise<string> {
  return new Promise((resolve) => {
    const url = URL.createObjectURL(file)
    const videoEl = document.createElement('video')
    videoEl.preload = 'metadata'
    videoEl.onloadedmetadata = () => {
      const sec = Math.max(0, Math.floor(videoEl.duration || 0))
      URL.revokeObjectURL(url)
      const h = String(Math.floor(sec / 3600)).padStart(2, '0')
      const m = String(Math.floor((sec % 3600) / 60)).padStart(2, '0')
      const s = String(sec % 60).padStart(2, '0')
      resolve(`${h}:${m}:${s}`)
    }
    videoEl.onerror = () => {
      URL.revokeObjectURL(url)
      resolve('00:00:00')
    }
    videoEl.src = url
  })
}

async function handleVideoUpload(options: UploadRequestOptions) {
  const file = options.file as File
  uploadingVideo.value = true
  uploadProgress.value = 0
  uploadedVideoName.value = file.name

  try {
    const [duration, result] = await Promise.all([
      detectVideoDuration(file),
      videoApi.uploadVideo(file, (pct) => {
        uploadProgress.value = pct
      })
    ])
    form.videoSrcUrl = result.url
    form.duration = duration
    if (!form.title) {
      form.title = file.name.replace(/\.[^.]+$/, '').slice(0, 100)
    }
    ElMessage.success('视频上传成功')
  } finally {
    uploadingVideo.value = false
  }
}

async function handleCoverUpload(options: UploadRequestOptions) {
  uploadingCover.value = true
  try {
    const result = await videoApi.uploadCover(options.file as File)
    form.videoImgUrl = result.url
    ElMessage.success('封面上传成功')
  } finally {
    uploadingCover.value = false
  }
}

async function submitPublish() {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid || !form.categoryId) return

  publishing.value = true
  try {
    const res = await videoApi.publish({
      title: form.title.trim(),
      description: form.description.trim(),
      videoSrcUrl: form.videoSrcUrl,
      videoImgUrl: form.videoImgUrl,
      duration: form.duration,
      categoryId: form.categoryId,
      tagIds: form.tagIds
    })
    await authStore.refreshProfile()
    ElMessage.success('投稿发布成功！')
    router.push(`/video/${res.videoId}`)
  } finally {
    publishing.value = false
  }
}

function resetForm() {
  formRef.value?.resetFields()
  form.videoSrcUrl = ''
  form.videoImgUrl = ''
  form.duration = '00:00:00'
  uploadProgress.value = 0
  uploadedVideoName.value = ''
}

onMounted(async () => {
  const [cats, tagList] = await Promise.all([
    categoryApi.listCategories().catch(() => []),
    categoryApi.listTags().catch(() => [])
  ])
  categories.value = cats
  tags.value = tagList
})
</script>
