@echo off
echo 正在安装和启动应用...
echo.

echo 1. 安装APK到设备...
call gradlew installDebug
if %errorlevel% neq 0 (
    echo 安装失败，请检查设备连接
    pause
    exit /b %errorlevel%
)

echo.
echo 2. 启动应用...
adb shell am start -n com.example.modelfarm/.MainActivity

echo.
echo 3. 查看应用日志（按Ctrl+C停止）...
adb logcat -s "AndroidRuntime:E" "System.err:E" "FATAL:E" "com.example.modelfarm:*"
