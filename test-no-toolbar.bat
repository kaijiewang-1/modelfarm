@echo off
echo ========================================
echo 智慧养殖系统 - 去除顶部标题栏测试
echo ========================================
echo.

echo 修改内容:
echo 1. 主题改为 NoActionBar
echo 2. 所有Toolbar背景改为透明
echo 3. 所有Toolbar标题清空
echo 4. 返回按钮颜色改为绿色
echo.

echo 1. 安装APK...
call gradlew installDebug
if %errorlevel% neq 0 (
    echo 安装失败
    pause
    exit /b %errorlevel%
)

echo.
echo 2. 启动应用...
adb shell am start -n com.example.modelfarm/.MainActivity

echo.
echo 3. 测试页面跳转...
echo 请测试以下页面，确认顶部没有黑色标题栏:
echo - 登录页面
echo - 企业选择页面  
echo - 主控制台
echo - 设备管理页面
echo - 环境监控页面
echo - 数据分析页面
echo.
echo 如果仍有黑色标题栏，请提供具体页面信息。
