# ModelFarm HarmonyOS 迁移工程

该目录是 Android `modelfarm` 的 HarmonyOS Stage 模型迁移版本，使用 ArkTS + ArkUI，目标为 HarmonyOS 5.0 / API 12。它与原 Android 工程相互独立，可以直接用 DevEco Studio 打开 `harmonyos` 目录。

## 已迁移功能

- 登录、游客体验、输入校验和 Sa-Token 会话保存
- 首页企业统计、30 秒自动刷新和手动刷新
- 自动遍历环境传感器并读取第一条有效最新数据
- 养殖场搜索与列表
- 设备类型筛选、在线/离线/故障状态
- 工单列表与“标记完成”操作
- 六项环境指标与传感器切换
- 企业信息、个人资料、管理员权限检查与成员列表
- 服务端登出失败时仍清理本地会话
- 加载、空数据、错误和无权限状态

## 工程结构

```text
harmonyos/
├─ AppScope/                         应用级配置与图标
├─ entry/src/main/
│  ├─ ets/entryability/             Stage UIAbility 入口
│  ├─ ets/models/                   API 数据模型
│  ├─ ets/services/                 HTTP、会话、环境字段解析
│  ├─ ets/components/               公共标题栏与状态组件
│  ├─ ets/pages/                    ArkUI 页面
│  ├─ resources/base/element/       颜色与文案资源
│  └─ module.json5                  模块、设备与网络权限
├─ build-profile.json5
└─ oh-package.json5
```

## Android 与鸿蒙代码映射

| Android | HarmonyOS |
| --- | --- |
| `MainActivity.kt` | `pages/Index.ets` |
| `login.java` + `activity_login.xml` | `pages/LoginPage.ets` |
| `DashboardActivity.java` + XML | `pages/HomePage.ets` |
| `RetrofitClient.kt` | `services/ApiClient.ets` |
| `AuthManager.kt` / SharedPreferences | `services/AuthStore.ets` / Preferences |
| Repository + ApiService | `services/FarmService.ets` |
| `DeviceDataParser.java` | `services/EnvironmentParser.ets` |
| 各 Activity + Adapter | 对应 ArkUI 页面中的 `List` / `ForEach` |

## 在 DevEco Studio 中运行

1. 安装 DevEco Studio，并安装 HarmonyOS 5.0.0 / API 12 SDK。
2. 选择 **Open Project**，打开本目录 `harmonyos`。
3. 等待 Hvigor 和 ohpm 同步；若 DevEco 提示升级 Hvigor，接受 IDE 推荐的同一版本组合。
4. 在 **Project Structure > Signing Configs** 中启用自动签名。
5. 选择 phone 或 tablet 模拟器/真机，运行 `entry` 模块。

首次导入时 DevEco 会生成本机相关文件，这些文件不应复制回 Android 工程。

## 后端配置

当前迁移版为保持与 Android 行为一致，使用：

```text
http://124.71.97.178:8049
```

配置位置：`entry/src/main/ets/services/ApiConfig.ets`。

生产发布前必须换成有效的 HTTPS 域名与证书。不要在代码中保存 API 密钥、固定用户令牌或生产账号。请求统一携带后端现有的 `satoken` 请求头，密码和令牌不会写入日志。

当前版本为了和 Android 的 SharedPreferences 行为对齐，令牌保存在 HarmonyOS Preferences。正式发布时建议将令牌迁移到 HUKS/Asset Store 等系统安全存储，并在服务端配置合理的令牌有效期与撤销机制。

### 已验证的后端路由

当前部署 http://124.71.97.178:8049 的业务路由实际不带 /api 前缀，例如 /user/login、/enterprise/count、/farmSite/camera 和 /live/ticket。视频接口文档中的 /api/farmSite/camera、/api/live/detection-session 与当前部署不一致，直接访问会返回 No static resource；鸿蒙端已按实测可用路由接入，并将路径集中在服务层，后端升级后只需调整对应服务方法。

### 构建与接口检查

在安装 DevEco Studio 和 HarmonyOS API 12 SDK 后执行：

    $env:DEVECO_SDK_HOME = 'C:\Program Files\Huawei\DevEco Studio\sdk'
    & 'C:\Program Files\Huawei\DevEco Studio\tools\hvigor\bin\hvigorw.bat' --mode module -p product=default assembleHap

检查真实后端链路时，不要把账号密码写入仓库：

    $env:MODEL_FARM_TEST_PHONE = 'your-test-phone'
    $env:MODEL_FARM_TEST_PASSWORD = 'your-test-password'
    ./scripts/check-backend.ps1

脚本会验证登录、企业统计、农场、地块、摄像头、/live/ticket 和登出，并只输出业务摘要，不输出 satoken。
## 后续接入建议

- 先用现有后端完成登录、首页、设备、工单四条主链路的真机联调。
- 确认后端返回字段后，为 `ApiClient` 增加更严格的运行时 JSON 校验。
- 继续迁移养殖点详情、设备创建、摄像头直播、工单附件上传等二级流程。
- pages/LivePage.ets 已接入摄像头列表和智慧直播票据初始化，展示 streamId、HTTP-FLV 和 WebSocket 地址。HTTP-FLV 播放与检测框 Canvas 仍需按目标手机可用的媒体组件（AVPlayer/自研 FLV 解码器）落地，当前实测流媒体地址连接超时。
- 将中文文案逐步移入资源文件，补充深色模式与多语言资源。

## 上线前检查

- API 全部切换到 HTTPS。
- DevEco 编译无 ArkTS lint 错误。
- 手机与平板分别检查横竖屏布局。
- 验证登录失效、弱网、空列表、无管理员权限和服务端 5xx。
- 验证应用切后台后首页刷新定时器已停止，返回页面后重新启动。
- 在真机确认 Preferences 中的会话可跨进程启动恢复并可彻底退出。
