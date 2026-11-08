import { common } from '@kit.AbilityKit'
import { preferences } from '@kit.ArkData'
import { LoginResponse } from '../models/Models'

const STORE_NAME: string = 'SmartFarmAuth'
const KEY_TOKEN: string = 'token'
const KEY_USER_ID: string = 'userId'
const KEY_ENTERPRISE_ID: string = 'enterpriseId'

export class AuthStore {
  private static instance: AuthStore = new AuthStore()
  private store?: preferences.Preferences
  private tokenValue: string = ''
  private userIdValue: number = 0
  private enterpriseIdValue: number = 0

  static getInstance(): AuthStore {
    return AuthStore.instance
  }

  async initialize(context: common.Context): Promise<void> {
    this.store = await preferences.getPreferences(context, { name: STORE_NAME })
    this.tokenValue = String(await this.store.get(KEY_TOKEN, ''))
    this.userIdValue = Number(await this.store.get(KEY_USER_ID, 0))
    this.enterpriseIdValue = Number(await this.store.get(KEY_ENTERPRISE_ID, 0))
  }

  get token(): string {
    return this.tokenValue
  }

  get userId(): number {
    return this.userIdValue
  }

  get enterpriseId(): number {
    return this.enterpriseIdValue
  }

  isLoggedIn(): boolean {
    return this.tokenValue.length > 0
  }

  async saveSession(response: LoginResponse): Promise<void> {
    if (!this.store) {
      throw new Error('AuthStore is not initialized')
    }
    if (!response || !response.satoken || typeof response.satoken.tokenValue !== 'string' ||
      response.satoken.tokenValue.length === 0 || !(response.userId > 0)) {
      throw new Error('登录响应无效，请重试')
    }
    await this.store.put(KEY_TOKEN, response.satoken.tokenValue)
    await this.store.put(KEY_USER_ID, response.userId)
    await this.store.put(KEY_ENTERPRISE_ID, response.enterpriseId || 0)
    await this.store.flush()
    this.tokenValue = response.satoken.tokenValue
    this.userIdValue = response.userId
    this.enterpriseIdValue = response.enterpriseId || 0
  }

  async updateEnterpriseId(enterpriseId: number): Promise<void> {
    if (!this.store) {
      throw new Error('AuthStore is not initialized')
    }
    this.enterpriseIdValue = enterpriseId
    await this.store.put(KEY_ENTERPRISE_ID, enterpriseId)
    await this.store.flush()
  }

  async clearSession(): Promise<void> {
    this.tokenValue = ''
    this.userIdValue = 0
    this.enterpriseIdValue = 0
    if (this.store) {
      await this.store.clear()
      await this.store.flush()
    }
  }
}
