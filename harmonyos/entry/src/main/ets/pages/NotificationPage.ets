import { promptAction } from '@kit.ArkUI'
import { PageHeader, StatePanel, StatusTag } from '../components/CommonComponents'
import { FarmNotification } from '../models/Models'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct NotificationPage {
  @State notifications: FarmNotification[] = []
  @State loading: boolean = true
  @State acting: boolean = false
  @State errorText: string = ''
  private service: FarmService = FarmService.getInstance()

  aboutToAppear(): void { this.load() }

  private async load(): Promise<void> {
    this.loading = true
    this.errorText = ''
    try { this.notifications = await this.service.getNotifications() || [] }
    catch (error) { this.errorText = error instanceof Error ? error.message : '消息加载失败' }
    finally { this.loading = false }
  }

  private async read(id: number = 0): Promise<void> {
    if (this.acting) { return }
    this.acting = true
    try {
      if (id > 0) { await this.service.readNotification(id) }
      else { await this.service.readAllNotifications() }
      await this.load()
    } catch (error) {
      promptAction.showToast({ message: error instanceof Error ? error.message : '标记失败' })
    } finally { this.acting = false }
  }

  build() {
    Column({ space: 10 }) {
      PageHeader({ title: '消息通知', subtitle: `${this.notifications.filter((item: FarmNotification) => Number(item.isRead) !== 1).length} 条未读` })
      Row({ space: 12 }) {
        Button('刷新').fontColor('#176B43').backgroundColor('#E0EFE6')
          .enabled(!this.loading && !this.acting).onClick(() => this.load())
        Button('全部已读').fontColor(Color.White).backgroundColor('#176B43')
          .enabled(!this.loading && !this.acting && this.notifications.length > 0).onClick(() => this.read())
      }
      if (this.loading || this.errorText.length > 0 || this.notifications.length === 0) {
        StatePanel({ loading: this.loading, message: this.errorText || '暂无消息' })
      } else {
        List({ space: 12 }) {
          ForEach(this.notifications, (item: FarmNotification) => {
            ListItem() {
              Column({ space: 10 }) {
                Row() {
                  Text(item.title).layoutWeight(1).fontSize(16).fontWeight(FontWeight.Bold).fontColor('#17211B')
                  StatusTag({ text: Number(item.isRead) === 1 ? '已读' : '未读', positive: Number(item.isRead) !== 1, warning: item.type === 2 })
                }.width('100%')
                Text(item.content).width('100%').fontSize(14).fontColor('#536058')
                Text(item.createdAt).width('100%').fontSize(11).fontColor('#68736C')
                if (Number(item.isRead) !== 1) {
                  Button('标记已读').height(34).fontSize(12).fontColor('#176B43').backgroundColor('#E0EFE6')
                    .enabled(!this.acting).onClick(() => this.read(item.id))
                }
              }.width('100%').padding(16).backgroundColor(Color.White).borderRadius(8)
            }
          }, (item: FarmNotification) => String(item.id))
        }.width('100%').layoutWeight(1).padding({ left: 18, right: 18, bottom: 18 })
      }
    }.width('100%').height('100%').backgroundColor('#F4F7F2')
  }
}
