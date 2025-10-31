# STBackend-v2.0 API 集成指南

## 概述

本指南详细说明如何将您的Android智慧养殖应用与STBackend-v2.0 API进行集成。我们已经完成了基础的网络架构搭建，现在需要将现有的模拟数据替换为真实的API调用。

## 已完成的集成工作

### 1. 网络架构搭建 ✅
- ✅ 添加了Retrofit、OkHttp、Gson依赖
- ✅ 创建了统一的API响应模型
- ✅ 实现了认证拦截器
- ✅ 配置了Retrofit客户端

### 2. 数据模型创建 ✅
- ✅ 用户相关模型 (User, LoginRequest, LoginResponse等)
- ✅ 企业相关模型 (Enterprise, EnterpriseStats等)
- ✅ 设备相关模型 (Device, DeviceData, DeviceType等)
- ✅ 农场相关模型 (Farm, FarmSite等)
- ✅ 通知相关模型 (Notification)
- ✅ 工单相关模型 (Order)

### 3. API服务接口 ✅
- ✅ UserApiService - 用户管理
- ✅ DeviceApiService - 设备管理
- ✅ EnterpriseApiService - 企业管理
- ✅ FarmApiService - 农场管理
- ✅ NotificationApiService - 通知管理
- ✅ OrderApiService - 工单管理

### 4. 数据仓库模式 ✅
- ✅ UserRepository - 用户数据仓库
- ✅ EnterpriseRepository - 企业数据仓库
- ✅ DashboardRepository - 仪表板数据仓库

### 5. 认证管理 ✅
- ✅ AuthManager - 统一认证管理
- ✅ 自动token管理
- ✅ 登录状态持久化

## 集成步骤

### 步骤1: 登录功能集成 ✅

登录功能已经集成完成，主要修改：

1. **login.java** - 更新了登录逻辑
   - 替换模拟登录为真实API调用
   - 集成AuthManager进行token管理
   - 添加了错误处理机制

2. **UserRepository** - 实现了登录API调用
   - 使用回调接口处理异步响应
   - 统一的错误处理

### 步骤2: 仪表板数据集成 🔄

仪表板集成示例已创建，需要在实际的DashboardActivity中应用：

```java
// 在DashboardActivity中添加
private DashboardIntegrationExample dashboardIntegration;

@Override
protected void onCreate(Bundle savedInstanceState) {
    // ... 现有代码 ...
    
    // 初始化API集成
    dashboardIntegration = new DashboardIntegrationExample(this);
    
    // 更新统计数据
    updateDashboardData();
}

private void updateDashboardData() {
    dashboardIntegration.updateDashboardStats(
        tvFarmCount,      // 农场数量
        tvDeviceCount,    // 设备数量
        tvAlertCount,     // 告警数量
        tvOnlineDeviceCount,  // 在线设备数量
        tvFaultDeviceCount    // 故障设备数量
    );
}
```

### 步骤3: 设备管理集成 📋

需要在DeviceManagementActivity中集成设备API：

```java
// 创建设备
DeviceRepository deviceRepository = new DeviceRepository();
CreateDeviceRequest request = new CreateDeviceRequest(
    "摄像头-西区九号鸡舍",
    1,  // siteId
    "00:11:22:33:44:55",  // mac
    1,  // type
    "camera_xiqu_9"  // pushName
);

deviceRepository.createDevice(request, new DeviceRepository.CreateDeviceCallback() {
    @Override
    public void onSuccess(Integer deviceId) {
        // 设备创建成功
        Toast.makeText(context, "设备创建成功，ID: " + deviceId, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onError(String errorMessage) {
        // 处理错误
        Toast.makeText(context, "创建设备失败: " + errorMessage, Toast.LENGTH_LONG).show();
    }
});
```

### 步骤4: 农场管理集成 📋

需要在FarmListActivity中集成农场API：

```java
// 获取农场列表
FarmRepository farmRepository = new FarmRepository();
farmRepository.getEnterpriseFarms(new FarmRepository.FarmListCallback() {
    @Override
    public void onSuccess(List<Farm> farms) {
        // 更新农场列表UI
        updateFarmListUI(farms);
    }
    
    @Override
    public void onError(String errorMessage) {
        // 处理错误
        showErrorMessage(errorMessage);
    }
});
```

### 步骤5: 实时通知集成 📋

需要实现Server-Sent Events (SSE) 集成：

```java
// SSE连接管理
public class SSEManager {
    private static final String SSE_URL = "http://124.71.97.178:8049/sse/connect";
    
    public void connectSSE(int userId, int enterpriseId) {
        // 实现SSE连接逻辑
        // 处理实时通知推送
    }
}
```

## 需要创建的Repository

还需要创建以下Repository来完善API集成：

1. **DeviceRepository** - 设备数据仓库
2. **FarmRepository** - 农场数据仓库
3. **NotificationRepository** - 通知数据仓库
4. **OrderRepository** - 工单数据仓库

## 错误处理策略

### 网络错误处理
```java
// 统一的网络错误处理
private void handleNetworkError(Throwable throwable) {
    if (throwable instanceof java.net.UnknownHostException) {
        showError("网络连接失败，请检查网络设置");
    } else if (throwable instanceof java.net.SocketTimeoutException) {
        showError("请求超时，请稍后重试");
    } else {
        showError("网络请求失败: " + throwable.getMessage());
    }
}
```

### API错误处理
```java
// API响应错误处理
private void handleApiError(ApiResponse<?> response) {
    switch (response.getCode()) {
        case 401:
            // 未授权，跳转到登录页面
            redirectToLogin();
            break;
        case 400:
            // 参数错误
            showError(response.getMessage());
            break;
        case 500:
            // 服务器错误
            showError("服务器内部错误，请稍后重试");
            break;
        default:
            showError("未知错误: " + response.getMessage());
    }
}
```

## 测试建议

### 1. 单元测试
- 测试Repository层的API调用
- 测试数据模型的序列化/反序列化
- 测试认证管理功能

### 2. 集成测试
- 测试完整的登录流程
- 测试数据获取和显示
- 测试错误处理机制

### 3. 网络测试
- 测试网络异常情况
- 测试API超时处理
- 测试token过期处理

## 部署注意事项

### 1. 网络安全
- 确保使用HTTPS（生产环境）
- 实现证书固定
- 添加网络安全配置

### 2. 性能优化
- 实现请求缓存
- 添加图片加载优化
- 实现数据分页加载

### 3. 用户体验
- 添加加载状态指示
- 实现离线模式支持
- 优化错误提示信息

## 下一步行动

1. **立即执行**: 测试登录功能集成
2. **短期目标**: 完成仪表板数据集成
3. **中期目标**: 完成设备管理集成
4. **长期目标**: 实现完整的实时通知系统

## 联系支持

如果在集成过程中遇到问题，请参考：
- API文档：STBackend-v2.0 API 文档
- 示例代码：DashboardIntegrationExample.java
- 网络配置：RetrofitClient.kt
