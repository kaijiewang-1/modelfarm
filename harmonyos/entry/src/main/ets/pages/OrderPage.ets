import { promptAction, router } from '@kit.ArkUI'
import { PageHeader, StatePanel, StatusTag } from '../components/CommonComponents'
import { OrderWithMedias, orderStatusText } from '../models/Models'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct OrderPage {
  @State orders: OrderWithMedias[] = []
  @State loading: boolean = true
  @State actionOrderId: number = 0
  @State errorText: string = ''
  private service: FarmService = FarmService.getInstance()

  onPageShow(): void {
    this.load()
  }

  private async load(): Promise<void> {
    this.loading = true
    this.errorText = ''
    try {
      this.orders = await this.service.getOrders()
    } catch (error) {
      this.errorText = error instanceof Error ? error.message : '工单加载失败'
    } finally {
      this.loading = false
    }
  }

  private async complete(orderId: number): Promise<void> {
    if (this.actionOrderId !== 0) { return }
    this.actionOrderId = orderId
    try {
      await this.service.completeOrder(orderId)
      promptAction.showToast({ message: '工单已完成' })
      await this.load()
    } catch (error) {
      promptAction.showToast({ message: error instanceof Error ? error.message : '操作失败' })
    } finally {
      this.actionOrderId = 0
    }
  }

  private async accept(orderId: number): Promise<void> {
    if (this.actionOrderId !== 0) { return }
    this.actionOrderId = orderId
    try {
      await this.service.acceptOrder(orderId)
      promptAction.showToast({ message: '工单已认领' })
      await this.load()
    } catch (error) {
      promptAction.showToast({ message: error instanceof Error ? error.message : '认领失败' })
    } finally { this.actionOrderId = 0 }
  }

  build() {
    Column() {
      PageHeader({ title: '工单管理', subtitle: '现场问题处理闭环' })
      Row({ space: 12 }) {
        Button('新建工单').height(36).fontColor(Color.White).backgroundColor('#176B43')
          .onClick(() => router.pushUrl({ url: 'pages/OrderCreatePage' }))
        Button('刷新').height(36).fontColor('#176B43').backgroundColor('#E0EFE6')
          .enabled(!this.loading && this.actionOrderId === 0).onClick(() => this.load())
      }.width('100%').padding({ left: 18, right: 18, bottom: 12 })
      if (this.loading || this.errorText.length > 0 || this.orders.length === 0) {
        StatePanel({ loading: this.loading, message: this.errorText || '暂无工单' })
      } else {
        List({ space: 10 }) {
          ForEach(this.orders, (item: OrderWithMedias) => {
            ListItem() {
              Column({ space: 12 }) {
                Row() {
                  Text(`#${item.order.id}`)
                    .fontSize(11)
                    .fontColor('#7D8780')
                  Blank()
                  StatusTag({
                    text: orderStatusText(item.order.status),
                    positive: String(item.order.status) === '2',
                    warning: String(item.order.status) === '3'
                  })
                }
                .width('100%')
                Text(item.order.title)
                  .width('100%')
                  .fontSize(17)
                  .fontWeight(FontWeight.Bold)
                  .fontColor('#17211B')
                  .maxLines(2)
                Text(item.order.description || '暂无问题描述')
                  .width('100%')
                  .fontSize(13)
                  .fontColor('#68736C')
                Row() {
                  Text(item.order.createdAt)
                    .fontSize(11)
                    .fontColor('#8A938D')
                  Blank()
                  if (String(item.order.status) !== '2') {
                    if (!item.order.acceptedId && Number(item.order.isAccepted) !== 1) {
                      Button('认领').height(34).fontSize(12).fontColor('#176B43').backgroundColor('#E0EFE6')
                        .enabled(this.actionOrderId === 0).onClick(() => this.accept(item.order.id))
                    }
                    Button(this.actionOrderId === item.order.id ? '处理中…' : '标记完成')
                      .height(34)
                      .fontSize(12)
                      .fontColor(Color.White)
                      .backgroundColor('#176B43')
                      .borderRadius(5)
                      .enabled(this.actionOrderId === 0)
                      .onClick(() => this.complete(item.order.id))
                  }
                }
                .width('100%')
              }
              .width('100%')
              .padding(16)
              .backgroundColor(Color.White)
              .borderRadius(7)
            }
          }, (item: OrderWithMedias) => String(item.order.id))
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
