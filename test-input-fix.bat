@echo off
echo ========================================
echo 智慧养殖系统 - 输入和注册修复测试
echo ========================================
echo.

echo 修复内容:
echo 1. 注册页面样式兼容性修复
echo 2. 添加缺失的图标资源
echo 3. 增强异常处理
echo 4. 密码输入框验证
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
echo 3. 测试功能...
echo 请测试以下功能:
echo - 密码输入框是否可以正常输入
echo - 点击注册按钮是否正常跳转
echo - 注册页面是否正常显示
echo - 注册页面输入框是否正常工作
echo.
echo 如果仍有问题，请提供具体的错误信息。
