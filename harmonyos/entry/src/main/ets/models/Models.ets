export interface ApiEnvelope<T> {
  code: number
  message: string
  data?: T
}

export interface LoginRequest {
  phone: string
  password: string
}

export interface RegisterRequest {
  username: string
  phone: string
  password: string
}

export interface CreateEnterpriseRequest {
  name: string
  address: string
  contactPerson: string
  contactPhone: string
}

export interface JoinEnterpriseRequest {
  invitedCode: string
}

export interface DevicePageQuery {
  pageNum: number
  pageSize: number
  type?: number
}

export interface DeviceDataQuery {
  deviceId: number
}

export interface SaToken {
  tokenName: string
  tokenValue: string
  isLogin: boolean
  loginId: number
  loginType: string
  tokenTimeout: number
}

export interface LoginResponse {
  userId: number
  enterpriseId: number
  satoken: SaToken
}

export interface EnterpriseStats {
  onlineDeviceCount: number
  pendingOrderCount: number
  userCount: number
  deviceCount: number
  farmCount: number
  faultDeviceCount: number
}

export interface Farm {
  id: number
  enterpriseId: number
  supervisorId: number
  name: string
  address: string
  createdAt: string
  updatedAt: string
}

export interface CameraDevice {
  id: number
  enterpriseId: number
  name: string
  pushName?: string
  siteId: number
  mac: string
  type: number
  status: number | string
  url?: string
  streamId?: string
  createdAt?: string
  updatedAt?: string
}
export interface Device {
  id: number
  enterpriseId: number
  name: string
  pushName?: string
  siteId: number
  siteName?: string
  mac: string
  type: number
  status: number
  url?: string
  createdAt: string
  updatedAt: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

export interface DeviceData {
  id: string
  deviceId: number
  data: Record<string, string | number>
  createdAt: string
  updatedAt: string
}

export interface WorkOrder {
  id: number
  enterpriseId: number
  title: string
  description: string
  status: string
  creatorId: number
  solvedId?: number
  acceptedId?: number
  isAccepted: number
  compeletedAt?: string
  createdAt: string
  updatedAt: string
}

export interface OrderWithMedias {
  order: WorkOrder
  mediaUrls?: Record<string, string[]>
}

export interface FarmSite {
  id: number
  enterpriseId: number
  farmId: number
  name: string
  sum?: number
  lng?: number
  lat?: number
  properties?: Record<string, string | number | null>
}
export interface Enterprise {
  id: number
  name: string
  address: string
  contactPhone: string
  contactPerson: string
  status: string
  invitedCode: string
  createdAt: string
  updatedAt: string
}

export interface User {
  id: number
  enterpriseId: number
  username: string
  phone: string
  status: string
  createdAt: string
  updatedAt: string
}

export interface AdminMember {
  user: User
  role: string
}

export interface LiveTicketRequest {
  deviceId: number
}

export interface LiveTicket {
  deviceId: number
  streamId: string
  wsUrl: string
  ticket: string
  expiresAt: string
  algoFlvUrl?: string
  frameMetadataEnabled?: boolean
}

export interface QueryParams {
  pageNum?: number
  pageSize?: number
  type?: number
  deviceId?: number
  farmId?: number
  farmSiteId?: number
}

export type RequestBody = LoginRequest | RegisterRequest | CreateEnterpriseRequest | JoinEnterpriseRequest | LiveTicketRequest

export function deviceTypeText(type: number): string {
  if (type === 1) {
    return '摄像头'
  }
  if (type === 2) {
    return '传感器'
  }
  if (type === 3) {
    return '控制器'
  }
  return '其他设备'
}

export function deviceStatusCode(status: number | string): number {
  return typeof status === 'string' ? Number(status) : status
}

export function deviceStatusText(status: number | string): string {
  const normalized: number = deviceStatusCode(status)
  if (normalized === 1) {
    return '在线'
  }
  if (normalized === 2) {
    return '离线'
  }
  if (normalized === 3) {
    return '故障'
  }
  return '未知'
}

export function orderStatusText(status: string): string {
  if (status === '2') {
    return '已完成'
  }
  if (status === '3') {
    return '紧急'
  }
  if (status === '4') {
    return '已认领'
  }
  return '待处理'
}
