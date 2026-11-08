import { promptAction, router } from '@kit.ArkUI'
import { Device, EnterpriseStats } from '../models/Models'
import { emptyEnvironment, EnvironmentReading, parseEnvironment, hasEnvironmentReadings } from '../services/EnvironmentParser'
import { FarmService } from '../services/FarmService'

interface ModuleItem {
  mark: string
  title: string
  caption: string
  url: string
  color: string
}

@Entry
@Component
struct HomePage {
  @State loading: boolean = true
  @State stats: EnterpriseStats = {
    onlineDeviceCount: 0,
    pendingOrderCount: 0,
    userCount: 0,
    deviceCount: 0,
    farmCount: 0,
    faultDeviceCount: 0
  }
  @State environment: EnvironmentReading = emptyEnvironment()
  @State refreshedAt: string = '--:--'
  @State errorText: string = ''
  private service: FarmService = FarmService.getInstance()
  private refreshTimer: number = -1
  private modules: ModuleItem[] = [
    { mark: '场', title: '养殖场', caption: '场区与养殖点', url: 'pages/FarmPage', color: '#DFF1E7' },
    { mark: '设', title: '设备管理', caption: '在线与故障状态', url: 'pages/DevicePage', color: '#E3EEF8' },
    { mark: '视', title: '视频中心', caption: '摄像头与智慧直播', url: 'pages/LivePage', color: '#E7E3F4' },
    { mark: '单', title: '工单管理', caption: '任务闭环处理', url: 'pages/OrderPage', color: '#F8EBD7' },
    { mark: '企', title: '企业信息', caption: '主体与联系人', url: 'pages/CompanyPage', color: '#EEE8F6' },
    { mark: '我', title: '个人资料', caption: '账号与安全', url: 'pages/ProfilePage', color: '#E8EFEB' },
    { mark: '权', title: '管理员', caption: '用户权限管理', url: 'pages/AdminPage', color: '#F5E5E3' }
  ]

  onPageShow(): void {
    this.refresh()
    if (this.refreshTimer >= 0) { clearInterval(this.refreshTimer) }
    this.refreshTimer = setInterval(() => this.refresh(), 30000)
  }

  onPageHide(): void {
    if (this.refreshTimer >= 0) {
      clearInterval(this.refreshTimer)
      this.refreshTimer = -1
    }
  }

  private async refresh(): Promise<void> {
    if (this.loading && this.refreshTimer >= 0) { return }
    this.loading = true
    this.errorText = ''
    try {
      this.stats = await this.service.getStats()
      await this.loadEnvironment()
      const now: Date = new Date()
      this.refreshedAt = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '数据刷新失败'
      promptAction.showToast({ message: error instanceof Error ? error.message : '数据刷新失败' })
    } finally {
      this.loading = false
    }
  }

  private async loadEnvironment(): Promise<void> {
    this.environment = emptyEnvironment()
    const sensors = await this.service.getDevices(2)
    for (let index: number = 0; index < sensors.records.length; index++) {
      const sensor: Device = sensors.records[index]
      try {
        const latest = await this.service.getLatestDeviceData(sensor.id)
        if (hasEnvironmentReadings(latest)) {
          this.environment = parseEnvironment(latest)
          return
        }
      } catch (error) {
        // Continue with the next sensor; one offline device must not block the dashboard.
      }
    }
  }

  @Builder
  private statItem(value: string, label: string, accent: string) {
    Column({ space: 5 }) {
      Text(value)
        .fontSize(25)
        .fontWeight(FontWeight.Bold)
        .fontColor(accent)
      Text(label)
        .fontSize(12)
        .fontColor('#68736C')
    }
    .layoutWeight(1)
    .alignItems(HorizontalAlign.Start)
  }

  @Builder
  private environmentItem(label: string, value: string, accent: string) {
    Column({ space: 8 }) {
      Row() {
        Text(label)
          .fontSize(12)
          .fontColor('#68736C')
        Blank()
        Circle({ width: 7, height: 7 }).fill(accent)
      }
      .width('100%')
      Text(value)
        .width('100%')
        .fontSize(23)
        .fontWeight(FontWeight.Bold)
        .fontColor('#17211B')
    }
    .padding(15)
    .backgroundColor(Color.White)
    .borderRadius(7)
  }

  build() {
    Column() {
      Scroll() {
        Column({ space: 18 }) {
          Row() {
            Column({ space: 3 }) {
              Text('数智化养殖')
                .fontSize(23)
                .fontWeight(FontWeight.Bold)
                .fontColor('#17211B')
              Text(`数据更新 ${this.refreshedAt}`)
                .fontSize(12)
                .fontColor('#68736C')
            }
            .alignItems(HorizontalAlign.Start)
            .layoutWeight(1)
            Button('↻')
              .width(42)
              .height(42)
              .fontSize(22)
              .fontColor('#176B43')
              .backgroundColor('#E0EFE6')
              .borderRadius(6)
              .enabled(!this.loading)
              .onClick(() => this.refresh())
          }
          .width('100%')

          Column({ space: 18 }) {
            Row() {
              Column({ space: 4 }) {
                Text('企业运行总览')
                  .fontSize(12)
                  .fontColor('#D9F2E4')
                Text('今天的养殖现场\n尽在掌握')
                  .fontSize(25)
                  .lineHeight(34)
                  .fontWeight(FontWeight.Bold)
                  .fontColor(Color.White)
              }
              .alignItems(HorizontalAlign.Start)
              .layoutWeight(1)
              Text(this.loading ? '同步中' : this.errorText.length > 0 ? '同步失败' : '已同步')
                .fontSize(11)
                .fontColor('#173B28')
                .backgroundColor('#B9E769')
                .padding({ left: 10, right: 10, top: 6, bottom: 6 })
                .borderRadius(4)
            }
            Row({ space: 20 }) {
              this.statItem(String(this.stats.farmCount), '养殖场', '#FFFFFF')
              this.statItem(String(this.stats.deviceCount), '设备', '#FFFFFF')
              this.statItem(String(this.stats.faultDeviceCount), '预警', '#F8D585')
            }
          }
          .width('100%')
          .padding(20)
          .backgroundColor('#176B43')
          .borderRadius(8)

          Row() {
            Text('实时环境')
              .fontSize(18)
              .fontWeight(FontWeight.Bold)
              .fontColor('#17211B')
            Blank()
            Button('查看全部')
              .fontSize(12)
              .fontColor('#176B43')
              .backgroundColor(Color.Transparent)
              .onClick(() => router.pushUrl({ url: 'pages/EnvironmentPage' }))
          }
          .width('100%')

          Grid() {
            GridItem() { this.environmentItem('温度', this.environment.temperature, '#E76F51') }
            GridItem() { this.environmentItem('湿度', this.environment.humidity, '#3A86C8') }
          }
          .columnsTemplate('1fr 1fr')
          .columnsGap(12)
          .height(105)

          Text('业务工作台')
            .width('100%')
            .fontSize(18)
            .fontWeight(FontWeight.Bold)
            .fontColor('#17211B')

          Grid() {
            ForEach(this.modules, (item: ModuleItem) => {
              GridItem() {
                Row({ space: 12 }) {
                  Text(item.mark)
                    .width(40)
                    .height(40)
                    .textAlign(TextAlign.Center)
                    .fontSize(16)
                    .fontWeight(FontWeight.Bold)
                    .fontColor('#17211B')
                    .backgroundColor(item.color)
                    .borderRadius(6)
                  Column({ space: 3 }) {
                    Text(item.title)
                      .fontSize(15)
                      .fontWeight(FontWeight.Bold)
                      .fontColor('#17211B')
                    Text(item.caption)
                      .fontSize(11)
                      .fontColor('#68736C')
                  }
                  .alignItems(HorizontalAlign.Start)
                  .layoutWeight(1)
                  Text('›')
                    .fontSize(24)
                    .fontColor('#98A19B')
                }
                .width('100%')
                .height('100%')
                .padding(12)
                .backgroundColor(Color.White)
                .borderRadius(7)
                .onClick(() => router.pushUrl({ url: item.url }))
              }
            }, (item: ModuleItem) => item.url)
          }
          .columnsTemplate('1fr 1fr')
          .columnsGap(12)
          .rowsGap(12)
          .height(336)
          .margin({ bottom: 20 })
        }
        .width('100%')
        .padding({ left: 18, right: 18, top: 18 })
      }
      .layoutWeight(1)

      Row() {
        this.navItem('⌂', '首页', true, 'pages/HomePage')
        this.navItem('场', '养殖场', false, 'pages/FarmPage')
        this.navItem('设', '设备', false, 'pages/DevicePage')
        this.navItem('单', '工单', false, 'pages/OrderPage')
      }
      .width('100%')
      .height(64)
      .backgroundColor(Color.White)
      .border({ width: { top: 1 }, color: '#E4E9E5' })
    }
    .width('100%')
    .height('100%')
    .backgroundColor('#F4F7F2')
  }

  @Builder
  private navItem(mark: string, label: string, active: boolean, url: string) {
    Column({ space: 2 }) {
      Text(mark)
        .fontSize(18)
        .fontWeight(FontWeight.Bold)
        .fontColor(active ? '#176B43' : '#7D8780')
      Text(label)
        .fontSize(10)
        .fontColor(active ? '#176B43' : '#7D8780')
    }
    .layoutWeight(1)
    .height('100%')
    .justifyContent(FlexAlign.Center)
    .onClick(() => {
      if (!active) {
        router.pushUrl({ url: url })
      }
    })
  }
}
