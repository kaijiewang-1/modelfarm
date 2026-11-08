import { router } from '@kit.ArkUI'
import { PageHeader, StatePanel, StatusTag } from '../components/CommonComponents'
import { Device, PageParams, deviceStatusText, deviceStatusCode, deviceTypeText } from '../models/Models'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct SiteDetailPage {
  @State devices: Device[] = []
  @State siteName: string = '养殖点详情'
  @State siteId: number = 0
  @State loading: boolean = true
  @State errorText: string = ''

  onPageShow(): void {
    const params = router.getParams() as PageParams
    this.siteId = params?.siteId || 0
    this.siteName = params?.siteName || '养殖点详情'
    this.load()
  }

  private async load(): Promise<void> {
    this.loading = true
    this.errorText = ''
    try {
      if (this.siteId <= 0) { throw new Error('养殖点信息无效，请返回重试') }
      this.devices = await FarmService.getInstance().getSiteDevices(this.siteId) || []
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '设备加载失败'
    } finally { this.loading = false }
  }

  build() {
    Column({ space: 12 }) {
      PageHeader({ title: this.siteName, subtitle: `${this.devices.length} 台设备` })
      Row({ space: 10 }) {
        Button('添加设备').fontColor(Color.White).backgroundColor('#176B43')
          .onClick(() => router.pushUrl({ url: 'pages/DeviceCreatePage', params: { siteId: this.siteId, siteName: this.siteName } }))
        Button('现场视频').fontColor('#176B43').backgroundColor('#E0EFE6')
          .onClick(() => router.pushUrl({ url: 'pages/LivePage', params: { siteId: this.siteId } }))
        Button('刷新').fontColor('#176B43').backgroundColor('#E0EFE6')
          .enabled(!this.loading).onClick(() => this.load())
      }
      if (this.loading || this.errorText.length > 0 || this.devices.length === 0) {
        StatePanel({ loading: this.loading, message: this.errorText || '暂无设备，点击上方添加设备' })
      } else {
        List({ space: 10 }) {
          ForEach(this.devices, (device: Device) => {
            ListItem() {
              Row({ space: 12 }) {
                Column({ space: 6 }) {
                  Text(device.name).fontSize(16).fontWeight(FontWeight.Bold).fontColor('#17211B')
                  Text(deviceTypeText(device.type)).fontSize(12).fontColor('#68736C')
                }.layoutWeight(1).alignItems(HorizontalAlign.Start)
                StatusTag({ text: deviceStatusText(device.status), positive: deviceStatusCode(device.status) === 1 })
                Text('›').fontSize(24).fontColor('#68736C')
              }
              .width('100%').padding(16).backgroundColor(Color.White).borderRadius(8)
              .onClick(() => router.pushUrl({ url: 'pages/DeviceDetailPage', params: { deviceId: device.id } }))
            }
          }, (device: Device) => String(device.id))
        }.width('100%').layoutWeight(1).padding({ left: 18, right: 18, bottom: 18 })
      }
    }.width('100%').height('100%').backgroundColor('#F4F7F2')
  }
}
