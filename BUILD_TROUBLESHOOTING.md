# MPAndroidChart 依赖下载问题解决方案

## 问题描述
构建时出现 SSL 证书验证错误，无法从 JitPack 下载 MPAndroidChart 依赖。

## 解决方案

### 方案 1: 配置网络代理（推荐）
如果您在公司网络或使用代理，请在 `gradle.properties` 中配置代理设置：

```properties
systemProp.http.proxyHost=your.proxy.host
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=your.proxy.host
systemProp.https.proxyPort=8080
```

### 方案 2: 使用本地 Maven 仓库
1. 手动下载 MPAndroidChart JAR 文件
2. 将其放入项目的 `libs` 目录
3. 在 `app/build.gradle.kts` 中使用本地依赖：

```kotlin
implementation(files("libs/MPAndroidChart-v3.1.0.aar"))
```

### 方案 3: 使用替代图表库
如果 MPAndroidChart 无法下载，可以考虑使用其他图表库：

#### 选项 A: Vico (Compose 图表库)
```kotlin
implementation("com.patrykandpatrick.vico:compose:1.13.1")
```

#### 选项 B: 使用 Android Canvas 自定义绘制
创建一个简单的自定义 View 来绘制折线图。

### 方案 4: 临时禁用 SSL 验证（不推荐，仅用于开发环境）
在 `gradle.properties` 中添加：

```properties
systemProp.javax.net.ssl.trustStore=NONE
```

**注意：** 这会降低安全性，仅应在开发环境中使用。

## 验证
运行以下命令验证依赖是否已正确下载：
```bash
./gradlew :app:dependencies --configuration debugRuntimeClasspath
```

