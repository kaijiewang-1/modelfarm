import { promptAction, router } from '@kit.ArkUI'
import { CameraDevice, FarmSite, LiveTicket } from '../models/Models'
import { FarmService } from '../services/FarmService'
import { LiveService } from '../services/LiveService'

@Entry
@Component
struct LivePage {
  @State cameras: CameraDevice[] = []
  @State selectedCameraId: number = -1
  @State selectedSite: FarmSite | undefined = undefined
  @State ticket: LiveTicket | undefined = undefined
  @State loading: boolean = true
  @State requestingTicket: boolean = false
  @State errorText: string = ''
  private farmService: FarmService = FarmService.getInstance()
  private liveService: LiveService = LiveService.getInstance()

  aboutToAppear(): void {
    this.load()
  }

  private async load(): Promise<void> {
    this.loading = true
    this.errorText = ''
    try {
      const farms = await this.farmService.getFarms()
      if (farms.length === 0) {
        this.errorText = '暂无养殖场，无法加载摄像头'
        return
      }
      const sites = await this.farmService.getFarmSites(farms[0].id)
      if (sites.length === 0) {
        this.errorText = '暂无养殖点，无法加载摄像头'
        return
      }
      this.selectedSite = sites[0]
      this.cameras = await this.liveService.getCameras(sites[0].id)
      if (this.cameras.length > 0) {
        this.selectedCameraId = this.cameras[0].id
      }
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '摄像头加载失败'
    } finally {
      this.loading = false
    }
  }

  private async createTicket(camera: CameraDevice): Promise<void> {
    this.selectedCameraId = camera.id
    this.requestingTicket = true
    this.ticket = undefined
    this.errorText = ''
    try {
      this.ticket = await this.liveService.requestTicket(camera.id)
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '智慧直播初始化失败'
    } finally {
      this.requestingTicket = false
    }
  }

  @Builder
  private cameraCard(camera: CameraDevice) {
    Column({ space: 8 }) {
      Row({ space: 10 }) {
        Text('视')
          .width(38)
          .height(38)
          .textAlign(TextAlign.Center)
          .fontColor('#176B43')
          .backgroundColor('#E0EFE6')
          .borderRadius(6)
        Column({ space: 3 }) {
          Text(camera.name).fontSize(15).fontWeight(FontWeight.Bold).fontColor('#17211B')
          Text(`设备 ${camera.id} · ${camera.streamId || camera.pushName || '未配置流'}`)
            .fontSize(11).fontColor('#68736C').maxLines(1)
        }
        .alignItems(HorizontalAlign.Start)
        .layoutWeight(1)
        Text(Number(camera.status) === 1 ? '在线' : '离线')
          .fontSize(11)
          .fontColor(Number(camera.status) === 1 ? '#176B43' : '#8A4D3B')
      }
      .width('100%')
      Text(camera.url || '后端未下发 HTTP-FLV 地址')
        .fontSize(11).fontColor('#7D8780').maxLines(1).width('100%')
      Button(this.requestingTicket && this.selectedCameraId === camera.id ? '初始化中…' : '获取智慧直播票据')
        .width('100%').height(40).fontSize(12)
        .fontColor(Color.White).backgroundColor('#176B43').borderRadius(5)
        .enabled(!this.requestingTicket)
        .onClick(() => this.createTicket(camera))
    }
    .width('100%').padding(14).backgroundColor(Color.White).borderRadius(7)
  }

  build() {
    Column() {
      Row() {
        Column({ space: 3 }) {
          Text('视频中心').fontSize(22).fontWeight(FontWeight.Bold).fontColor('#17211B')
          Text(this.selectedSite ? `${this.selectedSite.name} · ${this.cameras.length} 路摄像头` : '摄像头与智慧直播')
            .fontSize(12).fontColor('#68736C')
        }
        .alignItems(HorizontalAlign.Start)
        .layoutWeight(1)
        Button('刷新').fontSize(12).fontColor('#176B43').backgroundColor('#E0EFE6')
          .onClick(() => this.load())
      }
      .width('100%').padding({ left: 18, right: 18, top: 18, bottom: 12 })

      if (this.ticket) {
        Column({ space: 8 }) {
          Text('智慧直播会话已创建').fontSize(15).fontWeight(FontWeight.Bold).fontColor('#176B43')
          Text(`streamId: ${this.ticket.streamId}`).fontSize(11).fontColor('#536058')
          Text(`WebSocket: ${this.ticket.wsUrl}`).fontSize(11).fontColor('#536058').maxLines(2)
          Text('当前版本完成票据与地址联通；HTTP-FLV 播放与检测框绘制需接入设备媒体组件。')
            .fontSize(11).fontColor('#68736C').lineHeight(16)
        }
        .width('100%').margin({ left: 18, right: 18, bottom: 12 }).padding(14)
        .backgroundColor('#EAF5EE').borderRadius(7)
      }

      if (this.loading || this.errorText.length > 0 || this.cameras.length === 0) {
        Column({ space: 10 }) {
          if (this.loading) {
            LoadingProgress().width(30).height(30).color('#176B43')
          }
          Text(this.errorText || '暂无摄像头').fontSize(13).fontColor('#68736C')
          if (this.errorText.length > 0) {
            Button('重试').height(38).fontColor('#176B43').backgroundColor('#E0EFE6')
              .onClick(() => this.load())
          }
        }
        .layoutWeight(1).width('100%').justifyContent(FlexAlign.Center)
      } else {
        List({ space: 10 }) {
          ForEach(this.cameras, (camera: CameraDevice) => {
            ListItem() { this.cameraCard(camera) }
          }, (camera: CameraDevice) => String(camera.id))
        }
        .layoutWeight(1).width('100%').padding({ left: 18, right: 18, bottom: 18 })
      }

      Row() {
        Button('首页').layoutWeight(1).fontSize(12).fontColor('#68736C').backgroundColor(Color.Transparent)
          .onClick(() => router.replaceUrl({ url: 'pages/HomePage' }))
        Button('设备').layoutWeight(1).fontSize(12).fontColor('#68736C').backgroundColor(Color.Transparent)
          .onClick(() => router.replaceUrl({ url: 'pages/DevicePage' }))
      }
      .width('100%').height(56).backgroundColor(Color.White)
    }
    .width('100%').height('100%').backgroundColor('#F4F7F2')
  }
}
