import { router } from '@kit.ArkUI'
import { PageHeader, StatePanel } from '../components/CommonComponents'
import { Farm } from '../models/Models'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct FarmPage {
  @State farms: Farm[] = []
  @State query: string = ''
  @State loading: boolean = true
  @State errorText: string = ''
  private service: FarmService = FarmService.getInstance()

  onPageShow(): void {
    this.load()
  }

  private async load(): Promise<void> {
    this.loading = true
    this.errorText = ''
    try {
      this.farms = await this.service.getFarms()
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '养殖场加载失败'
    } finally {
      this.loading = false
    }
  }

  private visibleFarms(): Farm[] {
    const keyword: string = this.query.trim().toLowerCase()
    if (keyword.length === 0) {
      return this.farms
    }
    return this.farms.filter((farm: Farm) =>
      farm.name.toLowerCase().includes(keyword) || farm.address.toLowerCase().includes(keyword))
  }

  build() {
    Column() {
      PageHeader({ title: '养殖场管理', subtitle: `${this.farms.length} 个场区` })
      Button('新建养殖场').height(36).fontColor(Color.White).backgroundColor('#176B43')
        .onClick(() => router.pushUrl({ url: 'pages/LocationCreatePage' }))
      Row({ space: 10 }) {
        TextInput({ text: this.query, placeholder: '搜索名称或地址' })
          .layoutWeight(1)
          .height(44)
          .backgroundColor(Color.White)
          .borderRadius(6)
          .onChange((value: string) => this.query = value)
        Button('↻')
          .width(44)
          .height(44)
          .fontSize(20)
          .fontColor('#176B43')
          .backgroundColor('#E0EFE6')
          .borderRadius(6)
          .onClick(() => this.load())
      }
      .width('100%')
      .padding({ left: 18, right: 18, bottom: 14 })

      if (this.loading || this.errorText.length > 0 || this.visibleFarms().length === 0) {
        StatePanel({ loading: this.loading, message: this.errorText || '暂无养殖场数据' })
      } else {
        List({ space: 10 }) {
          ForEach(this.visibleFarms(), (farm: Farm) => {
            ListItem() {
              Row({ space: 13 }) {
                Text('场')
                  .width(44)
                  .height(44)
                  .textAlign(TextAlign.Center)
                  .fontSize(17)
                  .fontWeight(FontWeight.Bold)
                  .fontColor('#176B43')
                  .backgroundColor('#E0EFE6')
                  .borderRadius(6)
                Column({ space: 5 }) {
                  Text(farm.name)
                    .fontSize(16)
                    .fontWeight(FontWeight.Bold)
                    .fontColor('#17211B')
                    .maxLines(1)
                  Text(farm.address || '地址待完善')
                    .fontSize(12)
                    .fontColor('#68736C')
                    .maxLines(1)
                }
                .alignItems(HorizontalAlign.Start)
                .layoutWeight(1)
                Text('›')
                  .fontSize(25)
                  .fontColor('#9AA39D')
              }
              .width('100%')
              .padding(15)
              .backgroundColor(Color.White)
              .borderRadius(7)
              .onClick(() => router.pushUrl({ url: 'pages/FarmDetailPage', params: { farmId: farm.id, farmName: farm.name } }))
            }
          }, (farm: Farm) => String(farm.id))
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
