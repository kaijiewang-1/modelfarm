import { router } from '@kit.ArkUI'
import { PageHeader, StatePanel, StatusTag } from '../components/CommonComponents'
import { Device, deviceStatusCode, deviceStatusText, deviceTypeText } from '../models/Models'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct DevicePage {
  @State devices: Device[] = []
  @State selectedType: number = 0
  @State loading: boolean = true
  @State errorText: string = ''
  private service: FarmService = FarmService.getInstance()

  onPageShow(): void {
    this.load()
  }

  private async load(): Promise<void> {
    const requestedType: number = this.selectedType
    this.loading = true
    this.errorText = ''
    try {
      const result = await this.service.getDevices(requestedType === 0 ? undefined : requestedType)
      if (requestedType === this.selectedType) { this.devices = result?.records || [] }
    } catch (error) {
      if (requestedType === this.selectedType) { this.errorText = error instanceof Error ? error.message : '设备加载失败' }
    } finally {
      if (requestedType === this.selectedType) { this.loading = false }
    }
  }

  @Builder
  private filterButton(label: string, type: number) {
    Button(label)
      .height(36)
      .fontSize(12)
      .fontColor(this.selectedType === type ? Color.White : '#536058')
      .backgroundColor(this.selectedType === type ? '#176B43' : Color.White)
      .borderRadius(5)
      .onClick(() => {
        this.selectedType = type
        this.load()
      })
  }

  build() {
    Column() {
      PageHeader({ title: '设备管理', subtitle: '摄像头、传感器与控制器' })
      Row({ space: 12 }) {
        Button('添加设备').height(36).fontColor(Color.White).backgroundColor('#176B43')
          .onClick(() => router.pushUrl({ url: 'pages/DeviceCreatePage' }))
        Button('刷新').height(36).fontColor('#176B43').backgroundColor('#E0EFE6')
          .enabled(!this.loading).onClick(() => this.load())
      }.width('100%').padding({ left: 18, right: 18, bottom: 10 })
      Scroll() {
        Row({ space: 8 }) {
          this.filterButton('全部', 0)
          this.filterButton('摄像头', 1)
          this.filterButton('传感器', 2)
          this.filterButton('控制器', 3)
        }
        .padding({ left: 18, right: 18 })
      }
      .scrollable(ScrollDirection.Horizontal)
      .width('100%')
      .scrollBar(BarState.Off)
      .height(48)

      if (this.loading || this.errorText.length > 0 || this.devices.length === 0) {
        StatePanel({ loading: this.loading, message: this.errorText || '暂无设备数据' })
      } else {
        List({ space: 10 }) {
          ForEach(this.devices, (device: Device) => {
            ListItem() {
              Column({ space: 12 }) {
                Row({ space: 12 }) {
                  Text(device.type === 1 ? '像' : device.type === 2 ? '感' : '控')
                    .width(42)
                    .height(42)
                    .textAlign(TextAlign.Center)
                    .fontSize(15)
                    .fontWeight(FontWeight.Bold)
                    .fontColor('#176B43')
                    .backgroundColor('#E0EFE6')
                    .borderRadius(6)
                  Column({ space: 4 }) {
                    Text(device.name)
                      .fontSize(16)
                      .fontWeight(FontWeight.Bold)
                      .fontColor('#17211B')
                      .maxLines(1)
                    Text(`${deviceTypeText(device.type)} · ${device.siteName || `站点 ${device.siteId}`}`)
                      .fontSize(12)
                      .fontColor('#68736C')
                  }
                  .alignItems(HorizontalAlign.Start)
                  .layoutWeight(1)
                  StatusTag({
                    text: deviceStatusText(device.status),
                    positive: deviceStatusCode(device.status) === 1,
                    warning: deviceStatusCode(device.status) === 3
                  })
                }
                Row() {
                  Text(`MAC ${device.mac || '未登记'}`)
                    .fontSize(11)
                    .fontColor('#7D8780')
                  Blank()
                  Text(device.updatedAt || '暂无更新时间')
                    .fontSize(11)
                    .fontColor('#9AA39D')
                }
                .width('100%')
              }
              .width('100%')
              .padding(15)
              .backgroundColor(Color.White)
              .borderRadius(7)
              .onClick(() => router.pushUrl({ url: 'pages/DeviceDetailPage', params: { deviceId: device.id } }))
            }
          }, (device: Device) => String(device.id))
        }
        .layoutWeight(1)
        .width('100%')
        .padding({ left: 18, right: 18, bottom: 18 })
      }
    }
    .width('100%')
    .height('100%')
    .backgroundColor('#F4F7F2')
  }
}
