@echo off
echo ========================================
echo 智慧养殖系统 - 应用测试脚本
echo ========================================
echo.

echo 1. 检查设备连接...
adb devices
if %errorlevel% neq 0 (
    echo 错误: ADB未找到或设备未连接
    echo 请确保:
    echo - Android SDK已安装
    echo - ADB已添加到PATH
    echo - 设备已连接并启用USB调试
    pause
    exit /b 1
)

echo.
echo 2. 清理并重新构建项目...
call gradlew clean assembleDebug
if %errorlevel% neq 0 (
    echo 构建失败
    pause
    exit /b %errorlevel%
)

echo.
echo 3. 安装APK到设备...
call gradlew installDebug
if %errorlevel% neq 0 (
    echo 安装失败
    pause
    exit /b %errorlevel%
)

echo.
echo 4. 启动应用...
adb shell am start -n com.example.modelfarm/.MainActivity

echo.
echo 5. 查看应用日志 (按Ctrl+C停止)...
echo 如果应用闪退，请查看下面的错误信息:
echo.
adb logcat -s "AndroidRuntime:E" "System.err:E" "FATAL:E" "com.example.modelfarm:*"
