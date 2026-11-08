COD# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests (JVM)
./gradlew test

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Clean build
./gradlew clean

# Install debug APK to connected device
./gradlew installDebug
```

## Architecture Overview

This is **modelfarm** (数智化养殖软件平台), an Android smart farming management app. It uses a **mixed Java/Kotlin** codebase: new code is written in Kotlin, but the majority of Activities are still Java. UI is primarily **traditional XML layouts** (not Compose). The project is configured for Jetpack Compose but only uses it in `MainActivity` as a routing shell.

### Layer Structure

```
Activity (Java/Kotlin, XML layout)
    └── Repository (Kotlin, callback-based)
        └── Retrofit ApiService interface (Kotlin)
            └── RetrofitClient singleton (Kotlin)
```

- **Activities** directly instantiate Repository classes and call methods with anonymous callback objects.
- **Repositories** wrap Retrofit calls, handle the `ApiResponse<T>` envelope (check `code == 200`), and dispatch to app-defined callback interfaces (`onSuccess` / `onError`).
- **ApiServices** are Retrofit interfaces defining endpoints. Each domain has its own service file.
- **Models** are Kotlin `data class`es in `network.models`, annotated with `@SerializedName` for backend field mapping when needed.

### Network Layer (Kotlin)

| File | Role |
|------|------|
| `network/ApiConfig.kt` | BASE_URL constant (`http://124.71.97.178:8049/`) |
| `network/RetrofitClient.kt` | Singleton Retrofit builder with `AuthInterceptor`, logging interceptor, 30s timeouts, Gson with `yyyy-MM-dd HH:mm:ss` date format |
| `network/AuthInterceptor.kt` | OkHttp interceptor that adds `satoken` header from `AuthManager` |
| `network/AuthManager.kt` | Token/user/enterprise persistence via SharedPreferences (`SmartFarmAuth`). Thread-safe singleton. |
| `network/OssUploadHelper.kt` | Uploads files via OSS pre-signed PUT URLs — first calls `/oss/signature`, then uploads bytes directly with OkHttp |

API responses follow a uniform envelope: `ApiResponse<T> { code: Int, message: String, data: T? }`. Repositories treat `code == 200` as success.

### Package: `com.example.modelfarm` (root)

Main package containing all Java Activities:

- `MainActivity.kt` — Entry point, routes to `DashboardActivity` (logged in) or `logins.login` (logged out)
- `DashboardActivity.java` — Main dashboard with cards for farm management, environment monitoring, device management, work orders, admin
- `DeviceManagementActivity.java` — Device list with online/offline stats
- `DeviceDetailActivity.java` / `DeviceListActivity.java`
- `EnvironmentMonitoringEnhancedActivity.java` — Environmental monitoring
- `OrderCreateActivity.java`, `OrderListActivity.java`, `OrderDetailActivity.java`, `OrderDispatchActivity.java` — Work order management
- `LiveStreamActivity.java` — RTMP live stream via ExoPlayer
- `AdminActivity.java` — User management
- `DataAnalysisActivity.java`, `DataAnalysisEnhancedActivity.java` — Data analysis/charts
- `MessageNotificationActivity.java` — Notifications

Adapters: `DeviceAdapter.java`, `AdminUserAdapter.java`, `AlertAdapter.java`, `NotificationAdapter.java`, `OrderListAdapter.java`

### Sub-packages (Java)

| Package | Purpose |
|---------|---------|
| `logins` | Authentication flow: `login.java`, `register.java`, `choose_company.java`, `create_company.java`, `join_company.java` |
| `farm` | Farm management: `farm_list.java`, `farm_detail.java`, `farm_point_detail.java`, `FarmAdapter.java`, `FarmPointAdapter.java` |
| `company` | Enterprise: `CompanyListActivity.java`, `company_info.java`, `CompanyAdapter.java` |
| `personal` | User profile: `profile.java` |
| `utils` | Helpers: `LoginIntegrationHelper.java`, `SimpleLoginHelper.java`, `DeviceDataParser.java`, `FarmDataDisplay.java`, etc. |

### Network Models & Services (Kotlin, under `network/`)

- **models/Farm.kt** — `Farm`, `FarmSite`, create/update requests
- **models/Device.kt** — `Device` (type 1=camera, 2=sensor, 3=controller; status 1=online, 2=offline, 3=fault), `DeviceData`, `DeviceType`
- **models/Order.kt** — `Order` (status: 1=pending, 2=completed, 3=urgent, 4=claimed), `OrderWithMedias`
- **models/User.kt** — `LoginRequest/Response`, `SaToken`, `User`, register/update request models
- **models/Enterprise.kt** — Enterprise-related models
- **models/Inout.kt** — `MonthInoutData` for animal in/out statistics
- **models/Notification.kt**, **models/Admin.kt**, **models/OssModels.kt**

Corresponding services: `FarmApiService.kt`, `DeviceApiService.kt`, `OrderApiService.kt`, `UserApiService.kt`, `EnterpriseApiService.kt`, `AdminApiService.kt`, `NotificationApiService.kt`, `OssApiService.kt`

Repositories: `FarmRepository.kt`, `DeviceRepository.kt`, `OrderRepository.kt`, `UserRepository.kt`, `EnterpriseRepository.kt`, `DashboardRepository.kt`, `NotificationRepository.kt`

### Auth Flow

1. User logs in via `/user/login` → receives `LoginResponse` containing `SaToken.tokenValue`
2. `AuthManager.saveLoginInfo()` persists token, userId, enterpriseId to SharedPreferences
3. `AuthInterceptor` adds `satoken` header to every request (read from `AuthManager`)
4. `MainActivity` checks `AuthManager.isLoggedIn()` to decide initial screen
5. Logout clears SharedPreferences

### Key Dependencies

- **Networking**: Retrofit 2 + OkHttp + Gson
- **Media**: ExoPlayer (RTMP streaming), RxFFmpeg (soft decoding), Glide (image loading)
- **Charts**: MPAndroidChart (via JitPack)
- **UI**: Material Components, AppCompat, RecyclerView, ConstraintLayout, Jetpack Compose BOM (configured but barely used)

### Mixed Java/Kotlin

New Kotlin code calls Java activities directly (e.g., `MainActivity.kt` starts `DashboardActivity::class.java`). Java code uses `RetrofitClient.create(context, ServiceClass::class.java)` to get service instances (the Kotlin `RetrofitClient` has `@JvmStatic` helpers). Model classes are in Kotlin and accessed from Java via getters/setters.
