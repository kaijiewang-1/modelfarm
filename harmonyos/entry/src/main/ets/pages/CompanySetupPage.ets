import { promptAction, router } from '@kit.ArkUI'
import { AuthStore } from '../services/AuthStore'
import { FarmService } from '../services/FarmService'

type SetupMode = 'join' | 'create'

@Entry
@Component
struct CompanySetupPage {
  @State mode: SetupMode = 'join'
  @State invitedCode: string = ''
  @State name: string = ''
  @State address: string = ''
  @State contactPerson: string = ''
  @State contactPhone: string = ''
  @State loading: boolean = false
  @State errorText: string = ''
  private service: FarmService = FarmService.getInstance()

  private async join(): Promise<void> {
    const code: string = this.invitedCode.trim()
    if (!/^[a-zA-Z0-9_-]{4,32}$/.test(code)) {
      this.errorText = '请输入有效的企业邀请码'
      return
    }
    this.loading = true
    this.errorText = ''
    try {
      await this.service.joinEnterprise(code)
      await AuthStore.getInstance().clearSession()
      promptAction.showToast({ message: '加入成功，请重新登录' })
      router.replaceUrl({ url: 'pages/LoginPage' })
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '加入企业失败'
    } finally {
      this.loading = false
    }
  }

  private async create(): Promise<void> {
    const phonePattern: RegExp = /^1[3-9]\d{9}$/
    if (this.name.trim().length < 2 || this.address.trim().length < 2 ||
      this.contactPerson.trim().length < 2 || !phonePattern.test(this.contactPhone.trim())) {
      this.errorText = '请完整填写企业名称、地址、联系人和有效手机号'
      return
    }
    this.loading = true
    this.errorText = ''
    try {
      const enterpriseId: number = await this.service.createEnterprise(
        this.name.trim(), this.address.trim(), this.contactPerson.trim(), this.contactPhone.trim())
      await AuthStore.getInstance().updateEnterpriseId(enterpriseId)
      promptAction.showToast({ message: '企业创建成功' })
      router.replaceUrl({ url: 'pages/HomePage' })
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '创建企业失败'
    } finally {
      this.loading = false
    }
  }

  @Builder
  private modeButton(label: string, mode: SetupMode) {
    Button(label)
      .layoutWeight(1).height(42).fontSize(14)
      .fontColor(this.mode === mode ? Color.White : '#536058')
      .backgroundColor(this.mode === mode ? '#176B43' : Color.Transparent)
      .borderRadius(5)
      .onClick(() => {
        this.mode = mode
        this.errorText = ''
      })
  }

  build() {
    Scroll() {
      Column({ space: 20 }) {
        Column({ space: 6 }) {
          Text('加入你的养殖组织')
            .fontSize(28).fontWeight(FontWeight.Bold).fontColor('#17211B')
          Text('账号需要关联企业后才能使用业务工作台')
            .fontSize(13).fontColor('#68736C')
        }
        .width('100%').alignItems(HorizontalAlign.Start)

        Row({ space: 6 }) {
          this.modeButton('加入企业', 'join')
          this.modeButton('创建企业', 'create')
        }
        .width('100%').padding(4).backgroundColor('#E7ECE8').borderRadius(7)

        Column({ space: 13 }) {
          if (this.mode === 'join') {
            TextInput({ text: this.invitedCode, placeholder: '企业邀请码' })
              .height(50).backgroundColor('#F5F7F5').borderRadius(6)
              .onChange((value: string) => this.invitedCode = value)
          } else {
            TextInput({ text: this.name, placeholder: '企业名称' })
              .height(50).backgroundColor('#F5F7F5').borderRadius(6)
              .onChange((value: string) => this.name = value)
            TextInput({ text: this.address, placeholder: '企业地址' })
              .height(50).backgroundColor('#F5F7F5').borderRadius(6)
              .onChange((value: string) => this.address = value)
            TextInput({ text: this.contactPerson, placeholder: '联系人' })
              .height(50).backgroundColor('#F5F7F5').borderRadius(6)
              .onChange((value: string) => this.contactPerson = value)
            TextInput({ text: this.contactPhone, placeholder: '联系电话' })
              .type(InputType.PhoneNumber).height(50).backgroundColor('#F5F7F5').borderRadius(6)
              .onChange((value: string) => this.contactPhone = value)
          }
          if (this.errorText.length > 0) {
            Text(this.errorText).width('100%').fontSize(12).fontColor('#B33B2E')
          }
          Button(this.loading ? '提交中…' : this.mode === 'join' ? '加入并重新登录' : '创建并进入工作台')
            .width('100%').height(50).fontColor(Color.White).fontWeight(FontWeight.Bold)
            .backgroundColor('#176B43').borderRadius(6).enabled(!this.loading)
            .onClick(() => this.mode === 'join' ? this.join() : this.create())
        }
        .width('100%').padding(20).backgroundColor(Color.White).borderRadius(8)

        Button('退出当前账号')
          .fontSize(13).fontColor('#68736C').backgroundColor(Color.Transparent)
          .onClick(async () => {
            await AuthStore.getInstance().clearSession()
            router.replaceUrl({ url: 'pages/LoginPage' })
          })
      }
      .width('100%').height('100%').padding({ left: 22, right: 22, top: 48, bottom: 24 })
    }
    .width('100%').height('100%').backgroundColor('#F4F7F2')
  }
}
