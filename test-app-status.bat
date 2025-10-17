@echo off
echo ========================================
echo 智慧养殖系统 - 应用状态检查
echo ========================================
echo.

echo 检查项目构建状态...
call gradlew assembleDebug
if %errorlevel% neq 0 (
    echo ❌ 构建失败
    pause
    exit /b %errorlevel%
)

echo ✅ 构建成功

echo.
echo 检查关键文件是否存在...
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo ✅ APK文件已生成
) else (
    echo ❌ APK文件未找到
)

echo.
echo 检查主题配置...
findstr "Theme.MaterialComponents" app\src\main\res\values\themes.xml
if %errorlevel% equ 0 (
    echo ✅ 主题配置正确
) else (
    echo ❌ 主题配置可能有问题
)

echo.
echo 检查布局文件...
findstr "Widget.MaterialComponents" app\src\main\res\layout\activity_login.xml
if %errorlevel% equ 0 (
    echo ✅ 布局样式配置正确
) else (
    echo ❌ 布局样式可能有问题
)

echo.
echo 应用状态检查完成！
echo.
echo 如果所有检查都通过，应用应该可以正常运行。
echo 如果仍有问题，请提供具体的错误信息。
