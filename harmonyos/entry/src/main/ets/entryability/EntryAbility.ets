import { UIAbility } from '@kit.AbilityKit'
import { window } from '@kit.ArkUI'
import { AuthStore } from '../services/AuthStore'

export default class EntryAbility extends UIAbility {
  onWindowStageCreate(windowStage: window.WindowStage): void {
    AuthStore.getInstance().initialize(this.context)
      .then(() => windowStage.loadContent('pages/Index'))
      .catch(() => windowStage.loadContent('pages/Index'))
  }
}
