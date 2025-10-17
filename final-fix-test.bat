@echo off
echo ========================================
echo 智慧养殖系统 - 最终修复测试
echo ========================================
echo.

echo 修复内容:
echo 1. 主题兼容性: Theme.MaterialComponents
echo 2. TextInputLayout样式: Widget.MaterialComponents
echo 3. 空指针异常: 添加null检查
echo 4. onResume延迟执行: 确保UI初始化完成
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
adb logcat -s "AndroidRuntime:E" "System.err:E" "FATAL:E" "com.example.modelfarm:*" "LoginActivity:*"
