import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

declare module 'vue-router' {
  interface RouteMeta {
    title?: string
    requiresAuth?: boolean
    group?: string
    icon?: string
    showInSidebar?: boolean
  }
}

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/index',
    children: [
      {
        path: 'index',
        name: 'Home',
        component: () => import('@/views/HomeView.vue'),
        meta: { title: '首页推荐', group: '发现', icon: 'HomeFilled', showInSidebar: true }
      },
      {
        path: 'hot',
        name: 'Hot',
        component: () => import('@/views/HotView.vue'),
        meta: { title: '热门排行', group: '发现', icon: 'TrendCharts', showInSidebar: true }
      },
      {
        path: 'classification',
        name: 'Classification',
        component: () => import('@/views/ClassificationView.vue'),
        meta: { title: '分区频道', group: '发现', icon: 'Grid', showInSidebar: true }
      },
      {
        path: 'search',
        name: 'Search',
        component: () => import('@/views/SearchView.vue'),
        meta: { title: '搜索结果', showInSidebar: false }
      },
      {
        path: 'video/:videoId',
        name: 'VideoDetail',
        component: () => import('@/views/VideoDetailView.vue'),
        meta: { title: '视频播放', showInSidebar: false }
      },
      {
        path: 'dynamic',
        name: 'Dynamic',
        component: () => import('@/views/DynamicView.vue'),
        meta: { title: '关注动态', group: '我的', icon: 'Promotion', showInSidebar: true, requiresAuth: true }
      },
      {
        path: 'collection',
        name: 'Collection',
        component: () => import('@/views/CollectionView.vue'),
        meta: { title: '我的收藏', group: '我的', icon: 'Star', showInSidebar: true, requiresAuth: true }
      },
      {
        path: 'history',
        name: 'History',
        component: () => import('@/views/HistoryView.vue'),
        meta: { title: '观看历史', group: '我的', icon: 'Clock', showInSidebar: true, requiresAuth: true }
      },
      {
        path: 'follow',
        name: 'Follow',
        component: () => import('@/views/FollowFansView.vue'),
        meta: { title: '关注列表', group: '我的', icon: 'UserFilled', showInSidebar: true, requiresAuth: true }
      },
      {
        path: 'fans',
        name: 'Fans',
        component: () => import('@/views/FollowFansView.vue'),
        meta: { title: '我的粉丝', group: '我的', icon: 'User', showInSidebar: true, requiresAuth: true }
      },
      {
        path: 'notify',
        name: 'Notify',
        component: () => import('@/views/NotifyView.vue'),
        meta: { title: '消息通知', group: '我的', icon: 'Bell', showInSidebar: true, requiresAuth: true }
      },
      {
        path: 'space/:userId',
        name: 'UserSpace',
        component: () => import('@/views/UserSpaceView.vue'),
        meta: { title: '个人空间', showInSidebar: false }
      },
      {
        path: 'contribute',
        name: 'Contribute',
        component: () => import('@/views/ContributeView.vue'),
        meta: { title: '发布投稿', group: '创作中心', icon: 'VideoCamera', showInSidebar: true, requiresAuth: true }
      },
      {
        path: 'contribute-manage',
        name: 'ContributeManage',
        component: () => import('@/views/ContributeManageView.vue'),
        meta: { title: '稿件管理', group: '创作中心', icon: 'FolderOpened', showInSidebar: true, requiresAuth: true }
      },
      {
        path: 'vip',
        name: 'Vip',
        component: () => import('@/views/VipView.vue'),
        meta: { title: '大会员中心', group: '服务', icon: 'Trophy', showInSidebar: true }
      },
      {
        path: 'orders',
        name: 'Orders',
        component: () => import('@/views/OrdersView.vue'),
        meta: { title: '我的订单', group: '服务', icon: 'Tickets', showInSidebar: true, requiresAuth: true }
      },
      {
        path: 'report',
        name: 'Report',
        component: () => import('@/views/ReportView.vue'),
        meta: { title: '违规举报', showInSidebar: false, requiresAuth: true }
      }
    ]
  },
  // 兼容旧版路由地址
  { path: '/showHot', redirect: '/hot' },
  { path: '/pay', redirect: '/vip' },
  {
    path: '/videoIndex',
    redirect: (to) => ({ path: `/video/${to.query.videoID || 1}` })
  },
  { path: '/:pathMatch(.*)*', redirect: '/index' }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach(async (to) => {
  const authStore = useAuthStore()
  if (!authStore.initialized) {
    await authStore.initAuth()
  }

  document.title = to.meta.title ? `${to.meta.title} - YouBili` : 'YouBili'

  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    authStore.openAuthModal('login')
    return false
  }
  return true
})

export default router
