import { router } from '@kit.ArkUI'
import { AuthStore } from '../services/AuthStore'

@Entry
@Component
struct Index {
  aboutToAppear(): void {
    const auth: AuthStore = AuthStore.getInstance()
    let target: string = 'pages/LoginPage'
    if (auth.isLoggedIn()) {
      target = auth.enterpriseId > 0 ? 'pages/HomePage' : 'pages/CompanySetupPage'
    }
    router.replaceUrl({ url: target })
  }

  build() {
    Column({ space: 14 }) {
      Image($r('app.media.app_icon'))
        .width(78)
        .height(78)
        .borderRadius(18)
      Text('数智化养殖')
        .fontSize(22)
        .fontWeight(FontWeight.Bold)
        .fontColor('#17211B')
      LoadingProgress()
        .width(28)
        .height(28)
        .color('#176B43')
    }
    .width('100%')
    .height('100%')
    .justifyContent(FlexAlign.Center)
    .backgroundColor('#F4F7F2')
  }
}
