import { promptAction, router } from '@kit.ArkUI'
import { PageHeader } from '../components/CommonComponents'
import { PageParams, CreateFarmRequest, CreateSiteRequest } from '../models/Models'
import { AuthStore } from '../services/AuthStore'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct LocationCreatePage {
  @State farmId: number = 0
  @State farmName: string = ''
  @State name: string = ''
  @State address: string = ''
  @State supervisorId: string = ''
  @State sum: string = '0'
  @State saving: boolean = false
  @State errorText: string = ''

  aboutToAppear(): void {
    const params = router.getParams() as PageParams
    this.farmId = params?.farmId || 0
    this.farmName = params?.farmName || ''
    this.supervisorId = String(AuthStore.getInstance().userId)
  }

  private async save(): Promise<void> {
    if (this.saving) { return }
    this.errorText = ''
    if (this.name.trim().length === 0) { this.errorText = '请填写名称'; return }
    if (this.farmId > 0 && (!/^\d{1,9}$/.test(this.sum) || Number(this.sum) > 100000000)) {
      this.errorText = '存栏数量须为 0 到 100000000 的整数'; return
    }
    if (this.farmId === 0 && (this.address.trim().length === 0 || !/^[1-9]\d*$/.test(this.supervisorId))) {
      this.errorText = '请填写地址和有效的负责人用户编号'; return
    }
    this.saving = true
    try {
      const service: FarmService = FarmService.getInstance()
      if (this.farmId > 0) {
        const site: CreateSiteRequest = { farmId: this.farmId, name: this.name.trim(), sum: Number(this.sum) }
        await service.createSite(site)
      } else {
        const farm: CreateFarmRequest = { name: this.name.trim(), address: this.address.trim(), supervisorId: this.supervisorId }
        await service.createFarm(farm)
      }
      promptAction.showToast({ message: '创建成功' })
      router.back()
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '创建失败'
    } finally { this.saving = false }
  }

  build() {
    Column() {
      PageHeader({ title: this.farmId > 0 ? '新建养殖点' : '新建养殖场', subtitle: this.farmName || '登记场区基本信息' })
      Scroll() {
        Column({ space: 16 }) {
          TextInput({ text: this.name, placeholder: '名称（必填）' }).maxLength(100)
            .onChange((value: string) => this.name = value)
          if (this.farmId > 0) {
            Text('存栏数量').width('100%').fontColor('#68736C')
            TextInput({ text: this.sum, placeholder: '存栏数量' }).type(InputType.Number).maxLength(9)
              .onChange((value: string) => this.sum = value)
          } else {
            TextInput({ text: this.address, placeholder: '地址（必填）' }).maxLength(200)
              .onChange((value: string) => this.address = value)
            Text('负责人用户编号（默认为当前用户）').width('100%').fontSize(13).fontColor('#68736C')
            TextInput({ text: this.supervisorId, placeholder: '负责人用户编号' }).type(InputType.Number).maxLength(12)
              .onChange((value: string) => this.supervisorId = value)
          }
          if (this.errorText.length > 0) {
            Text(this.errorText).width('100%').fontSize(13).fontColor('#B33B2E')
          }
          Button(this.saving ? '正在保存…' : '创建').width('100%').height(48)
            .backgroundColor('#176B43').fontColor(Color.White).enabled(!this.saving).onClick(() => this.save())
        }.width('100%').padding(20)
      }.layoutWeight(1)
    }.width('100%').height('100%').backgroundColor('#F4F7F2')
  }
}
