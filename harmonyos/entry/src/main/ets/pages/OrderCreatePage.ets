import { promptAction, router } from '@kit.ArkUI'
import { PageHeader } from '../components/CommonComponents'
import { CreateOrderRequest } from '../models/Models'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct OrderCreatePage {
  @State title: string = ''
  @State description: string = ''
  @State urgent: boolean = false
  @State saving: boolean = false
  @State errorText: string = ''

  private async save(): Promise<void> {
    if (this.saving) { return }
    if (this.title.trim().length === 0 || this.description.trim().length === 0) {
      this.errorText = '请填写工单标题和问题描述'
      return
    }
    this.saving = true
    this.errorText = ''
    try {
      const request: CreateOrderRequest = {
        title: this.title.trim(), description: this.description.trim(), status: this.urgent ? 3 : 1
      }
      await FarmService.getInstance().createOrder(request)
      promptAction.showToast({ message: '工单创建成功' })
      router.back()
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '创建工单失败'
    } finally { this.saving = false }
  }

  build() {
    Column() {
      PageHeader({ title: '新建工单', subtitle: '记录现场问题，跟进处理结果' })
      Scroll() {
        Column({ space: 18 }) {
          TextInput({ text: this.title, placeholder: '工单标题（必填）' }).maxLength(100)
            .onChange((value: string) => this.title = value)
          TextArea({ text: this.description, placeholder: '详细描述现场问题（必填）' }).height(180).maxLength(2000)
            .onChange((value: string) => this.description = value)
          Row() {
            Text('紧急工单').layoutWeight(1).fontColor('#17211B')
            Toggle({ type: ToggleType.Switch, isOn: this.urgent }).selectedColor('#176B43')
              .onChange((value: boolean) => this.urgent = value)
          }.width('100%')
          if (this.errorText.length > 0) {
            Text(this.errorText).fontSize(13).fontColor('#B33B2E').width('100%')
          }
          Button(this.saving ? '正在提交…' : '创建工单').width('100%').height(48)
            .backgroundColor('#176B43').fontColor(Color.White).enabled(!this.saving)
            .onClick(() => this.save())
        }.width('100%').padding(20)
      }.layoutWeight(1)
    }.width('100%').height('100%').backgroundColor('#F4F7F2')
  }
}
