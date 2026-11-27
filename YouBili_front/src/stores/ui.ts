import { defineStore } from 'pinia'
import { ref } from 'vue'

const THEME_KEY = 'youbili_theme'
const SEARCH_HISTORY_KEY = 'youbili_search_history'

function loadSearchHistory(): string[] {
  try {
    const raw = localStorage.getItem(SEARCH_HISTORY_KEY)
    if (!raw) return []
    const parsed = JSON.parse(raw)
    return Array.isArray(parsed) ? parsed.filter((v): v is string => typeof v === 'string') : []
  } catch {
    return []
  }
}

export const useUiStore = defineStore('ui', () => {
  const sidebarCollapsed = ref(false)
  const mobileSidebarOpen = ref(false)
  const isDark = ref(localStorage.getItem(THEME_KEY) === 'dark')
  const searchHistory = ref<string[]>(loadSearchHistory())

  function applyTheme(dark: boolean) {
    isDark.value = dark
    document.documentElement.classList.toggle('dark', dark)
    localStorage.setItem(THEME_KEY, dark ? 'dark' : 'light')
  }

  function initTheme() {
    applyTheme(isDark.value)
  }

  function toggleTheme() {
    applyTheme(!isDark.value)
  }

  function toggleSidebar() {
    if (window.innerWidth < 1024) {
      mobileSidebarOpen.value = !mobileSidebarOpen.value
    } else {
      sidebarCollapsed.value = !sidebarCollapsed.value
    }
  }

  function addSearchHistory(keyword: string) {
    const cleaned = keyword.trim()
    if (!cleaned) return
    const next = [cleaned, ...searchHistory.value.filter((k) => k !== cleaned)].slice(0, 12)
    searchHistory.value = next
    localStorage.setItem(SEARCH_HISTORY_KEY, JSON.stringify(next))
  }

  function removeSearchHistory(keyword: string) {
    searchHistory.value = searchHistory.value.filter((k) => k !== keyword)
    localStorage.setItem(SEARCH_HISTORY_KEY, JSON.stringify(searchHistory.value))
  }

  function clearSearchHistory() {
    searchHistory.value = []
    localStorage.removeItem(SEARCH_HISTORY_KEY)
  }

  return {
    sidebarCollapsed,
    mobileSidebarOpen,
    isDark,
    searchHistory,
    initTheme,
    toggleTheme,
    toggleSidebar,
    addSearchHistory,
    removeSearchHistory,
    clearSearchHistory
  }
})
