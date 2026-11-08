import { router } from '@kit.ArkUI'
import { PageHeader, StatePanel } from '../components/CommonComponents'
import { FarmSite, PageParams } from '../models/Models'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct FarmDetailPage {
  @State sites: FarmSite[] = []
  @State farmName: string = '养殖场详情'
  @State loading: boolean = true
  @State errorText: string = ''
  private farmId: number = 0

  aboutToAppear(): void {
    const params = router.getParams() as PageParams
    this.farmId = params?.farmId || 0
    this.farmName = params?.farmName || '养殖场详情'
    this.load()
  }

  private async load(): Promise<void> {
    this.loading = true
    this.errorText = ''
    try {
      if (this.farmId <= 0) { throw new Error('养殖场信息无效，请返回重试') }
      this.sites = await FarmService.getInstance().getFarmSites(this.farmId) || []
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '养殖点加载失败'
    } finally { this.loading = false }
  }

  build() {
    Column({ space: 12 }) {
      PageHeader({ title: this.farmName, subtitle: `${this.sites.length} 个养殖点` })
      Button('刷新').height(36).fontColor('#176B43').backgroundColor('#E0EFE6')
        .enabled(!this.loading).onClick(() => this.load())
      if (this.loading || this.errorText.length > 0 || this.sites.length === 0) {
        StatePanel({ loading: this.loading, message: this.errorText || '暂无养殖点' })
      } else {
        List({ space: 12 }) {
          ForEach(this.sites, (site: FarmSite) => {
            ListItem() {
              Column({ space: 8 }) {
                Text(site.name).fontSize(18).fontWeight(FontWeight.Bold).fontColor('#17211B')
                Text(`存栏：${site.sum === undefined || site.sum === null ? '--' : site.sum}`)
                  .fontSize(13).fontColor('#68736C')
                Text('查看设备与现场监控 ›').fontSize(13).fontColor('#176B43')
              }
              .width('100%').alignItems(HorizontalAlign.Start).padding(18)
              .backgroundColor(Color.White).borderRadius(8)
              .onClick(() => router.pushUrl({ url: 'pages/SiteDetailPage', params: { siteId: site.id, siteName: site.name } }))
            }
          }, (site: FarmSite) => String(site.id))
        }
        .layoutWeight(1).width('100%').padding({ left: 18, right: 18, bottom: 18 })
      }
    }
    .width('100%').height('100%').backgroundColor('#F4F7F2')
  }
}
