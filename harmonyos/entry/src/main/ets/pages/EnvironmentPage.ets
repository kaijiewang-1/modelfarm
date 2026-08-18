import { promptAction } from '@kit.ArkUI'
import { PageHeader, StatePanel } from '../components/CommonComponents'
import { Device } from '../models/Models'
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
    this.loadSensors()
  }

  private async loadSensors(): Promise<void> {
    this.loading = true
    this.errorText = ''
    try {
      const result = await this.service.getDevices(2)
      this.sensors = result.records
      if (this.sensors.length > 0) {
        await this.selectSensor(this.sensors[0].id)
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
      this.reading = parseEnvironment(data)
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
            GridItem() { this.metric('光照', this.reading.light, '光', '#B27A19') }
            GridItem() { this.metric('空气质量', this.reading.airQuality, '气', '#4E7D59') }
            GridItem() { this.metric('风速', this.reading.windSpeed, '风', '#477E8D') }
            GridItem() { this.metric('水位', this.reading.waterLevel, '水', '#336EAA') }
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
