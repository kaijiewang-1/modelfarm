import { promptAction, router } from '@kit.ArkUI'
import { PageHeader } from '../components/CommonComponents'
import { LoginResponse } from '../models/Models'
import { AuthStore } from '../services/AuthStore'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct RegisterPage {
  @State username: string = ''
  @State phone: string = ''
  @State password: string = ''
  @State confirmPassword: string = ''
  @State loading: boolean = false
  @State errorText: string = ''
  private service: FarmService = FarmService.getInstance()

  private validate(): boolean {
    if (!/^[a-zA-Z0-9_]{3,20}$/.test(this.username.trim())) {
      this.errorText = '用户名需为 3-20 位字母、数字或下划线'
      return false
    }
    if (!/^1[3-9]\d{9}$/.test(this.phone.trim())) {
      this.errorText = '请输入有效的中国大陆手机号'
      return false
    }
    if (this.password.length < 6 || this.password.length > 64 || this.password !== this.confirmPassword) {
      this.errorText = '密码需为 6-64 位，且两次输入必须一致'
      return false
    }
    return true
  }

  private async submit(): Promise<void> {
    if (this.loading) { return }
    if (!this.validate()) {
      return
    }
    this.loading = true
    this.errorText = ''
    try {
      const response: LoginResponse = await this.service.register({
        username: this.username.trim(),
        phone: this.phone.trim(),
        password: this.password
      })
      await AuthStore.getInstance().saveSession(response)
      this.password = ''
      this.confirmPassword = ''
      router.clear()
      promptAction.showToast({ message: '注册成功' })
      const target: string = response.enterpriseId > 0 ? 'pages/HomePage' : 'pages/CompanySetupPage'
      router.replaceUrl({ url: target })
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '注册失败，请稍后重试'
    } finally {
      this.loading = false
    }
  }

  build() {
    Column() {
      PageHeader({ title: '注册账号', subtitle: '创建你的养殖平台身份' })
      Scroll() {
        Column({ space: 14 }) {
          TextInput({ text: this.username, placeholder: '用户名' })
            .height(50).backgroundColor(Color.White).borderRadius(6)
            .onChange((value: string) => this.username = value)
          TextInput({ text: this.phone, placeholder: '手机号' })
            .type(InputType.PhoneNumber)
            .height(50).backgroundColor(Color.White).borderRadius(6)
            .onChange((value: string) => this.phone = value)
          TextInput({ text: this.password, placeholder: '密码' })
            .type(InputType.Password).showPasswordIcon(true)
            .height(50).backgroundColor(Color.White).borderRadius(6)
            .onChange((value: string) => this.password = value)
          TextInput({ text: this.confirmPassword, placeholder: '确认密码' })
            .type(InputType.Password).showPasswordIcon(true)
            .height(50).backgroundColor(Color.White).borderRadius(6)
            .onChange((value: string) => this.confirmPassword = value)
          if (this.errorText.length > 0) {
            Text(this.errorText).width('100%').fontSize(12).fontColor('#B33B2E')
          }
          Button(this.loading ? '提交中…' : '注册并继续')
            .width('100%').height(50).fontColor(Color.White)
            .fontWeight(FontWeight.Bold).backgroundColor('#176B43').borderRadius(6)
            .enabled(!this.loading).onClick(() => this.submit())
        }
        .width('100%')
        .padding({ left: 20, right: 20, top: 12, bottom: 24 })
      }
      .layoutWeight(1)
    }
    .width('100%').height('100%').backgroundColor('#F4F7F2')
  }
}
