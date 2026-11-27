import { request } from '@/api/http'
import type { AxiosProgressEvent } from 'axios'
import type {
  BarrageItem,
  CategoryItem,
  CommentItem,
  FavoriteItem,
  FollowUser,
  HistoryItem,
  LoginResponse,
  NoticeItem,
  OrderItem,
  PageResult,
  ReactionState,
  TagItem,
  UploadResult,
  UserProfile,
  UserSpace,
  VideoCard,
  VideoDetail,
  VipPlan
} from '@/types/api'

export const authApi = {
  login: (data: { username: string; password: string }) =>
    request<LoginResponse>({ method: 'POST', url: '/api/v1/auth/login', data }),

  sendEmailCode: (email: string) =>
    request<void>({ method: 'POST', url: '/api/v1/auth/email-code', data: { email } }),

  register: (data: {
    username: string
    password: string
    nickname: string
    email: string
    code: string
  }) => request<void>({ method: 'POST', url: '/api/v1/auth/register', data }),

  logout: () => request<void>({ method: 'POST', url: '/api/v1/auth/logout' }),

  me: () => request<UserProfile>({ method: 'GET', url: '/api/v1/auth/me' })
}

export const recommendApi = {
  feed: (size = 12) =>
    request<VideoCard[]>({ method: 'GET', url: '/api/v1/recommend/feed', params: { size } }),

  carousel: (size = 5) =>
    request<VideoCard[]>({ method: 'GET', url: '/api/v1/recommend/carousel', params: { size } }),

  related: (videoId: number, size = 8) =>
    request<VideoCard[]>({
      method: 'GET',
      url: '/api/v1/recommend/related',
      params: { videoId, size }
    }),

  hot: (size = 20) =>
    request<VideoCard[]>({ method: 'GET', url: '/api/v1/recommend/hot', params: { size } })
}

export const categoryApi = {
  listCategories: () =>
    request<CategoryItem[]>({ method: 'GET', url: '/api/v1/categories' }),

  listTags: () =>
    request<TagItem[]>({ method: 'GET', url: '/api/v1/tags' }),

  listCategoryVideos: (categoryId: number, params?: { page?: number; size?: number; sort?: string }) =>
    request<PageResult<VideoCard>>({
      method: 'GET',
      url: `/api/v1/categories/${categoryId}/videos`,
      params
    })
}

export const searchApi = {
  videos: (params: { keyword: string; page?: number; size?: number; sort?: string }) =>
    request<PageResult<VideoCard>>({ method: 'GET', url: '/api/v1/search/videos', params }),

  suggest: (keyword: string) =>
    request<string[]>({ method: 'GET', url: '/api/v1/search/suggest', params: { keyword } })
}

export const videoApi = {
  detail: (videoId: number) =>
    request<VideoDetail>({ method: 'GET', url: `/api/v1/videos/${videoId}` }),

  myVideos: (params?: { reviewStatus?: string; page?: number; size?: number }) =>
    request<PageResult<VideoCard>>({ method: 'GET', url: '/api/v1/videos/my', params }),

  uploadVideo: (file: File, onProgress?: (percent: number) => void) => {
    const formData = new FormData()
    formData.append('file', file)
    return request<UploadResult>({
      method: 'POST',
      url: '/api/v1/videos/upload/file',
      data: formData,
      timeout: 300000,
      headers: { 'Content-Type': 'multipart/form-data' },
      onUploadProgress: (e: AxiosProgressEvent) => {
        if (onProgress && e.total) {
          onProgress(Math.round((e.loaded * 100) / e.total))
        }
      }
    })
  },

  uploadCover: (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    return request<UploadResult>({
      method: 'POST',
      url: '/api/v1/videos/upload/cover',
      data: formData,
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  publish: (data: {
    title: string
    description: string
    videoSrcUrl: string
    videoImgUrl: string
    duration?: string
    categoryId: number
    tagIds: number[]
  }) => request<{ videoId: number }>({ method: 'POST', url: '/api/v1/videos/publish', data }),

  update: (
    videoId: number,
    data: {
      title: string
      description?: string
      videoImgUrl?: string
      categoryId?: number
      tagIds?: number[]
    }
  ) => request<void>({ method: 'PUT', url: `/api/v1/videos/${videoId}`, data }),

  remove: (videoId: number) =>
    request<void>({ method: 'DELETE', url: `/api/v1/videos/${videoId}` })
}

export const barrageApi = {
  list: (videoId: number) =>
    request<BarrageItem[]>({ method: 'GET', url: `/api/v1/videos/${videoId}/barrages` }),

  send: (videoId: number, data: { time: number; text: string; color: string; type: 'scroll' | 'top' | 'bottom' }) =>
    request<BarrageItem>({ method: 'POST', url: `/api/v1/videos/${videoId}/barrages`, data })
}

export const interactionApi = {
  like: (videoId: number) =>
    request<ReactionState>({ method: 'POST', url: `/api/v1/videos/${videoId}/like` }),

  cancelLike: (videoId: number) =>
    request<ReactionState>({ method: 'DELETE', url: `/api/v1/videos/${videoId}/like` }),

  dislike: (videoId: number) =>
    request<ReactionState>({ method: 'POST', url: `/api/v1/videos/${videoId}/dislike` }),

  cancelDislike: (videoId: number) =>
    request<ReactionState>({ method: 'DELETE', url: `/api/v1/videos/${videoId}/dislike` }),

  listFavorites: (keyword?: string) =>
    request<FavoriteItem[]>({ method: 'GET', url: '/api/v1/favorites', params: { keyword } }),

  addFavorite: (videoId: number) =>
    request<ReactionState>({ method: 'POST', url: `/api/v1/favorites/${videoId}` }),

  removeFavoriteByVideo: (videoId: number) =>
    request<ReactionState>({ method: 'DELETE', url: `/api/v1/favorites/${videoId}` }),

  removeFavoriteById: (favoriteId: number) =>
    request<void>({ method: 'DELETE', url: `/api/v1/favorites/item/${favoriteId}` }),

  listHistory: (keyword?: string) =>
    request<HistoryItem[]>({ method: 'GET', url: '/api/v1/history', params: { keyword } }),

  recordWatch: (videoId: number, watchDurationSeconds = 0) =>
    request<void>({
      method: 'POST',
      url: '/api/v1/history',
      data: { videoId, watchDurationSeconds }
    }),

  deleteHistory: (historyId: number) =>
    request<void>({ method: 'DELETE', url: `/api/v1/history/${historyId}` }),

  clearHistory: () =>
    request<void>({ method: 'DELETE', url: '/api/v1/history' })
}

export const socialApi = {
  listComments: (videoId: number, params?: { page?: number; size?: number; sort?: string }) =>
    request<PageResult<CommentItem>>({
      method: 'GET',
      url: `/api/v1/videos/${videoId}/comments`,
      params
    }),

  addComment: (videoId: number, commentText: string) =>
    request<{ commentId: number }>({
      method: 'POST',
      url: `/api/v1/videos/${videoId}/comments`,
      data: { commentText }
    }),

  addReply: (commentId: number, replyText: string, parentReplyId?: number | null) =>
    request<{ replyId: number }>({
      method: 'POST',
      url: `/api/v1/comments/${commentId}/replies`,
      data: { replyText, parentReplyId: parentReplyId ?? null }
    }),

  deleteComment: (commentId: number) =>
    request<void>({ method: 'DELETE', url: `/api/v1/comments/${commentId}` }),

  listDynamics: (params?: { page?: number; size?: number }) =>
    request<PageResult<VideoCard>>({ method: 'GET', url: '/api/v1/dynamics', params }),

  listNotices: (type: 'personal' | 'system' = 'personal') =>
    request<NoticeItem[]>({ method: 'GET', url: '/api/v1/notices', params: { type } }),

  markNoticeRead: (noticeId: number) =>
    request<void>({ method: 'PUT', url: `/api/v1/notices/${noticeId}/read` }),

  deleteNotice: (noticeId: number) =>
    request<void>({ method: 'DELETE', url: `/api/v1/notices/${noticeId}` }),

  submitReport: (data: {
    targetType: 'VIDEO' | 'COMMENT' | 'USER'
    targetId: number
    category: string
    reasons: string[]
    description: string
  }) => request<{ reportId: number }>({ method: 'POST', url: '/api/v1/reports', data })
}

export const userApi = {
  space: (userId: number) =>
    request<UserSpace>({ method: 'GET', url: `/api/v1/users/${userId}/space` }),

  updateProfile: (data: { nickname?: string; birthday?: string | null; avatarUrl?: string }) =>
    request<UserProfile>({ method: 'PUT', url: '/api/v1/users/me', data }),

  follows: () =>
    request<FollowUser[]>({ method: 'GET', url: '/api/v1/users/me/follows' }),

  fans: () =>
    request<FollowUser[]>({ method: 'GET', url: '/api/v1/users/me/fans' }),

  follow: (targetUserId: number) =>
    request<void>({ method: 'POST', url: `/api/v1/users/${targetUserId}/follow` }),

  unfollow: (targetUserId: number) =>
    request<void>({ method: 'DELETE', url: `/api/v1/users/${targetUserId}/follow` }),

  removeFan: (followerId: number) =>
    request<void>({ method: 'DELETE', url: `/api/v1/users/me/fans/${followerId}` })
}

export const orderApi = {
  plans: () => request<VipPlan[]>({ method: 'GET', url: '/api/v1/orders/plans' }),

  myOrders: () => request<OrderItem[]>({ method: 'GET', url: '/api/v1/orders' }),

  createOrder: (planCode: string, payment = '支付宝') =>
    request<OrderItem>({ method: 'POST', url: '/api/v1/orders', data: { planCode, payment } }),

  deleteOrder: (orderId: number) =>
    request<void>({ method: 'DELETE', url: `/api/v1/orders/${orderId}` }),

  payOrder: (orderNumber: string) =>
    request<{ formHtml: string }>({ method: 'POST', url: `/api/v1/alipay/pay/${orderNumber}` })
}
