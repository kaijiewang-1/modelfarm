# 调研发现

## 2026-11-08 迁移差距
- Android 当前环境页已使用温度、湿度、CO₂、NH₃、PM2.5、PM10，鸿蒙尚未同步。
- 可复用的真实接口：GET /farm/sites、GET /farmSite/devices、POST /device、GET /device、POST /order、POST /order/accept。
- Android 摄像头使用 pushName 中的完整 RTMP URL；原生鸿蒙媒体能力与 FLV/RTMP 支持需要明确处理，不能把获取票据视为播放成功。
- DevEco Studio 与 API 12 SDK 在本机存在；无签名配置，预计交付 unsigned HAP。

## 工程与构建

- 当前目录是 HarmonyOS Stage 工程，目标 API 12，设备类型 phone/tablet。
- 缺少 hvigor 配置会导致构建在 CompileArkTS 前直接失败；补齐后可进入编译。
- 最终 Hvigor 结果：BUILD SUCCESSFUL，产物 entry/build/default/outputs/default/entry-default-unsigned.hap。
- 当前 build-profile 没有 signingConfigs，真机安装前必须在 DevEco Studio 配置自动签名。

## 后端实测

- 登录、统计、农场、地块、设备/摄像头和票据均返回 code=200；satoken 通过请求头传递。
- 当前部署实际路由为 /user/login、/enterprise/count、/farm/sites、/farmSite/camera、/live/ticket。
- 访问 /api/user/login、/api/farmSite/camera、/api/live/ticket 返回 No static resource，说明视频文档中的 /api 前缀与部署不一致。
- 实测测试账号所属企业为 4，首个农场为 5，地块为 7，摄像头为 48；票据返回 streamId=gzcam-north-1 和 WebSocket 地址。
- HTTP-FLV 地址 http://1.95.8.122:8080/live/gzcam-north-1.flv 当前 curl 10 秒超时；不能宣称媒体播放已打通。

## 代码变更

- ApiClient 查询参数改为显式白名单序列化，避免 ArkTS 动态索引和参数漂移。
- FarmService 增加 FarmSite 查询；LiveService/LivePage 提供摄像头列表和智慧直播票据初始化。
- 设备 status 兼容后端字符串和数字，修复在线/故障标签判断。
- README 增加构建、接口脚本和路由差异说明。
