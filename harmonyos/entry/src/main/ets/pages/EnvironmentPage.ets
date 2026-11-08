import { promptAction, router } from '@kit.ArkUI'
import { PageHeader, StatePanel } from '../components/CommonComponents'
import { Device, PageParams } from '../models/Models'
import { emptyEnvironment, EnvironmentReading, parseEnvironment } from '../services/EnvironmentParser'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct EnvironmentPage {
  @State sensors: Device[] = []
  @State selectedSensorId: number = 0
  @State reading: EnvironmentReading = emptyEnvironment()
  @State loading: boolean = true
  @State errorText: string = ''
  private service: FarmService = FarmService.getInstance()

  aboutToAppear(): void {
    const params = router.getParams() as PageParams
    this.selectedSensorId = params?.deviceId || 0
    this.loadSensors()
  }

  private async loadSensors(): Promise<void> {
    this.loading = true
    this.errorText = ''
    try {
      const result = await this.service.getDevices(2)
      this.sensors = result.records
      if (this.sensors.length > 0) {
        const selected = this.sensors.find((sensor: Device) => sensor.id === this.selectedSensorId)
        await this.selectSensor(selected ? selected.id : this.sensors[0].id)
      }
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '环境数据加载失败'
    } finally {
      this.loading = false
    }
  }

  private async selectSensor(deviceId: number): Promise<void> {
    this.selectedSensorId = deviceId
    this.reading = emptyEnvironment()
    try {
      const data = await this.service.getLatestDeviceData(deviceId)
      if (this.selectedSensorId === deviceId) {
        this.reading = parseEnvironment(data)
      }
    } catch (error) {
      promptAction.showToast({ message: '该传感器暂无可用数据' })
    }
  }

  @Builder
  private metric(label: string, value: string, mark: string, color: string) {
    Row({ space: 12 }) {
      Text(mark)
        .width(40)
        .height(40)
        .textAlign(TextAlign.Center)
        .fontSize(15)
        .fontWeight(FontWeight.Bold)
        .fontColor(color)
        .backgroundColor('#F0F4F1')
        .borderRadius(6)
      Column({ space: 3 }) {
        Text(label)
          .fontSize(12)
          .fontColor('#68736C')
        Text(value)
          .fontSize(21)
          .fontWeight(FontWeight.Bold)
          .fontColor('#17211B')
      }
      .alignItems(HorizontalAlign.Start)
    }
    .width('100%')
    .padding(14)
    .backgroundColor(Color.White)
    .borderRadius(7)
  }

  build() {
    Column() {
      PageHeader({ title: '环境监测', subtitle: `更新于 ${this.reading.updatedAt}` })
      Button('刷新').height(36).fontColor('#176B43').backgroundColor('#E0EFE6')
        .enabled(!this.loading).onClick(() => this.loadSensors())
      if (this.loading) {
        StatePanel({ loading: true, message: '' })
      } else if (this.errorText.length > 0 || this.sensors.length === 0) {
        StatePanel({ loading: false, message: this.errorText || '暂无环境传感器' })
      } else {
        Scroll() {
          Row({ space: 8 }) {
            ForEach(this.sensors, (sensor: Device) => {
              Button(sensor.name)
                .height(36)
                .fontSize(12)
                .fontColor(this.selectedSensorId === sensor.id ? Color.White : '#536058')
                .backgroundColor(this.selectedSensorId === sensor.id ? '#176B43' : Color.White)
                .borderRadius(5)
                .onClick(() => this.selectSensor(sensor.id))
            }, (sensor: Device) => String(sensor.id))
          }
          .padding({ left: 18, right: 18 })
        }
        .scrollable(ScrollDirection.Horizontal)
        .height(48)
        .scrollBar(BarState.Off)

        Scroll() {
          Grid() {
            GridItem() { this.metric('温度', this.reading.temperature, '温', '#D85C41') }
            GridItem() { this.metric('湿度', this.reading.humidity, '湿', '#2D78B7') }
            GridItem() { this.metric('二氧化碳 CO₂', this.reading.co2, '碳', '#B27A19') }
            GridItem() { this.metric('氨气 NH₃', this.reading.nh3, '氨', '#4E7D59') }
            GridItem() { this.metric('PM2.5', this.reading.pm25, '尘', '#477E8D') }
            GridItem() { this.metric('PM10', this.reading.pm10, '尘', '#336EAA') }
          }
          .columnsTemplate('1fr 1fr')
          .columnsGap(10)
          .rowsGap(10)
          .height(290)
          .padding({ left: 18, right: 18, top: 8 })
        }
        .layoutWeight(1)
      }
    }
    .width('100%')
    .height('100%')
    .backgroundColor('#F4F7F2')
  }
}
