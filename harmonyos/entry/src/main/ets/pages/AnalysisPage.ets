import { PageHeader, StatePanel } from '../components/CommonComponents'
import { MonthInoutData } from '../models/Models'
import { FarmService } from '../services/FarmService'

@Entry
@Component
struct AnalysisPage {
  @State year: number = new Date().getFullYear()
  @State rows: MonthInoutData[] = []
  @State loading: boolean = true
  @State errorText: string = ''

  aboutToAppear(): void { this.load() }

  private async load(): Promise<void> {
    this.loading = true
    this.errorText = ''
    try {
      const data = await FarmService.getInstance().getYearData(this.year)
      this.rows = (data || []).slice().sort((a: MonthInoutData, b: MonthInoutData) => a.month - b.month)
    } catch (error) { this.errorText = error instanceof Error ? error.message : '统计加载失败' }
    finally { this.loading = false }
  }

  private barPercent(value: number): number {
    const maximum = Math.max(1, ...this.rows.map((item: MonthInoutData) => Math.max(item.inCount || 0, item.outCount || 0)))
    return Math.max(0, Math.min(100, (value || 0) / maximum * 100))
  }

  build() {
    Column({ space: 12 }) {
      PageHeader({ title: '数据分析', subtitle: '企业月度存栏与出入库统计' })
      Row({ space: 12 }) {
        Button('上一年').fontColor('#176B43').backgroundColor('#E0EFE6')
          .enabled(!this.loading && this.year > 2000).onClick(() => { this.year--; this.load() })
        Text(String(this.year)).fontSize(20).fontWeight(FontWeight.Bold).fontColor('#17211B')
        Button('下一年').fontColor('#176B43').backgroundColor('#E0EFE6')
          .enabled(!this.loading && this.year < new Date().getFullYear()).onClick(() => { this.year++; this.load() })
        Button('刷新').fontColor('#176B43').backgroundColor('#E0EFE6')
          .enabled(!this.loading).onClick(() => this.load())
      }
      if (this.loading || this.errorText.length > 0 || this.rows.length === 0) {
        StatePanel({ loading: this.loading, message: this.errorText || '本年度暂无统计数据' })
      } else {
        List({ space: 12 }) {
          ForEach(this.rows, (item: MonthInoutData) => {
            ListItem() {
              Column({ space: 8 }) {
                Row() {
                  Text(`${item.month} 月`).fontSize(16).fontWeight(FontWeight.Bold).fontColor('#17211B')
                  Blank()
                  Text(`存栏 ${item.sum === undefined ? '--' : item.sum}`).fontSize(13).fontColor('#536058')
                }.width('100%')
                Text(`入库 ${item.inCount || 0}`).fontSize(12).fontColor('#176B43').width('100%')
                Progress({ value: this.barPercent(item.inCount), total: 100, type: ProgressType.Linear }).color('#176B43').width('100%')
                Text(`出库 ${item.outCount || 0}`).fontSize(12).fontColor('#B27A19').width('100%')
                Progress({ value: this.barPercent(item.outCount), total: 100, type: ProgressType.Linear }).color('#B27A19').width('100%')
              }.width('100%').padding(16).backgroundColor(Color.White).borderRadius(8)
            }
          }, (item: MonthInoutData) => String(item.month))
        }.layoutWeight(1).width('100%').padding({ left: 18, right: 18, bottom: 18 })
      }
    }.width('100%').height('100%').backgroundColor('#F4F7F2')
  }
}
