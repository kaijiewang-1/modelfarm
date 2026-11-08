import { url } from '@kit.ArkTS'

// Prefer the exact media endpoint from the backend. RTMP -> FLV is only a
// compatibility fallback for the SRS deployment described in the live API doc.
export function resolvePlaybackUrl(pushName: string = '', mediaUrl: string = ''): string {
  try {
    if (mediaUrl.trim().length > 0) {
      const direct = url.URL.parseURL(mediaUrl.trim())
      if ((direct.protocol === 'http:' || direct.protocol === 'https:') && !direct.username && !direct.password) {
        return mediaUrl.trim()
      }
    }
    const source = url.URL.parseURL(pushName.trim())
    if ((source.protocol !== 'rtmp:' && source.protocol !== 'rtmps:') || source.username || source.password ||
      source.pathname.split('/').filter((part: string) => part.length > 0).length < 2) { return '' }
    const protocol: string = source.protocol === 'rtmps:' ? 'https:' : 'http:'
    const port: string = source.protocol === 'rtmps:' ? '' : ':8080'
    return `${protocol}//${source.hostname}${port}${source.pathname}.flv${source.search}`
  } catch (error) { return '' }
}
