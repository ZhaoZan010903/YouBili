import axios, { type AxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiResponse } from '@/types/api'

const TOKEN_STORAGE_KEY = 'youbili_token'

export function getStoredToken(): string | null {
  return localStorage.getItem(TOKEN_STORAGE_KEY)
}

export function setStoredToken(token: string | null): void {
  if (token) {
    localStorage.setItem(TOKEN_STORAGE_KEY, token)
  } else {
    localStorage.removeItem(TOKEN_STORAGE_KEY)
  }
}

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

axiosInstance.interceptors.request.use(
  (config) => {
    const token = getStoredToken()
    if (token) {
      config.headers = config.headers ?? {}
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

axiosInstance.interceptors.response.use(
  (response) => {
    const payload = response.data as ApiResponse<unknown>
    if (payload && typeof payload.success === 'boolean' && !payload.success) {
      if (payload.status === 60000 || payload.status === 60100 || payload.status === 40100) {
        setStoredToken(null)
        window.dispatchEvent(new CustomEvent('youbili:auth-expired'))
      }
      ElMessage.error(payload.message || '操作失败')
      return Promise.reject(new Error(payload.message || '请求失败'))
    }
    return response
  },
  (error) => {
    const status = error.response?.status
    const message: string | undefined = error.response?.data?.message
    if (status === 401) {
      setStoredToken(null)
      window.dispatchEvent(new CustomEvent('youbili:auth-expired'))
      ElMessage.warning(message || '登录已过期或未登录，请重新登录')
    } else if (status === 403) {
      ElMessage.error(message || '没有权限执行该操作')
    } else if (status === 404) {
      ElMessage.error(message || '请求的资源不存在')
    } else if (status && status >= 500) {
      ElMessage.error(message || '服务器繁忙，请稍后再试')
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请检查网络连接')
    } else {
      ElMessage.error(message || '网络连接异常')
    }
    return Promise.reject(error)
  }
)

export async function request<T>(config: AxiosRequestConfig): Promise<T> {
  const res = await axiosInstance.request<ApiResponse<T>>(config)
  return res.data.data
}

export default axiosInstance
