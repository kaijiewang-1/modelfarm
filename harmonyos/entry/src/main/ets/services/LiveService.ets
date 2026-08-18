import { ApiClient } from './ApiClient'
import { CameraDevice, LiveTicket, LiveTicketRequest, QueryParams } from '../models/Models'

/** API-only live service. Media playback is delegated to the device media capability. */
export class LiveService {
  private static instance: LiveService = new LiveService()
  private client: ApiClient = ApiClient.getInstance()

  static getInstance(): LiveService {
    return LiveService.instance
  }

  getCameras(farmSiteId: number): Promise<CameraDevice[]> {
    const query: QueryParams = { farmSiteId: farmSiteId }
    return this.client.get<CameraDevice[]>('/farmSite/camera', query)
  }

  requestTicket(deviceId: number): Promise<LiveTicket> {
    const body: LiveTicketRequest = { deviceId: deviceId }
    return this.client.post<LiveTicket>('/live/ticket', body)
  }
}
