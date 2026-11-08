import { router } from '@kit.ArkUI'
import { PageHeader, StatePanel, StatusTag } from '../components/CommonComponents'
import { Device, PageParams, deviceStatusText, deviceStatusCode, deviceTypeText } from '../models/Models'
import { FarmService } from '../services/FarmService'
import { EnvironmentReading, emptyEnvironment, parseEnvironment } from '../services/EnvironmentParser'

@Entry
@Component
struct DeviceDetailPage {
  @State device: Device | undefined = undefined
  @State reading: EnvironmentReading = emptyEnvironment()
  @State loading: boolean = true
  @State errorText: string = ''
  private deviceId: number = 0

  aboutToAppear(): void {
    const params = router.getParams() as PageParams
    this.deviceId = params?.deviceId || 0
    this.load()
  }

  private async load(): Promise<void> {
    this.loading = true
    this.errorText = ''
    this.reading = emptyEnvironment()
    try {
      if (this.deviceId <= 0) { throw new Error('设备信息无效，请返回重试') }
      const service: FarmService = FarmService.getInstance()
      this.device = await service.getDevice(this.deviceId)
      if (this.device && this.device.type === 2) {
        this.reading = parseEnvironment(await service.getLatestDeviceData(this.deviceId))
      }
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '设备加载失败'
    } finally { this.loading = false }
  }

  @Builder
  private field(label: string, value: string) {
    Column({ space: 5 }) {
      Text(label).fontSize(12).fontColor('#68736C')
      Text(value || '--').fontSize(16).fontColor('#17211B')
    }.width('100%').alignItems(HorizontalAlign.Start).padding(14).backgroundColor(Color.White).borderRadius(7)
  }

  build() {
    Column() {
      PageHeader({ title: this.device?.name || '设备详情', subtitle: '设备信息与最新读数' })
      Button('刷新').height(36).fontColor('#176B43').backgroundColor('#E0EFE6')
        .enabled(!this.loading).onClick(() => this.load())
      if (this.loading) {
        StatePanel({ loading: true })
      } else {
        Scroll() {
          Column({ space: 10 }) {
            if (this.errorText.length > 0) {
              Text(this.errorText).width('100%').fontColor('#B33B2E').fontSize(13)
            }
            if (this.device) {
              StatusTag({ text: deviceStatusText(this.device.status), positive: deviceStatusCode(this.device.status) === 1 })
              this.field('设备类型', deviceTypeText(this.device.type))
              this.field('所属养殖点', this.device.siteName || String(this.device.siteId))
              this.field('MAC / 设备标识', this.device.mac)
              this.field('更新时间', this.device.updatedAt)
              if (this.device.type === 2) {
                this.field('温度', this.reading.temperature)
                this.field('湿度', this.reading.humidity)
                this.field('二氧化碳 CO₂', this.reading.co2)
                this.field('氨气 NH₃', this.reading.nh3)
                this.field('PM2.5', this.reading.pm25)
                this.field('PM10', this.reading.pm10)
                this.field('采集时间', this.reading.updatedAt)
              }
              if (this.device.type === 1) {
                Button('查看现场视频').width('100%').backgroundColor('#176B43')
                  .onClick(() => router.pushUrl({ url: 'pages/LivePage', params: { siteId: this.device?.siteId, deviceId: this.deviceId } }))
              }
            }
          }.width('100%').padding(18)
        }.layoutWeight(1)
      }
    }.width('100%').height('100%').backgroundColor('#F4F7F2')
  }
}
