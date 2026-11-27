<template>
  <el-dialog
    v-model="authStore.authModalVisible"
    width="440px"
    align-center
    destroy-on-close
    class="!rounded-2xl"
    :show-close="true"
  >
    <template #header>
      <div class="text-center pt-1">
        <div class="inline-flex items-center gap-2 mb-1">
          <div
            class="w-8 h-8 rounded-xl flex items-center justify-center"
            style="background: linear-gradient(135deg, #fb7299 0%, #ff5c8a 100%);"
          >
            <img src="/mLogo.png" alt="YouBili" class="w-5 h-5 object-contain brightness-0 invert" />
          </div>
          <span class="text-xl font-extrabold">
            You<span style="color: var(--yb-brand);">Bili</span>
          </span>
        </div>
        <p class="text-xs" style="color: var(--yb-text-muted);">
          发弹幕、追 UP 主、同步多端观看进度
        </p>
      </div>
    </template>

    <!-- Tab Switcher -->
    <div class="grid grid-cols-2 p-1 rounded-xl mb-5" style="background-color: var(--yb-surface-muted);">
      <button
        type="button"
        class="py-2 rounded-lg text-sm font-semibold transition-all cursor-pointer"
        :style="authStore.authModalTab === 'login'
          ? { backgroundColor: 'var(--yb-surface)', color: 'var(--yb-brand)', boxShadow: 'var(--yb-shadow-sm)' }
          : { color: 'var(--yb-text-secondary)' }"
        @click="authStore.authModalTab = 'login'"
      >
        密码登录
      </button>
      <button
        type="button"
        class="py-2 rounded-lg text-sm font-semibold transition-all cursor-pointer"
        :style="authStore.authModalTab === 'register'
          ? { backgroundColor: 'var(--yb-surface)', color: 'var(--yb-brand)', boxShadow: 'var(--yb-shadow-sm)' }
          : { color: 'var(--yb-text-secondary)' }"
        @click="authStore.authModalTab = 'register'"
      >
        邮箱注册
      </button>
    </div>

    <!-- Login Form -->
    <el-form
      v-if="authStore.authModalTab === 'login'"
      ref="loginFormRef"
      :model="loginForm"
      :rules="loginRules"
      label-position="top"
      @submit.prevent="submitLogin"
    >
      <el-form-item label="用户名" prop="username">
        <el-input v-model="loginForm.username" placeholder="请输入用户名" size="large" prefix-icon="User" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          show-password
          placeholder="请输入密码"
          size="large"
          prefix-icon="Lock"
          @keyup.enter="submitLogin"
        />
      </el-form-item>
      <el-button
        type="primary"
        size="large"
        class="w-full !rounded-xl !font-semibold mt-2"
        :loading="submitting"
        @click="submitLogin"
      >
        立即登录
      </el-button>
    </el-form>

    <!-- Register Form -->
    <el-form
      v-else
      ref="registerFormRef"
      :model="registerForm"
      :rules="registerRules"
      label-position="top"
      @submit.prevent="submitRegister"
    >
      <div class="grid grid-cols-2 gap-3">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="4-24位字母数字" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="registerForm.nickname" placeholder="展示昵称" />
        </el-form-item>
      </div>
      <div class="grid grid-cols-2 gap-3">
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" show-password placeholder="至少6位" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" show-password placeholder="再次输入" />
        </el-form-item>
      </div>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="registerForm.email" placeholder="用于接收验证码" />
      </el-form-item>
      <el-form-item label="邮箱验证码" prop="code">
        <div class="flex gap-2 w-full">
          <el-input v-model="registerForm.code" placeholder="6位数字验证码" maxlength="6" class="flex-1" />
          <el-button :disabled="cooldown > 0 || sendingCode" :loading="sendingCode" @click="sendCode">
            {{ cooldown > 0 ? `${cooldown}s 后重试` : '获取验证码' }}
          </el-button>
        </div>
      </el-form-item>
      <el-button
        type="primary"
        size="large"
        class="w-full !rounded-xl !font-semibold mt-1"
        :loading="submitting"
        @click="submitRegister"
      >
        完成注册
      </el-button>
    </el-form>
  </el-dialog>
</template>

<script setup lang="ts">
import { onUnmounted, reactive, ref } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { authApi } from '@/api/modules'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

const loginFormRef = ref<FormInstance>()
const registerFormRef = ref<FormInstance>()
const submitting = ref(false)
const sendingCode = ref(false)
const cooldown = ref(0)
let timer: ReturnType<typeof setInterval> | null = null

const loginForm = reactive({
  username: '',
  password: ''
})

const registerForm = reactive({
  username: '',
  nickname: '',
  password: '',
  confirmPassword: '',
  email: '',
  code: ''
})

const loginRules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const registerRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]{4,24}$/, message: '4-24位字母、数字或下划线', trigger: 'blur' }
  ],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 32, message: '密码长度 6-32 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { pattern: /^\d{6}$/, message: '验证码需为 6 位数字', trigger: 'blur' }
  ]
}

async function submitLogin() {
  if (!loginFormRef.value) return
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    await authStore.login(loginForm.username.trim(), loginForm.password)
    ElMessage.success('欢迎回来！')
  } finally {
    submitting.value = false
  }
}

async function sendCode() {
  if (!registerFormRef.value) return
  const valid = await registerFormRef.value.validateField('email').then(() => true).catch(() => false)
  if (!valid) return

  sendingCode.value = true
  try {
    await authApi.sendEmailCode(registerForm.email.trim())
    ElMessage.success('验证码已发送，请查收邮件')
    cooldown.value = 60
    timer = setInterval(() => {
      cooldown.value -= 1
      if (cooldown.value <= 0 && timer) {
        clearInterval(timer)
        timer = null
      }
    }, 1000)
  } finally {
    sendingCode.value = false
  }
}

async function submitRegister() {
  if (!registerFormRef.value) return
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    await authApi.register({
      username: registerForm.username.trim(),
      nickname: registerForm.nickname.trim(),
      password: registerForm.password,
      email: registerForm.email.trim(),
      code: registerForm.code.trim()
    })
    ElMessage.success('注册成功，请登录！')
    loginForm.username = registerForm.username.trim()
    loginForm.password = ''
    authStore.authModalTab = 'login'
  } finally {
    submitting.value = false
  }
}

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>
