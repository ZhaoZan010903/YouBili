export function formatCount(count?: number | null): string {
  const n = count ?? 0
  if (n >= 100_000_000) {
    return `${(n / 100_000_000).toFixed(1)}亿`
  }
  if (n >= 10_000) {
    return `${(n / 10_000).toFixed(1)}万`
  }
  return String(n)
}

export function formatDuration(duration?: string | null): string {
  if (!duration) return '00:00'
  const parts = duration.split(':')
  if (parts.length === 3 && parts[0] === '00') {
    return `${parts[1]}:${parts[2]}`
  }
  return duration
}

export function formatSeconds(totalSeconds: number): string {
  const sec = Math.max(0, Math.floor(totalSeconds))
  const h = Math.floor(sec / 3600)
  const m = Math.floor((sec % 3600) / 60)
  const s = sec % 60
  const pad = (v: number) => String(v).padStart(2, '0')
  return h > 0 ? `${pad(h)}:${pad(m)}:${pad(s)}` : `${pad(m)}:${pad(s)}`
}

export function formatRelativeTime(dateStr?: string | null): string {
  if (!dateStr) return ''
  const parsed = new Date(dateStr.replace(/-/g, '/'))
  if (Number.isNaN(parsed.getTime())) return dateStr
  const diffMs = Date.now() - parsed.getTime()
  const diffMin = Math.floor(diffMs / 60_000)
  if (diffMin < 1) return '刚刚'
  if (diffMin < 60) return `${diffMin} 分钟前`
  const diffHour = Math.floor(diffMin / 60)
  if (diffHour < 24) return `${diffHour} 小时前`
  const diffDay = Math.floor(diffHour / 24)
  if (diffDay < 30) return `${diffDay} 天前`
  return dateStr.slice(0, 10)
}

export const FALLBACK_COVER =
  'data:image/svg+xml;utf8,' +
  encodeURIComponent(
    '<svg xmlns="http://www.w3.org/2000/svg" width="640" height="360" viewBox="0 0 640 360"><defs><linearGradient id="g" x1="0" y1="0" x2="1" y2="1"><stop offset="0%" stop-color="#fb7299"/><stop offset="100%" stop-color="#00aeec"/></linearGradient></defs><rect width="640" height="360" fill="url(#g)"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="#ffffff" font-family="sans-serif" font-size="36" font-weight="bold">YouBili</text></svg>'
  )

export const FALLBACK_AVATAR =
  'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
