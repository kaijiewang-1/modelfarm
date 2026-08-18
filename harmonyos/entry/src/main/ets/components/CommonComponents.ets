import { router } from '@kit.ArkUI'

@Component
export struct PageHeader {
  @Prop title: string = ''
  @Prop subtitle: string = ''
  @Prop showBack: boolean = true

  build() {
    Row() {
      if (this.showBack) {
        Button('‹')
          .fontSize(34)
          .fontColor('#17211B')
          .backgroundColor(Color.Transparent)
          .width(44)
          .height(44)
          .onClick(() => router.back())
      }
      Column({ space: 2 }) {
        Text(this.title)
          .fontSize(22)
          .fontWeight(FontWeight.Bold)
          .fontColor('#17211B')
        if (this.subtitle.length > 0) {
          Text(this.subtitle)
            .fontSize(12)
            .fontColor('#68736C')
        }
      }
      .alignItems(HorizontalAlign.Start)
      .layoutWeight(1)
    }
    .width('100%')
    .height(72)
    .padding({ left: this.showBack ? 4 : 20, right: 20 })
    .backgroundColor('#F4F7F2')
  }
}

@Component
export struct StatePanel {
  @Prop loading: boolean = false
  @Prop message: string = ''

  build() {
    Column({ space: 12 }) {
      if (this.loading) {
        LoadingProgress()
          .width(34)
          .height(34)
          .color('#176B43')
        Text('正在同步数据')
          .fontSize(14)
          .fontColor('#68736C')
      } else {
        Text('—')
          .fontSize(30)
          .fontColor('#B7C1BA')
        Text(this.message)
          .fontSize(14)
          .fontColor('#68736C')
      }
    }
    .width('100%')
    .height(180)
    .justifyContent(FlexAlign.Center)
  }
}

@Component
export struct StatusTag {
  @Prop text: string = ''
  @Prop positive: boolean = true
  @Prop warning: boolean = false

  build() {
    Text(this.text)
      .fontSize(11)
      .fontWeight(FontWeight.Medium)
      .fontColor(this.warning ? '#9B3F2C' : this.positive ? '#176B43' : '#68736C')
      .backgroundColor(this.warning ? '#FDEBE6' : this.positive ? '#E4F3E9' : '#EDF0EE')
      .padding({ left: 9, right: 9, top: 5, bottom: 5 })
      .borderRadius(4)
  }
}
