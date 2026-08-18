import { PageHeader, StatePanel, StatusTag } from '../components/CommonComponents'
import { AdminMember } from '../models/Models'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct AdminPage {
  @State members: AdminMember[] = []
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
      await this.service.checkAdmin()
      this.members = await this.service.getAdminUsers()
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '无企业管理权限'
    } finally {
      this.loading = false
    }
  }

  build() {
    Column() {
      PageHeader({ title: '管理员', subtitle: '企业成员与账号状态' })
      if (this.loading || this.errorText.length > 0 || this.members.length === 0) {
        StatePanel({ loading: this.loading, message: this.errorText || '暂无企业成员' })
      } else {
        List({ space: 10 }) {
          ForEach(this.members, (member: AdminMember) => {
            ListItem() {
              Row({ space: 12 }) {
                Text(member.user.username.substring(0, 1).toUpperCase())
                  .width(42)
                  .height(42)
                  .textAlign(TextAlign.Center)
                  .fontSize(16)
                  .fontWeight(FontWeight.Bold)
                  .fontColor('#176B43')
                  .backgroundColor('#E0EFE6')
                  .borderRadius(21)
                Column({ space: 4 }) {
                  Text(member.user.username)
                    .fontSize(16)
                    .fontWeight(FontWeight.Bold)
                    .fontColor('#17211B')
                  Text(`${member.user.phone} · ${member.role}`)
                    .fontSize(12)
                    .fontColor('#68736C')
                }
                .alignItems(HorizontalAlign.Start)
                .layoutWeight(1)
                StatusTag({
                  text: member.user.status === '0' ? '停用' : '正常',
                  positive: member.user.status !== '0'
                })
              }
              .width('100%')
              .padding(15)
              .backgroundColor(Color.White)
              .borderRadius(7)
            }
          }, (member: AdminMember) => String(member.user.id))
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
