import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { authApi } from '@/api/modules'
import { getStoredToken, setStoredToken } from '@/api/http'
import type { UserProfile } from '@/types/api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(getStoredToken())
  const user = ref<UserProfile | null>(null)
  const initialized = ref(false)
  const authModalVisible = ref(false)
  const authModalTab = ref<'login' | 'register'>('login')

  const isLoggedIn = computed(() => Boolean(token.value && user.value))
  const isVip = computed(() => user.value?.membershipStatus === 'Active')

  async function initAuth() {
    if (!token.value) {
      initialized.value = true
      return
    }
    try {
      user.value = await authApi.me()
    } catch {
      clearSession()
    } finally {
      initialized.value = true
    }
  }

  async function login(username: string, password: string) {
    const res = await authApi.login({ username, password })
    token.value = res.token
    setStoredToken(res.token)
    user.value = res.user
    authModalVisible.value = false
    return res
  }

  async function refreshProfile() {
    if (!token.value) return
    user.value = await authApi.me()
  }

  async function logout() {
    try {
      if (token.value) {
        await authApi.logout()
      }
    } catch {
      // 忽略网络错误，始终清理本地会话
    } finally {
      clearSession()
    }
  }

  function clearSession() {
    token.value = null
    user.value = null
    setStoredToken(null)
  }

  function openAuthModal(tab: 'login' | 'register' = 'login') {
    authModalTab.value = tab
    authModalVisible.value = true
  }

  function closeAuthModal() {
    authModalVisible.value = false
  }

  return {
    token,
    user,
    initialized,
    authModalVisible,
    authModalTab,
    isLoggedIn,
    isVip,
    initAuth,
    login,
    refreshProfile,
    logout,
    clearSession,
    openAuthModal,
    closeAuthModal
  }
})
