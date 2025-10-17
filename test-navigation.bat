@echo off
echo ========================================
echo 智慧养殖系统 - 页面跳转测试
echo ========================================
echo.

echo 测试内容:
echo 1. 应用启动直接进入登录页面
echo 2. 登录成功后进入企业选择页面
echo 3. 选择企业后进入主控制台
echo 4. 各功能模块的跳转和返回
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
echo 3. 查看应用日志 (按Ctrl+C停止)...
echo 请测试以下流程:
echo - 应用启动后直接显示登录页面
echo - 输入任意用户名和密码登录
echo - 选择企业进入主控制台
echo - 测试各功能模块的跳转
echo.
adb logcat -s "AndroidRuntime:E" "System.err:E" "FATAL:E" "com.example.modelfarm:*"
