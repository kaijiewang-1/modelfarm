import { promptAction, router } from '@kit.ArkUI'
import { LoginResponse } from '../models/Models'
import { AuthStore } from '../services/AuthStore'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct LoginPage {
  @State account: string = ''
  @State password: string = ''
  @State passwordVisible: boolean = false
  @State loading: boolean = false
  @State errorText: string = ''
  private service: FarmService = FarmService.getInstance()

  private isValidAccount(value: string): boolean {
    const phonePattern: RegExp = /^1[3-9]\d{9}$/
    const usernamePattern: RegExp = /^[a-zA-Z0-9_]{3,20}$/
    return phonePattern.test(value) || usernamePattern.test(value)
  }

  private async submit(account: string = this.account, password: string = this.password): Promise<void> {
    if (this.loading) { return }
    const normalizedAccount: string = account.trim()
    if (!this.isValidAccount(normalizedAccount) || password.length < 6 || password.length > 64) {
      this.errorText = '请输入有效的手机号或用户名，密码至少 6 位'
      return
    }
    this.loading = true
    this.errorText = ''
    try {
      const response: LoginResponse = await this.service.login({
        phone: normalizedAccount,
        password: password
      })
      if (!response.satoken || response.satoken.tokenValue.length === 0) {
        throw new Error('Invalid session')
      }
      await AuthStore.getInstance().saveSession(response)
      this.password = ''
      router.clear()
      promptAction.showToast({ message: '登录成功' })
      const target: string = response.enterpriseId > 0 ? 'pages/HomePage' : 'pages/CompanySetupPage'
      router.replaceUrl({ url: target })
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '登录失败，请稍后重试'
    } finally {
      this.loading = false
    }
  }

  build() {
    Scroll() {
      Column() {
        Row() {
          Image($r('app.media.app_icon'))
            .width(48)
            .height(48)
            .borderRadius(12)
          Column({ space: 2 }) {
            Text('MODEL FARM')
              .fontSize(11)
              .fontWeight(FontWeight.Bold)
              .fontColor('#176B43')
            Text('数智化养殖')
              .fontSize(19)
              .fontWeight(FontWeight.Bold)
              .fontColor('#17211B')
          }
          .alignItems(HorizontalAlign.Start)
          .margin({ left: 12 })
        }
        .width('100%')

        Column({ space: 8 }) {
          Text('让每一次巡检\n都有数据可循')
            .fontSize(34)
            .fontWeight(FontWeight.Bold)
            .fontColor('#17211B')
            .lineHeight(45)
          Text('连接养殖场、环境传感器与设备工单')
            .fontSize(14)
            .fontColor('#68736C')
        }
        .width('100%')
        .alignItems(HorizontalAlign.Start)
        .margin({ top: 42, bottom: 32 })

        Column({ space: 14 }) {
          Text('登录工作台')
            .width('100%')
            .fontSize(20)
            .fontWeight(FontWeight.Bold)
            .fontColor('#17211B')

          TextInput({ text: this.account, placeholder: '手机号或用户名' })
            .height(52)
            .fontSize(15)
            .backgroundColor('#F5F7F5')
            .borderRadius(6)
            .onChange((value: string) => this.account = value)

          TextInput({ text: this.password, placeholder: '密码' })
            .type(this.passwordVisible ? InputType.Normal : InputType.Password)
            .height(52)
            .fontSize(15)
            .backgroundColor('#F5F7F5')
            .borderRadius(6)
            .showPasswordIcon(true)
            .onChange((value: string) => this.password = value)

          if (this.errorText.length > 0) {
            Text(this.errorText)
              .width('100%')
              .fontSize(12)
              .fontColor('#B33B2E')
          }

          Button(this.loading ? '登录中…' : '登录')
            .width('100%')
            .height(52)
            .fontSize(16)
            .fontWeight(FontWeight.Bold)
            .fontColor(Color.White)
            .backgroundColor('#176B43')
            .borderRadius(6)
            .enabled(!this.loading)
            .onClick(() => this.submit())

          Button('注册账号')
            .width('100%')
            .height(40)
            .fontSize(13)
            .fontColor('#536058')
            .backgroundColor(Color.Transparent)
            .onClick(() => router.pushUrl({ url: 'pages/RegisterPage' }))
        }
        .width('100%')
        .padding(22)
        .backgroundColor(Color.White)
        .borderRadius(8)
        .shadow({ radius: 24, color: '#16000000', offsetY: 8 })

        Text('登录即表示你同意平台的数据与隐私规范')
          .fontSize(11)
          .fontColor('#89918C')
          .margin({ top: 24, bottom: 24 })
      }
      .width('100%')
      .height('100%')
      .padding({ left: 24, right: 24, top: 28 })
    }
    .width('100%')
    .height('100%')
    .backgroundColor('#F4F7F2')
  }
}
