# Android应用闪退调试指南

## 问题诊断步骤

### 1. 检查设备连接
```bash
# 检查ADB连接
adb devices

# 如果ADB未找到，请确保Android SDK已正确安装并添加到PATH
```

### 2. 安装和启动应用
```bash
# 安装APK
.\gradlew installDebug

# 启动应用
adb shell am start -n com.example.modelfarm/.MainActivity
```

### 3. 查看崩溃日志
```bash
# 查看实时日志
adb logcat -s "AndroidRuntime:E" "System.err:E" "FATAL:E"

# 或者查看特定应用的日志
adb logcat | findstr "com.example.modelfarm"
```

### 4. 常见闪退原因和解决方案

#### 4.1 Activity未注册
- **问题**: `ActivityNotFoundException`
- **解决**: 确保所有Activity都在AndroidManifest.xml中注册

#### 4.2 布局文件缺失
- **问题**: `Resources$NotFoundException`
- **解决**: 检查布局文件是否存在且ID正确

#### 4.3 空指针异常
- **问题**: `NullPointerException`
- **解决**: 检查findViewById是否返回null

#### 4.4 权限问题
- **问题**: `SecurityException`
- **解决**: 检查AndroidManifest.xml中的权限声明

### 5. 调试技巧

#### 5.1 添加异常捕获
```java
try {
    // 可能出错的代码
} catch (Exception e) {
    Log.e("TAG", "Error: " + e.getMessage(), e);
    Toast.makeText(this, "错误: " + e.getMessage(), Toast.LENGTH_LONG).show();
}
```

#### 5.2 使用Log输出调试信息
```java
Log.d("TAG", "Debug message");
Log.e("TAG", "Error message", exception);
```

#### 5.3 检查资源文件
- 确保所有drawable、layout、anim文件存在
- 检查XML语法是否正确
- 验证资源ID是否匹配

### 6. 项目特定检查

#### 6.1 已修复的问题
- ✅ 添加了缺失的Activity注册
- ✅ 修复了MainActivity的异常处理
- ✅ 添加了错误提示机制

#### 6.2 需要检查的文件
- `app/src/main/AndroidManifest.xml` - Activity注册
- `app/src/main/res/layout/activity_login.xml` - 布局文件
- `app/src/main/res/anim/` - 动画资源
- `app/src/main/res/drawable/` - 图标资源

### 7. 运行测试

使用提供的调试脚本：
```bash
# Windows
debug-app.bat

# 或手动执行
.\gradlew installDebug
adb shell am start -n com.example.modelfarm/.MainActivity
adb logcat -s "AndroidRuntime:E" "System.err:E" "FATAL:E"
```

### 8. 如果仍然闪退

1. **检查设备兼容性**
   - 确保设备Android版本 >= 7.0 (API 24)
   - 检查设备存储空间

2. **清理重建**
   ```bash
   .\gradlew clean
   .\gradlew assembleDebug
   ```

3. **检查依赖冲突**
   - 查看build.gradle.kts中的依赖版本
   - 确保没有版本冲突

4. **简化测试**
   - 创建最简单的Activity进行测试
   - 逐步添加功能

## 联系支持

如果问题仍然存在，请提供：
1. 完整的logcat输出
2. 设备信息（Android版本、型号）
3. 具体的错误信息
4. 重现步骤
