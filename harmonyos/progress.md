# 工作日志

## 2026-11-08
- 养殖场详情、养殖点详情、设备创建、设备详情、工单创建页面已实现，且新增路由全部通过 ArkTS 编译。
- 环境字段同步为六项真实读数；主页计时器改用 onPageShow/onPageHide；失效会话统一清理返回登录；移除硬编码共享游客账号。
- 视频中心打包 flv.js 1.6.2 与 Apache-2.0 LICENSE，接入本地 Web 播放器、原画/智慧流、超时重试、元数据框与资源回收；保留后端和设备能力限制提示。
- 中间编译错误：无上下文补丁把 import 放在文件尾（已移到顶部）；ArkTS 禁止直接 throw 未收窄 catch 值（已修复）。修复后 BUILD SUCCESSFUL。
- hdc list targets 返回 [Empty]，无已连接真机/模拟器；本次不宣称设备级运行验证通过。
- 复核已有迁移工程及 Android 接口与模型。发现养殖场点击仍为占位提示、设备缺少创建/详情、工单仅支持完成；环境字段仍为旧版光照/风速/水位。
- 本次在既有 ArkUI 结构上补齐核心流程，保留旧工作记录和所有 Android 改动。

## 2026-08-19

- 读取并启用 using-superpowers、planning-with-files-zh、security-practices、testing-best-practices。
- 完成 HarmonyOS 工程与接口文档盘点；保留上级 Android 工程已有改动。
- 补齐 hvigor/hvigor-config.json5，并用 DevEco Studio 内置 SDK 根路径完成 HAP 构建。
- 修复 ArkTS DTO、查询参数、API 12 布局修饰器和设备状态字符串兼容问题。
- 新增 LiveService、LivePage，接入农场地块、摄像头查询和 live ticket 业务链路。
- 新增 scripts/check-backend.ps1，使用环境变量凭据完成登录、统计、农场、地块、摄像头、票据和登出验证。
- 最终验证：BUILD SUCCESSFUL；接口脚本返回 enterpriseId=4、cameraId=48、streamId=gzcam-north-1。
- 已知风险：HAP 未配置签名；HTTP-FLV 主机连接超时；ArkTS 仍有弃用 API 和 Preferences 异常处理警告。
