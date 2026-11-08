# 鸿蒙前端与后端接口联调计划

## 2026-11-08 初版迁移补全
- [in_progress] 对照 Android 补齐养殖场/养殖点/设备/工单基本业务闭环。
- [pending] 同步六项环境指标，修复会话失效、刷新生命周期与视频入口。
- [pending] 使用本机 DevEco SDK 完整编译，检查路由和纯业务逻辑。
- [pending] 更新迁移范围、运行步骤、产物与尚未真机验证的限制。
- 保留仓库中所有已有 Android 与 HarmonyOS 修改，不提交或覆盖用户工作。
- 构建首次遇到 EPERM（Hvigor 旧日志写入），已请求沙箱外本机构建。

目标：确认 HarmonyOS 前端可构建，按 STBackendV2.md 与原画和智慧直播接口.md 对齐接口，实现稳定通信，并完成可在鸿蒙手机运行的初版交付。

## 阶段

- [complete] 1. 工程与接口文档盘点
- [complete] 2. 首次构建与问题定位
- [complete] 3. 前端接口层/配置修复与结构整理
- [complete] 4. 后端连通性与契约验证
- [complete] 5. 鸿蒙打包与运行前检查

## 交付结论

- Hvigor 使用 DevEco Studio 自带 SDK 根路径构建成功。
- HAP 已生成，但当前工程未配置 signingConfigs，产物为 unsigned HAP，需在 DevEco Studio 开启自动签名后安装真机。
- 普通业务接口实际不带 /api 前缀；已按实测路由接入。
- 视频中心已接入摄像头列表和 /live/ticket 票据流程；HTTP-FLV 播放与 WebSocket 检测框仍需设备媒体组件落地。

## 遇到的错误

| 错误 | 尝试次数 | 处理 |
|---|---:|---|
| Windows 沙箱初始化失败（只读扫描） | 2 | 经用户批准后使用沙箱外只读命令继续 |
| 缺少 hvigor/hvigor-config.json5 | 1 | 补齐标准 Hvigor 配置 |
| DEVECO_SDK_HOME 无效 | 2 | 使用 DevEco Studio/sdk 作为当前构建进程 SDK 根路径 |
| ArkTS 未显式声明请求对象、minHeight 不支持、动态字段访问 | 4 | 增加 DTO、显式查询序列化、替换 API 12 不支持修饰器 |
| HTTP-FLV 地址连接超时 | 1 | 记录为流媒体服务可用性风险，不伪造播放成功 |
