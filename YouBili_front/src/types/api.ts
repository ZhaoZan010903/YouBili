export interface ApiResponse<T = unknown> {
  status: number
  success: boolean
  message: string
  data: T
}

export interface PageResult<T> {
  page: number
  size: number
  total: number
  pages: number
  records: T[]
}

export interface UserProfile {
  userId: number
  username: string
  nickname: string
  email?: string | null
  avatarUrl: string
  birthday?: string | null
  accountLevel: number
  fans: number
  follows: number
  videoCount: number
  totalLikes: number
  membershipStatus: 'Active' | 'Inactive' | string
  registrationDate?: string
}

export interface LoginResponse {
  token: string
  tokenType: string
  user: UserProfile
}

export interface CategoryItem {
  categoryId: number
  categoryName: string
  description?: string
}

export interface TagItem {
  tagId: number
  tagName: string
  description?: string
}

export interface VideoCard {
  videoId: number
  userId: number
  title: string
  description?: string
  duration?: string
  uploadDate: string
  videoSrcUrl: string
  videoImgUrl: string
  authorNickname: string
  authorAvatarUrl: string
  categoryId?: number
  categoryName?: string
  viewCount: number
  likeCount: number
  favoriteCount: number
  barrageCount: number
  commentCount: number
  reviewStatus?: 'Pending' | 'Approved' | 'Rejected' | string
  heatScore?: number
}

export interface QualityOption {
  label: string
  url: string
}

export interface VideoDetail {
  videoId: number
  userId: number
  title: string
  description: string
  duration?: string
  uploadDate: string
  videoSrcUrl: string
  videoImgUrl: string
  authorNickname: string
  authorAvatarUrl: string
  authorFans: number
  authorFollows: number
  categoryId?: number
  categoryName?: string
  viewCount: number
  likeCount: number
  dislikeCount: number
  favoriteCount: number
  barrageCount: number
  commentCount: number
  tags: TagItem[]
  qualities: QualityOption[]
  liked: boolean
  disliked: boolean
  favorited: boolean
  followingAuthor: boolean
}

export interface UploadResult {
  url: string
  fileName: string
  size: number
}

export interface BarrageItem {
  time: number
  text: string
  color: string
  type: 'scroll' | 'top' | 'bottom'
  userId?: number
}

export interface ReactionState {
  liked: boolean
  disliked: boolean
  favorited: boolean
  likeCount: number
  dislikeCount: number
  favoriteCount: number
}

export interface FavoriteItem {
  favoriteId: number
  videoId: number
  title: string
  description?: string
  videoImgUrl: string
  duration?: string
  authorId: number
  authorNickname: string
  authorAvatarUrl: string
  viewCount: number
  likeCount: number
  favoriteDate: string
}

export interface HistoryItem {
  historyId: number
  videoId: number
  title: string
  videoImgUrl: string
  duration?: string
  authorId: number
  authorNickname: string
  authorAvatarUrl: string
  viewCount: number
  watchDuration?: string
  watchDate: string
}

export interface CommentReplyItem {
  replyId: number
  commentId: number
  parentReplyId?: number | null
  userId: number
  nickname: string
  avatarUrl: string
  replyToNickname?: string | null
  replyText: string
  likeCount: number
  dislikeCount: number
  gmtCreate: string
}

export interface CommentItem {
  commentId: number
  userId: number
  nickname: string
  avatarUrl: string
  commentText: string
  likeCount: number
  dislikeCount: number
  gmtCreate: string
  replyCount: number
  replies: CommentReplyItem[]
}

export interface FollowUser {
  followId: number
  userId: number
  nickname: string
  avatarUrl: string
  accountLevel: number
  fansCount: number
  followCount: number
  mutualFollow: boolean
  followDate: string
}

export interface UserSpace {
  profile: UserProfile
  isSelf: boolean
  isFollowing: boolean
  videos: VideoCard[]
  favorites: VideoCard[]
  histories: VideoCard[]
}

export interface NoticeItem {
  noticeId: number
  userId?: number | null
  systemName?: string
  typeEnable: number
  isRead: number
  noticeText: string
  noticeTime: string
}

export interface VipPlan {
  code: string
  name: string
  price: number
  durationDays: number
  privileges: string[]
}

export interface OrderItem {
  orderId: number
  tradeName: string
  orderNumber: string
  zfbNumber?: string | null
  price: number
  payment: string
  status: string
  paid: boolean
  payTime?: string | null
  gmtCreate?: string
}
