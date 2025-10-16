# ModelFarm Android 项目环境配置指南

## 项目概述
这是一个基于Android的智慧农场管理系统，使用Java和Kotlin开发，支持Android API 24+。

## 环境要求

### 1. Java开发环境
- **Java版本**: Java 11 或更高版本（推荐Java 17）
- **当前环境**: Java 17.0.12 ✅
- **验证命令**: `java -version`

### 2. Android开发环境
- **Android SDK**: 已配置在 `C:\Users\wangkaijie\AppData\Local\Android\Sdk` ✅
- **编译SDK**: API 35
- **最低SDK**: API 24
- **目标SDK**: API 35

### 3. 构建工具
- **Gradle版本**: 8.11.1 ✅
- **Android Gradle Plugin**: 8.9.1
- **Kotlin版本**: 2.0.21

## 项目结构
```
modelfarm/
├── app/
│   ├── src/main/java/          # Java源代码
│   │   ├── farm/               # 农场管理模块
│   │   ├── login/              # 登录模块
│   │   └── com/example/modelfarm/ # 主应用模块
│   ├── src/main/res/           # 资源文件
│   │   ├── layout/             # 布局文件
│   │   ├── drawable/           # 图标资源
│   │   └── values/             # 样式和颜色
│   └── build.gradle.kts        # 应用级构建配置
├── build.gradle.kts            # 项目级构建配置
├── gradle/                     # Gradle配置
└── settings.gradle.kts         # 项目设置
```

## 主要依赖库
- **AndroidX Core**: 1.16.0
- **AndroidX Lifecycle**: 2.9.2
- **AndroidX Compose**: 2024.09.00
- **Material Design**: 1.12.0
- **AndroidX AppCompat**: 1.7.1
- **AndroidX ConstraintLayout**: 2.2.1

## 开发环境配置步骤

### 1. 克隆项目
```bash
git clone <repository-url>
cd modelfarm
```

### 2. 配置Android SDK
确保Android SDK已正确安装并配置环境变量：
- Windows: 设置 `ANDROID_HOME` 环境变量
- 或确保 `local.properties` 文件中的 `sdk.dir` 路径正确

### 3. 同步项目依赖
```bash
# Windows
.\gradlew build

# Linux/Mac
./gradlew build
```

### 4. 运行项目
```bash
# 构建Debug版本
.\gradlew assembleDebug

# 安装到设备
.\gradlew installDebug
```

## 已修复的问题

### Lint错误修复
- **问题**: `join_company.java` 中 `btnBack` 类型不匹配
- **原因**: 布局文件中定义为 `ImageButton`，但Java代码中声明为 `Button`
- **解决方案**: 
  1. 将Java代码中的类型从 `Button` 改为 `ImageButton`
  2. 添加必要的import语句
  3. 为布局文件中的ImageButton添加ID

## 开发建议

### 1. 代码规范
- 使用Android Studio的代码格式化功能
- 遵循Java命名规范
- 及时处理Lint警告

### 2. 版本控制
- 提交前运行 `.\gradlew build` 确保构建成功
- 避免提交 `local.properties` 文件
- 定期同步依赖更新

### 3. 调试建议
- 使用Android Studio的调试工具
- 查看Lint报告：`app/build/reports/lint-results-debug.html`
- 使用Logcat查看运行时日志

## 常见问题解决

### 1. 构建失败
```bash
# 清理项目
.\gradlew clean

# 重新构建
.\gradlew build
```

### 2. 依赖冲突
检查 `gradle/libs.versions.toml` 文件中的版本配置

### 3. SDK问题
确保Android SDK路径正确，API级别已安装

## 项目状态
- ✅ Java环境配置完成
- ✅ Android SDK配置完成  
- ✅ Gradle构建配置完成
- ✅ 项目依赖同步完成
- ✅ Lint错误修复完成
- ✅ 构建测试通过

项目已准备就绪，可以开始开发！
