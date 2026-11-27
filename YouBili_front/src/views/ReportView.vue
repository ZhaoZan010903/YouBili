<template>
  <div class="max-w-2xl mx-auto">
    <div class="yb-card p-6 sm:p-8 space-y-6">
      <div>
        <h1 class="text-2xl font-extrabold">违规内容举报</h1>
        <p class="text-xs mt-1" style="color: var(--yb-text-muted);">
          感谢您协助维护 YouBili 社区环境，请如实填写违规信息以便审核团队核查处理。
        </p>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <el-form-item label="举报对象类型" prop="targetType">
            <el-select v-model="form.targetType" class="w-full">
              <el-option label="视频稿件" value="VIDEO" />
              <el-option label="评论/回复" value="COMMENT" />
              <el-option label="用户账号" value="USER" />
            </el-select>
          </el-form-item>

          <el-form-item label="举报对象 ID" prop="targetId">
            <el-input-number v-model="form.targetId" :min="1" class="!w-full" controls-position="right" />
          </el-form-item>
        </div>

        <el-form-item v-if="targetTitle" label="关联内容标题">
          <el-input :model-value="targetTitle" disabled />
        </el-form-item>

        <el-form-item label="违规性质" prop="category">
          <el-select v-model="form.category" placeholder="请选择主要违规类别" class="w-full" @change="form.reasons = []">
            <el-option v-for="cat in categoryList" :key="cat" :label="cat" :value="cat" />
          </el-select>
        </el-form-item>

        <el-form-item v-if="currentReasons.length > 0" label="具体违规标签（可多选）">
          <el-checkbox-group v-model="form.reasons">
            <el-checkbox v-for="r in currentReasons" :key="r" :value="r" :label="r" />
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="违规详细描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="5"
            maxlength="1000"
            show-word-limit
            placeholder="请具体描述违规出现的时间点、言论或行为..."
          />
        </el-form-item>

        <div class="flex justify-end gap-3 pt-2">
          <el-button round @click="router.back()">返回</el-button>
          <el-button type="primary" round :loading="submitting" @click="submit">
            提交举报
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { socialApi } from '@/api/modules'

const route = useRoute()
const router = useRouter()

const formRef = ref<FormInstance>()
const submitting = ref(false)
const targetTitle = computed(() => String(route.query.title || ''))

const reasonMap: Record<string, string[]> = {
  违法违规: ['涉黄低俗', '暴力血腥', '赌博诈骗', '违禁品推广'],
  侵权抄袭: ['未经授权搬运', '侵犯肖像/隐私权', '盗用他人原创素材'],
  人身攻击与引战: ['恶意辱骂', '人身攻击', '刻意引战拉踩', '散布谣言'],
  垃圾广告与刷屏: ['恶意引流广告', '虚假中奖信息', '高频无意义刷屏'],
  其他违规: ['标题封面党', '内容引人不适', '其他社区违规行为']
}

const categoryList = Object.keys(reasonMap)

const form = reactive({
  targetType: (String(route.query.targetType || 'VIDEO') as 'VIDEO' | 'COMMENT' | 'USER'),
  targetId: Number(route.query.targetId) || 1,
  category: '违法违规',
  reasons: [] as string[],
  description: ''
})

const currentReasons = computed(() => reasonMap[form.category] || [])

const rules: FormRules = {
  targetType: [{ required: true, message: '请选择目标类型', trigger: 'change' }],
  targetId: [{ required: true, message: '请输入目标 ID', trigger: 'blur' }],
  category: [{ required: true, message: '请选择违规性质', trigger: 'change' }],
  description: [
    { required: true, message: '请填写违规详细描述', trigger: 'blur' },
    { min: 5, message: '请至少填写 5 个字的详细说明', trigger: 'blur' }
  ]
}

async function submit() {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    await socialApi.submitReport({
      targetType: form.targetType,
      targetId: form.targetId,
      category: form.category,
      reasons: form.reasons,
      description: form.description.trim()
    })
    ElMessage.success('举报已提交，我们会尽快审核处理')
    router.back()
  } finally {
    submitting.value = false
  }
}
</script>
