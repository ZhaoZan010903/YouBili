<template>
  <nav class="h-full overflow-y-auto py-4 px-2.5 select-none">
    <div v-for="(section, sIdx) in navGroups" :key="section.group" :class="{ 'mt-4 pt-4 border-t': sIdx > 0 }" style="border-color: var(--yb-border);">
      <div
        v-if="!collapsed"
        class="px-3 mb-2 text-[11px] font-semibold uppercase tracking-wider"
        style="color: var(--yb-text-muted);"
      >
        {{ section.group }}
      </div>

      <div class="space-y-1">
        <el-tooltip
          v-for="item in section.items"
          :key="item.path"
          :content="item.title"
          placement="right"
          :disabled="!collapsed"
        >
          <button
            type="button"
            class="w-full flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium transition-all cursor-pointer"
            :class="collapsed ? 'justify-center' : ''"
            :style="isActive(item.path)
              ? { backgroundColor: 'var(--yb-brand-soft)', color: 'var(--yb-brand)' }
              : { color: 'var(--yb-text-secondary)' }"
            @click="handleSelect(item.path, item.requiresAuth)"
          >
            <el-icon :size="19">
              <component :is="item.icon" />
            </el-icon>
            <span v-if="!collapsed" class="truncate">{{ item.title }}</span>
          </button>
        </el-tooltip>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

defineProps<{
  collapsed: boolean
}>()

const emit = defineEmits<{
  (e: 'navigate'): void
}>()

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

interface NavItem {
  path: string
  title: string
  icon: string
  requiresAuth: boolean
}

const navGroups = computed(() => {
  const root = router.options.routes.find((r) => r.path === '/')
  const children = root?.children ?? []
  const groupMap = new Map<string, NavItem[]>()

  for (const child of children) {
    if (!child.meta?.showInSidebar || !child.meta.group) continue
    const list = groupMap.get(child.meta.group) ?? []
    list.push({
      path: `/${child.path}`,
      title: child.meta.title ?? child.path,
      icon: child.meta.icon ?? 'Menu',
      requiresAuth: Boolean(child.meta.requiresAuth)
    })
    groupMap.set(child.meta.group, list)
  }

  return Array.from(groupMap.entries()).map(([group, items]) => ({ group, items }))
})

function isActive(path: string) {
  return route.path === path
}

function handleSelect(path: string, requiresAuth: boolean) {
  if (requiresAuth && !authStore.isLoggedIn) {
    authStore.openAuthModal('login')
    return
  }
  router.push(path)
  emit('navigate')
}
</script>
