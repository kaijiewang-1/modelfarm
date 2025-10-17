@echo off
echo ========================================
echo 智慧养殖系统 - 最终测试
echo ========================================
echo.

echo 问题已修复: 主题兼容性问题
echo 原因: AppCompatActivity需要Theme.AppCompat主题
echo 修复: 将主题从android:Theme.Material改为Theme.AppCompat
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
echo 现在应该能正常显示登录页面了！
echo.
adb logcat -s "AndroidRuntime:E" "System.err:E" "FATAL:E" "com.example.modelfarm:*"
