import { router } from '@kit.ArkUI'
import { webview } from '@kit.ArkWeb'
import { PageHeader, StatePanel } from '../components/CommonComponents'
import { CameraDevice, FarmSite, PageParams, LiveTicket } from '../models/Models'
import { FarmService } from '../services/FarmService'
import { LiveService } from '../services/LiveService'
import { resolvePlaybackUrl } from '../services/StreamResolver'

interface PlaybackConfig {
  url: string
  wsUrl?: string
  ticket?: string
  streamId?: string
  algoEpoch?: number
}

@Entry
@Component
struct LivePage {
  @State sites: FarmSite[] = []
  @State cameras: CameraDevice[] = []
  @State selectedSiteId: number = 0
  @State selectedCameraId: number = 0
  @State loading: boolean = true
  @State requestingTicket: boolean = false
  @State errorText: string = ''
  @State playerVisible: boolean = false
  @State playingName: string = ''
  private farmService: FarmService = FarmService.getInstance()
  private liveService: LiveService = LiveService.getInstance()
  private controller: webview.WebviewController = new webview.WebviewController()
  private playerReady: boolean = false
  private playback: PlaybackConfig | undefined = undefined
  private requestVersion: number = 0

  aboutToAppear(): void {
    const params = router.getParams() as PageParams
    this.selectedSiteId = params?.siteId || 0
    this.selectedCameraId = params?.deviceId || 0
    this.load()
  }

  onPageHide(): void { this.stop() }
  aboutToDisappear(): void { this.stop() }

  private stop(hide: boolean = true): void {
    this.requestVersion++
    if (this.playerReady) {
      this.controller.runJavaScript('window.stopPlayback && window.stopPlayback()').catch(() => {})
    }
    this.playback = undefined
    if (hide) {
      this.playerReady = false
      this.playerVisible = false
    }
  }

  private async load(): Promise<void> {
    this.stop()
    this.loading = true
    this.errorText = ''
    try {
      const farms = await this.farmService.getFarms()
      let sites: FarmSite[] = []
      for (const farm of farms || []) {
        sites = sites.concat(await this.farmService.getFarmSites(farm.id) || [])
      }
      this.sites = sites
      if (sites.length === 0) { this.cameras = []; return }
      const selected = sites.find((site: FarmSite) => site.id === this.selectedSiteId)
      await this.loadCameras(selected ? selected.id : sites[0].id)
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '摄像头加载失败'
    } finally { this.loading = false }
  }

  private async loadCameras(siteId: number): Promise<void> {
    this.stop()
    const version: number = this.requestVersion
    this.selectedSiteId = siteId
    this.cameras = []
    this.errorText = ''
    this.loading = true
    try {
      const cameras = await this.liveService.getCameras(siteId)
      if (version === this.requestVersion) { this.cameras = cameras || [] }
    } catch (error) {
      if (version === this.requestVersion) { this.errorText = error instanceof Error ? error.message : '摄像头加载失败' }
    } finally { if (version === this.requestVersion) { this.loading = false } }
  }

  private async play(camera: CameraDevice, smart: boolean): Promise<void> {
    this.stop(false)
    const version: number = this.requestVersion
    this.selectedCameraId = camera.id
    this.errorText = ''
    this.requestingTicket = smart
    try {
      let config: PlaybackConfig = { url: resolvePlaybackUrl(camera.pushName, camera.url) }
      if (smart) {
        const ticket: LiveTicket = await this.liveService.requestDetectionSession(camera.id)
        config = { url: resolvePlaybackUrl('', ticket.algoFlvUrl), wsUrl: ticket.wsUrl,
          ticket: ticket.ticket, streamId: ticket.streamId, algoEpoch: ticket.algoEpoch }
      }
      if (version !== this.requestVersion) { return }
      if (config.url.length === 0) { throw new Error(smart ? '暂未提供智慧视频源，请使用原画直播' : '该摄像头未配置有效视频源') }
      this.playback = config
      this.playingName = `${camera.name} · ${smart ? '智慧直播' : '原画直播'}`
      this.playerVisible = true
      if (this.playerReady) { this.startPlayer() }
    } catch (error) {
      if (version === this.requestVersion) { this.errorText = error instanceof Error ? error.message : '视频加载失败' }
    } finally { if (version === this.requestVersion) { this.requestingTicket = false } }
  }

  private startPlayer(): void {
    this.playerReady = true
    if (this.playback) {
      // JSON serialization passes data as a JS value; never interpolate a URL as executable code.
      this.controller.runJavaScript(`window.startPlayback(${JSON.stringify(this.playback)})`)
        .catch(() => this.errorText = '播放器初始化失败，请重试')
    }
  }

  build() {
    Column({ space: 10 }) {
      PageHeader({ title: '视频中心', subtitle: '养殖现场 · 原画与智慧直播' })
      Row({ space: 10 }) {
        Select(this.sites.map((site: FarmSite): SelectOption => { return { value: site.name } }))
          .value(this.sites.find((site: FarmSite) => site.id === this.selectedSiteId)?.name || '选择养殖点')
          .layoutWeight(1).enabled(!this.loading && !this.requestingTicket)
          .onSelect((index: number) => this.loadCameras(this.sites[index].id))
        Button('刷新').fontColor('#176B43').backgroundColor('#E0EFE6').enabled(!this.loading)
          .onClick(() => this.load())
      }.width('100%').padding({ left: 18, right: 18 })
      if (this.playerVisible) {
        Text(this.playingName).fontSize(14).fontWeight(FontWeight.Bold).fontColor('#17211B')
        Web({ src: $rawfile('live/player.html'), controller: this.controller })
          .javaScriptAccess(true).domStorageAccess(false).databaseAccess(false)
          .mixedMode(MixedMode.All).height(340).width('100%')
          .onPageEnd(() => this.startPlayer())
          .onLoadIntercept((event) => {
            const target: string = event?.data.getRequestUrl() || ''
            return !target.startsWith('resource://') && !target.startsWith('file://') && target !== 'about:blank'
          })
      }
      if (this.errorText.length > 0) {
        Text(this.errorText).fontColor('#B33B2E').fontSize(13).width('100%').padding({ left: 18, right: 18 })
      }
      if (this.loading || this.cameras.length === 0) {
        StatePanel({ loading: this.loading, message: this.errorText || '当前养殖点暂无摄像头' })
      } else {
        List({ space: 10 }) {
          ForEach(this.cameras, (camera: CameraDevice) => {
            ListItem() {
              Column({ space: 10 }) {
                Row() {
                  Text(camera.name).layoutWeight(1).fontSize(16).fontWeight(FontWeight.Bold).fontColor('#17211B')
                  Text(Number(camera.status) === 1 ? '在线' : '离线').fontSize(12).fontColor('#68736C')
                }.width('100%')
                Row({ space: 12 }) {
                  Button('原画直播').layoutWeight(1).fontColor(Color.White).backgroundColor('#176B43')
                    .enabled(!this.requestingTicket).onClick(() => this.play(camera, false))
                  Button(this.requestingTicket && this.selectedCameraId === camera.id ? '连接中…' : '智慧直播')
                    .layoutWeight(1).fontColor('#176B43').backgroundColor('#E0EFE6')
                    .enabled(!this.requestingTicket).onClick(() => this.play(camera, true))
                }
              }.width('100%').padding(16).backgroundColor(Color.White).borderRadius(8)
            }
          }, (camera: CameraDevice) => String(camera.id))
        }.layoutWeight(1).width('100%').padding({ left: 18, right: 18, bottom: 18 })
      }
    }.width('100%').height('100%').backgroundColor('#F4F7F2')
  }
}
