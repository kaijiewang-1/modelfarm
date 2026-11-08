import { http } from '@kit.NetworkKit'
import { router } from '@kit.ArkUI'
import { ApiEnvelope, QueryParams, RequestBody } from '../models/Models'
import { ApiConfig } from './ApiConfig'
import { AuthStore } from './AuthStore'

export class ApiError extends Error {
  readonly statusCode: number
  readonly apiCode: number

  constructor(message: string, statusCode: number = 0, apiCode: number = 0) {
    super(message)
    this.name = 'ApiError'
    this.statusCode = statusCode
    this.apiCode = apiCode
  }
}

export class ApiClient {
  private static instance: ApiClient = new ApiClient()

  static getInstance(): ApiClient {
    return ApiClient.instance
  }

  async get<T>(path: string, query: QueryParams = {}): Promise<T> {
    const queryText: string = this.buildQuery(query)
    const url: string = queryText.length > 0 ? `${path}?${queryText}` : path
    return this.request<T>(url, http.RequestMethod.GET)
  }

  async post<T>(path: string, body?: RequestBody): Promise<T> {
    return this.request<T>(path, http.RequestMethod.POST, body)
  }

  async put<T>(path: string, body?: RequestBody): Promise<T> {
    return this.request<T>(path, http.RequestMethod.PUT, body)
  }

  private buildQuery(query: QueryParams): string {
    const segments: string[] = []
    if (query.pageNum !== undefined) {
      segments.push('pageNum=' + encodeURIComponent(String(query.pageNum)))
    }
    if (query.pageSize !== undefined) {
      segments.push('pageSize=' + encodeURIComponent(String(query.pageSize)))
    }
    if (query.type !== undefined) {
      segments.push('type=' + encodeURIComponent(String(query.type)))
    }
    if (query.deviceId !== undefined) {
      segments.push('deviceId=' + encodeURIComponent(String(query.deviceId)))
    }
    if (query.farmId !== undefined) {
      segments.push('farmId=' + encodeURIComponent(String(query.farmId)))
    }
    if (query.farmSiteId !== undefined) {
      segments.push('farmSiteId=' + encodeURIComponent(String(query.farmSiteId)))
    }
    if (query.siteId !== undefined) {
      segments.push('siteId=' + encodeURIComponent(String(query.siteId)))
    }
    if (query.year !== undefined) {
      segments.push('year=' + encodeURIComponent(String(query.year)))
    }
    return segments.join('&')
  }
  private async request<T>(path: string, method: http.RequestMethod, body?: RequestBody): Promise<T> {
    const request: http.HttpRequest = http.createHttp()
    const token: string = AuthStore.getInstance().token
    const headers: Record<string, string> = {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    }
    if (token.length > 0) {
      headers['satoken'] = token
    }

    try {
      const response: http.HttpResponse = await request.request(`${ApiConfig.BASE_URL}${path}`, {
        method: method,
        header: headers,
        extraData: body ? JSON.stringify(body) : undefined,
        connectTimeout: ApiConfig.CONNECT_TIMEOUT_MS,
        readTimeout: ApiConfig.READ_TIMEOUT_MS,
        expectDataType: http.HttpDataType.STRING
      })
      if (response.responseCode === 401) {
        await this.expireSession(token)
        throw new ApiError('登录状态已失效，请重新登录', 401)
      }
      if (response.responseCode < 200 || response.responseCode >= 300) {
        throw new ApiError('服务器暂时无法处理请求', response.responseCode)
      }
      const raw: string = String(response.result)
      const envelope: ApiEnvelope<T> = JSON.parse(raw) as ApiEnvelope<T>
      if (!envelope || typeof envelope.code !== 'number' ||
        (envelope.message !== undefined && typeof envelope.message !== 'string')) {
        throw new ApiError('服务器返回的数据格式不正确', response.responseCode)
      }
      if (envelope.code !== 200) {
        if (envelope.code === 401 || envelope.code === 11011 || envelope.code === 11012) {
          await this.expireSession(token)
          throw new ApiError('登录状态已失效，请重新登录', 401, envelope.code)
        }
        throw new ApiError(envelope.message || '业务请求失败', response.responseCode, envelope.code)
      }
      if (envelope.data === undefined || envelope.data === null) {
        return undefined as T
      }
      return envelope.data
    } catch (error) {
      if (error instanceof ApiError) {
        throw error
      }
      throw new ApiError('网络连接失败，请检查网络后重试')
    } finally {
      request.destroy()
    }
  }

  private async expireSession(requestToken: string): Promise<void> {
    const auth: AuthStore = AuthStore.getInstance()
    // Ignore a delayed response from a session which has already been replaced.
    if (requestToken.length === 0 || auth.token !== requestToken) {
      return
    }
    try {
      await auth.clearSession()
    } finally {
      router.clear()
      router.replaceUrl({ url: 'pages/LoginPage' }).catch(() => {})
    }
  }
}
