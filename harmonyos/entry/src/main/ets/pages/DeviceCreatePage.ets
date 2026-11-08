import { promptAction, router } from '@kit.ArkUI'
import { PageHeader, StatePanel } from '../components/CommonComponents'
import { Farm, FarmSite, PageParams, CreateDeviceRequest } from '../models/Models'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct DeviceCreatePage {
  @State farms: Farm[] = []
  @State sites: FarmSite[] = []
  @State siteId: number = 0
  @State siteName: string = ''
  @State name: string = ''
  @State mac: string = ''
  @State deviceType: number = 2
  @State pushName: string = ''
  @State loading: boolean = false
  @State saving: boolean = false
  @State errorText: string = ''
  @State selectedFarmId: number = 0
  private fixedSite: boolean = false
  private service: FarmService = FarmService.getInstance()

  aboutToAppear(): void {
    const params = router.getParams() as PageParams
    this.siteId = params?.siteId || 0
    this.siteName = params?.siteName || ''
    this.fixedSite = this.siteId > 0
    if (!this.fixedSite) { this.loadFarms() }
  }

  private async loadFarms(): Promise<void> {
    this.loading = true
    this.errorText = ''
    try {
      this.farms = await this.service.getFarms() || []
      if (this.farms.length > 0) { await this.loadSites(this.farms[0].id) }
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '养殖场加载失败'
    } finally { this.loading = false }
  }

  private async loadSites(farmId: number): Promise<void> {
    this.selectedFarmId = farmId
    this.siteId = 0
    this.sites = []
    this.loading = true
    this.errorText = ''
    try {
      const sites = await this.service.getFarmSites(farmId)
      if (this.selectedFarmId === farmId) {
        this.sites = sites || []
        this.siteId = this.sites.length > 0 ? this.sites[0].id : 0
      }
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '养殖点加载失败'
    } finally { if (this.selectedFarmId === farmId) { this.loading = false } }
  }

  private async save(): Promise<void> {
    if (this.saving || this.loading) { return }
    if (this.name.trim().length === 0 || this.siteId <= 0 || this.mac.trim().length === 0) {
      this.errorText = '请填写设备名称、MAC，并选择所属养殖点'
      return
    }
    if (this.deviceType === 1 && !/^rtmps?:\/\/[^\s/?#]+\/[^\s?#]+\/[^\s?#]+$/.test(this.pushName.trim())) {
      this.errorText = '摄像头推流名称须为完整 RTMP/RTMPS 地址'
      return
    }
    this.saving = true
    this.errorText = ''
    try {
      const body: CreateDeviceRequest = {
        name: this.name.trim(), siteId: this.siteId, mac: this.mac.trim(), type: this.deviceType,
        pushName: this.deviceType === 1 ? this.pushName.trim() : undefined
      }
      await this.service.createDevice(body)
      promptAction.showToast({ message: '设备添加成功' })
      router.back()
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '添加失败，请重试'
    } finally { this.saving = false }
  }

  build() {
    Column() {
      PageHeader({ title: '添加设备', subtitle: this.siteName || '选择养殖场和养殖点' })
      Scroll() {
        Column({ space: 16 }) {
          if (!this.fixedSite) {
            Text('所属养殖场').width('100%').fontColor('#68736C')
            Select(this.farms.map((farm: Farm): SelectOption => { return { value: farm.name } }))
              .value(this.farms.find((farm: Farm) => farm.id === this.selectedFarmId)?.name || '请选择')
              .width('100%').enabled(!this.saving)
              .onSelect((index: number) => this.loadSites(this.farms[index].id))
            Text('所属养殖点').width('100%').fontColor('#68736C')
            Select(this.sites.map((site: FarmSite): SelectOption => { return { value: site.name } }))
              .value(this.sites.find((site: FarmSite) => site.id === this.siteId)?.name || '暂无养殖点')
              .width('100%').enabled(!this.loading && !this.saving)
              .onSelect((index: number) => this.siteId = this.sites[index].id)
            if (this.loading) { LoadingProgress().width(28).height(28) }
          }
          TextInput({ text: this.name, placeholder: '设备名称（必填）' }).maxLength(100)
            .onChange((value: string) => this.name = value)
          TextInput({ text: this.mac, placeholder: 'MAC / 设备标识（必填）' }).maxLength(100)
            .onChange((value: string) => this.mac = value)
          Select([{ value: '摄像头' }, { value: '传感器' }, { value: '控制器' }])
            .selected(this.deviceType - 1).value(this.deviceType === 1 ? '摄像头' : this.deviceType === 2 ? '传感器' : '控制器')
            .width('100%').onSelect((index: number) => this.deviceType = index + 1)
          if (this.deviceType === 1) {
            TextInput({ text: this.pushName, placeholder: 'rtmp://主机/live/摄像头名称' }).maxLength(2048)
              .onChange((value: string) => this.pushName = value)
          }
          if (this.errorText.length > 0) {
            Text(this.errorText).fontSize(13).fontColor('#B33B2E').width('100%')
          }
          Button(this.saving ? '正在添加…' : '保存设备').width('100%').height(48)
            .fontColor(Color.White).backgroundColor('#176B43').enabled(!this.saving && !this.loading && this.siteId > 0)
            .onClick(() => this.save())
          if (!this.fixedSite) {
            Button('重新加载养殖点').backgroundColor('#E0EFE6').fontColor('#176B43')
              .enabled(!this.loading && !this.saving).onClick(() => this.loadFarms())
          }
        }.width('100%').padding(20)
      }.layoutWeight(1)
    }.width('100%').height('100%').backgroundColor('#F4F7F2')
  }
}
