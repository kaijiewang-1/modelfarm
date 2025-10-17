@echo off
echo ========================================
echo 智慧养殖系统 - 崩溃调试脚本
echo ========================================
echo.

echo 1. 清理并重新构建...
call gradlew clean assembleDebug
if %errorlevel% neq 0 (
    echo 构建失败
    pause
    exit /b %errorlevel%
)

echo.
echo 2. 安装APK...
call gradlew installDebug
if %errorlevel% neq 0 (
    echo 安装失败
    pause
    exit /b %errorlevel%
)

echo.
echo 3. 启动应用...
adb shell am start -n com.example.modelfarm/.MainActivity

echo.
echo 4. 等待3秒后查看日志...
timeout /t 3 /nobreak >nul

echo.
echo 5. 查看应用日志 (按Ctrl+C停止)...
echo 如果看到错误，请记录完整的错误信息
echo.
adb logcat -s "AndroidRuntime:E" "System.err:E" "FATAL:E" "com.example.modelfarm:*" "LoginActivity:*"
