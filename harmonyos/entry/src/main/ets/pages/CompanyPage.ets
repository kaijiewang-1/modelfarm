import { PageHeader, StatePanel, StatusTag } from '../components/CommonComponents'
import { Enterprise } from '../models/Models'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct CompanyPage {
  @State enterprise: Enterprise | undefined = undefined
  @State loading: boolean = true
  @State errorText: string = ''
  private service: FarmService = FarmService.getInstance()

  aboutToAppear(): void {
    this.load()
  }

  private async load(): Promise<void> {
    this.loading = true
    this.errorText = ''
    try {
      this.enterprise = await this.service.getEnterprise()
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '企业信息加载失败'
    } finally {
      this.loading = false
    }
  }

  @Builder
  private infoRow(label: string, value: string) {
    Row() {
      Text(label)
        .width(84)
        .fontSize(13)
        .fontColor('#68736C')
      Text(value || '待完善')
        .layoutWeight(1)
        .fontSize(14)
        .fontColor('#17211B')
        .textAlign(TextAlign.End)
    }
    .width('100%')
    .height(48)
    .border({ width: { bottom: 1 }, color: '#EDF0EE' })
  }

  build() {
    Column() {
      PageHeader({ title: '企业信息', subtitle: '企业主体与联系方式' })
      if (this.loading || this.errorText.length > 0 || !this.enterprise) {
        StatePanel({ loading: this.loading, message: this.errorText || '暂无企业信息' })
      } else {
        Scroll() {
          Column({ space: 16 }) {
            Stack({ alignContent: Alignment.BottomStart }) {
              Image($r('app.media.company'))
                .width('100%')
                .height(178)
                .objectFit(ImageFit.Cover)
                .borderRadius(8)
              Column({ space: 5 }) {
                Text(this.enterprise.name)
                  .fontSize(22)
                  .fontWeight(FontWeight.Bold)
                  .fontColor(Color.White)
                StatusTag({ text: this.enterprise.status === '0' ? '停用' : '正常运营', positive: this.enterprise.status !== '0' })
              }
              .alignItems(HorizontalAlign.Start)
              .padding(18)
              .width('100%')
              .backgroundColor('#66000000')
            }
            .width('100%')
            .height(178)

            Column() {
              this.infoRow('企业地址', this.enterprise.address)
              this.infoRow('联系人', this.enterprise.contactPerson)
              this.infoRow('联系电话', this.enterprise.contactPhone)
              this.infoRow('邀请码', this.enterprise.invitedCode)
              this.infoRow('创建时间', this.enterprise.createdAt)
            }
            .width('100%')
            .padding({ left: 16, right: 16, top: 4, bottom: 4 })
            .backgroundColor(Color.White)
            .borderRadius(7)
          }
          .width('100%')
          .padding({ left: 18, right: 18, bottom: 18 })
        }
        .layoutWeight(1)
      }
    }
    .width('100%')
    .height('100%')
    .backgroundColor('#F4F7F2')
  }
}
