import {
  AdminMember,
  CreateDeviceRequest,
  CreateOrderRequest,
  AcceptOrderRequest,
  CreateEnterpriseRequest,
  Device,


  DeviceData,
  Enterprise,
  EnterpriseStats,
  Farm,
  FarmSite,
  LoginRequest,
  JoinEnterpriseRequest,
  LoginResponse,
  OrderWithMedias,
  PageResult,
  QueryParams,
  RegisterRequest,
  User
} from '../models/Models'
import { ApiClient } from './ApiClient'

export class FarmService {
  private static instance: FarmService = new FarmService()
  private client: ApiClient = ApiClient.getInstance()

  static getInstance(): FarmService {
    return FarmService.instance
  }

  login(request: LoginRequest): Promise<LoginResponse> {
    return this.client.post<LoginResponse>('/user/login', request)
  }

  register(request: RegisterRequest): Promise<LoginResponse> {
    return this.client.post<LoginResponse>('/user/register', request)
  }

  createEnterprise(name: string, address: string, contactPerson: string, contactPhone: string): Promise<number> {
    const request: CreateEnterpriseRequest = {
      name: name,
      address: address,
      contactPerson: contactPerson,
      contactPhone: contactPhone
    }
    return this.client.post<number>('/enterprise', request)
  }

  joinEnterprise(invitedCode: string): Promise<void> {
    const request: JoinEnterpriseRequest = { invitedCode: invitedCode }
    return this.client.post<void>('/user/join', request)
  }

  logout(): Promise<void> {
    return this.client.post<void>('/user/logout')
  }

  getStats(): Promise<EnterpriseStats> {
    return this.client.get<EnterpriseStats>('/enterprise/count')
  }

  getFarms(): Promise<Farm[]> {
    return this.client.get<Farm[]>('/enterprise/farms')
  }

  getFarmSites(farmId: number): Promise<FarmSite[]> {
    const query: QueryParams = { farmId: farmId }
    return this.client.get<FarmSite[]>('/farm/sites', query)
  }
  getDevices(type?: number): Promise<PageResult<Device>> {
    const query: QueryParams = { pageNum: 1, pageSize: 100 }
    if (type !== undefined) {
      query.type = type
    }
    return this.client.get<PageResult<Device>>('/device/page', query)
  }

  getSiteDevices(siteId: number): Promise<Device[]> {
    const query: QueryParams = { farmSiteId: siteId }
    return this.client.get<Device[]>('/farmSite/devices', query)
  }

  getDevice(deviceId: number): Promise<Device> {
    const query: QueryParams = { deviceId: deviceId }
    return this.client.get<Device>('/device', query)
  }

  createDevice(request: CreateDeviceRequest): Promise<number> {
    return this.client.post<number>('/device', request)
  }

  createOrder(request: CreateOrderRequest): Promise<number> {
    return this.client.post<number>('/order', request)
  }

  acceptOrder(orderId: number): Promise<void> {
    const request: AcceptOrderRequest = { orderId: orderId }
    return this.client.post<void>('/order/accept', request)
  }

  getLatestDeviceData(deviceId: number): Promise<DeviceData> {
    const query: QueryParams = { deviceId: deviceId }
    return this.client.get<DeviceData>('/device-data/latest', query)
  }

  getOrders(): Promise<OrderWithMedias[]> {
    return this.client.get<OrderWithMedias[]>('/order/list')
  }

  completeOrder(orderId: number): Promise<void> {
    return this.client.post<void>(`/order/complete?id=${encodeURIComponent(String(orderId))}`)
  }

  getEnterprise(): Promise<Enterprise> {
    return this.client.get<Enterprise>('/enterprise')
  }

  getCurrentUser(): Promise<User> {
    return this.client.get<User>('/user')
  }

  checkAdmin(): Promise<void> {
    return this.client.get<void>('/admin/check')
  }

  getAdminUsers(): Promise<AdminMember[]> {
    return this.client.get<AdminMember[]>('/admin/users')
  }
}
