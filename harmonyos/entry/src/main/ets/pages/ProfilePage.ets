import { promptAction, router } from '@kit.ArkUI'
import { PageHeader, StatePanel, StatusTag } from '../components/CommonComponents'
import { User } from '../models/Models'
import { AuthStore } from '../services/AuthStore'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct ProfilePage {
  @State user: User | undefined = undefined
  @State loading: boolean = true
  @State signingOut: boolean = false
  @State errorText: string = ''
  private service: FarmService = FarmService.getInstance()

  aboutToAppear(): void {
    this.load()
  }

  private async load(): Promise<void> {
    this.loading = true
    this.errorText = ''
    try {
      this.user = await this.service.getCurrentUser()
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '用户信息加载失败'
    } finally {
      this.loading = false
    }
  }

  private async logout(): Promise<void> {
    this.signingOut = true
    try {
      await this.service.logout()
    } catch (error) {
      // Local session must still be cleared if the server is unreachable.
    }
    await AuthStore.getInstance().clearSession()
    promptAction.showToast({ message: '已安全退出' })
    router.replaceUrl({ url: 'pages/LoginPage' })
    this.signingOut = false
  }

  @Builder
  private infoRow(label: string, value: string) {
    Row() {
      Text(label).fontSize(13).fontColor('#68736C')
      Blank()
      Text(value || '待完善').fontSize(14).fontColor('#17211B')
    }
    .width('100%')
    .height(50)
    .border({ width: { bottom: 1 }, color: '#EDF0EE' })
  }

  build() {
    Column() {
      PageHeader({ title: '个人资料', subtitle: '账号与会话安全' })
      if (this.loading || this.errorText.length > 0 || !this.user) {
        StatePanel({ loading: this.loading, message: this.errorText || '暂无用户信息' })
      } else {
        Scroll() {
          Column({ space: 16 }) {
            Row({ space: 15 }) {
              Text(this.user.username.substring(0, 1).toUpperCase())
                .width(58)
                .height(58)
                .textAlign(TextAlign.Center)
                .fontSize(23)
                .fontWeight(FontWeight.Bold)
                .fontColor(Color.White)
                .backgroundColor('#176B43')
                .borderRadius(29)
              Column({ space: 5 }) {
                Text(this.user.username)
                  .fontSize(20)
                  .fontWeight(FontWeight.Bold)
                  .fontColor('#17211B')
                StatusTag({ text: this.user.status === '0' ? '账号停用' : '账号正常', positive: this.user.status !== '0' })
              }
              .alignItems(HorizontalAlign.Start)
            }
            .width('100%')
            .padding(18)
            .backgroundColor(Color.White)
            .borderRadius(7)

            Column() {
              this.infoRow('手机号', this.user.phone)
              this.infoRow('用户编号', String(this.user.id))
              this.infoRow('企业编号', String(this.user.enterpriseId))
              this.infoRow('注册时间', this.user.createdAt)
            }
            .padding({ left: 16, right: 16 })
            .backgroundColor(Color.White)
            .borderRadius(7)

            Button(this.signingOut ? '正在退出…' : '退出登录')
              .width('100%')
              .height(48)
              .fontSize(15)
              .fontColor('#B33B2E')
              .backgroundColor('#FBEDEA')
              .borderRadius(6)
              .enabled(!this.signingOut)
              .onClick(() => this.logout())
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
