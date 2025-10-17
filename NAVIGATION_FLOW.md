# 智慧养殖系统 - 页面跳转流程

## 📱 完整的页面跳转逻辑

### 1. 应用启动流程
```
MainActivity → login.login (直接跳转)
```

### 2. 登录相关流程
```
login.login → company.CompanyListActivity (登录成功)
login.login → login.register (点击注册)
login.register → login.login (注册成功)
```

### 3. 企业选择流程
```
company.CompanyListActivity → com.example.modelfarm.DashboardActivity (选择企业)
company.CompanyListActivity → login.join_company (加入企业)
company.CompanyListActivity → login.create_company (创建企业)
```

### 4. 主控制台流程
```
DashboardActivity → DeviceManagementActivity (设备管理)
DashboardActivity → EnvironmentMonitoringActivity (环境监控)
DashboardActivity → DataAnalysisActivity (数据分析)
DashboardActivity → MessageNotificationActivity (消息通知)
```

### 5. 设备管理流程
```
DeviceManagementActivity → DeviceListActivity (查看设备列表)
DeviceListActivity → DeviceDetailActivity (设备详情)
DeviceManagementActivity → DashboardActivity (返回主控制台)
```

### 6. 其他功能流程
```
MessageNotificationActivity → DashboardActivity (返回主控制台)
EnvironmentMonitoringActivity → DashboardActivity (返回主控制台)
DataAnalysisActivity → DashboardActivity (返回主控制台)
```

## 🔄 返回逻辑

### 有返回按钮的页面：
- ✅ DeviceManagementActivity (Toolbar返回)
- ✅ MessageNotificationActivity (Toolbar返回)
- ✅ EnvironmentMonitoringActivity (Toolbar返回)
- ✅ DataAnalysisActivity (Toolbar返回)
- ✅ DeviceListActivity (Toolbar返回)
- ✅ DeviceDetailActivity (Toolbar返回)

### 无返回按钮的页面：
- ✅ MainActivity (启动页，直接跳转)
- ✅ login.login (登录页，无返回)
- ✅ login.register (注册页，返回登录)
- ✅ company.CompanyListActivity (企业选择页，无返回)
- ✅ DashboardActivity (主控制台，无返回)

## 🎯 关键跳转点

1. **登录成功** → 企业列表
2. **选择企业** → 主控制台
3. **主控制台** → 各功能模块
4. **各功能模块** → 返回主控制台

## ✅ 跳转逻辑验证

所有页面的跳转逻辑都已完善：
- 登录流程完整
- 企业选择流程完整
- 主控制台功能完整
- 返回逻辑正确
- 动画过渡流畅
