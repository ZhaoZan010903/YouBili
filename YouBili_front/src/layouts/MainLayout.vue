<template>
  <div class="min-h-screen flex flex-col" style="background-color: var(--yb-bg); color: var(--yb-text);">
    <AppHeader />

    <div class="flex flex-1 pt-16">
      <!-- Desktop Sidebar -->
      <aside
        class="hidden lg:block fixed top-16 left-0 bottom-0 z-30 transition-all duration-300 border-r"
        :style="{
          width: uiStore.sidebarCollapsed ? '72px' : '220px',
          backgroundColor: 'var(--yb-surface)',
          borderColor: 'var(--yb-border)'
        }"
      >
        <AppSidebar :collapsed="uiStore.sidebarCollapsed" />
      </aside>

      <!-- Mobile Drawer Sidebar -->
      <el-drawer
        v-model="uiStore.mobileSidebarOpen"
        direction="ltr"
        size="240px"
        :with-header="false"
        class="lg:hidden"
      >
        <div class="py-3">
          <div class="flex items-center gap-2.5 px-4 pb-4 mb-2 border-b" style="border-color: var(--yb-border);">
            <img src="/mLogo.png" alt="YouBili" class="w-8 h-8 rounded-lg object-contain" />
            <span class="text-lg font-bold tracking-tight" style="color: var(--yb-brand);">YouBili</span>
          </div>
          <AppSidebar :collapsed="false" @navigate="uiStore.mobileSidebarOpen = false" />
        </div>
      </el-drawer>

      <!-- Main Workspace -->
      <main
        class="flex-1 min-w-0 transition-all duration-300"
        :class="uiStore.sidebarCollapsed ? 'lg:pl-[72px]' : 'lg:pl-[220px]'"
      >
        <div class="max-w-[1600px] mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <router-view />
        </div>
      </main>
    </div>

    <el-backtop :right="28" :bottom="32" />
  </div>
</template>

<script setup lang="ts">
import AppHeader from '@/components/AppHeader.vue'
import AppSidebar from '@/components/AppSidebar.vue'
import { useUiStore } from '@/stores/ui'

const uiStore = useUiStore()
</script>
