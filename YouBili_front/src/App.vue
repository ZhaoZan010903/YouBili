<template>
  <router-view />
  <AuthModal />
</template>

<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue'
import AuthModal from '@/components/AuthModal.vue'
import { useAuthStore } from '@/stores/auth'
import { useUiStore } from '@/stores/ui'

const authStore = useAuthStore()
const uiStore = useUiStore()

function handleAuthExpired() {
  authStore.clearSession()
  authStore.openAuthModal('login')
}

onMounted(() => {
  uiStore.initTheme()
  window.addEventListener('youbili:auth-expired', handleAuthExpired)
})

onUnmounted(() => {
  window.removeEventListener('youbili:auth-expired', handleAuthExpired)
})
</script>
