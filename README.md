# Wireless Explorer

> Android 무선 프로토콜 통합 탐색 도구 — BLE, Wi-Fi, NFC, UWB를 하나의 인터페이스로.

<p align="center">
  <img src="app/src/main/res/drawable/ic_app_icon_stitch.jpg" width="120" alt="Wireless Explorer" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.2.20-7F52FF?logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Compose-Material3-4285F4?logo=jetpackcompose&logoColor=white" />
  <img src="https://img.shields.io/badge/minSdk-33-34A853" />
  <img src="https://img.shields.io/badge/targetSdk-36-34A853" />
  <img src="https://img.shields.io/badge/Architecture-Clean-FF6F00" />
</p>

---

## Overview

Wireless Explorer는 Android 디바이스 주변의 무선 환경을 실시간으로 탐색하고 분석하는 네이티브 앱입니다.

BLE 디바이스 스캔 및 GATT 서비스 탐색, Wi-Fi 네트워크 분석(Wi-Fi 7 포함), NFC 태그 읽기, UWB 거리 측정까지 — 4가지 무선 프로토콜을 단일 앱에서 통합 관리합니다.

### Key Features

| Feature | Description |
|---------|-------------|
| **BLE Scanner** | BLE 디바이스 스캔, GATT 서비스/Characteristic 탐색, 실시간 RSSI 모니터링 |
| **Wi-Fi Analyzer** | 주변 네트워크 스캔, 채널/대역폭/보안 분석, Wi-Fi 7 (802.11be) 지원 |
| **NFC Reader** | NFC 태그 감지 및 NDEF 레코드 파싱, 태그 메타데이터 표시 |
| **UWB Ranging** | Ultra-Wideband 기반 거리/각도 측정 (AndroidX UWB) |
| **Dashboard** | 연결 상태 요약, 빠른 액션, 실시간 트래픽 통계 |
| **Device History** | 탐색 이력 저장, 검색, CSV 내보내기 |
| **Analytics** | 기술별 사용 통계 시각화 (도넛 차트, Top 5 디바이스) |

---

## Architecture

Clean Architecture + Unidirectional Data Flow 기반으로 설계했습니다.

```
┌──────────────────────────────────────────────────────┐
│  Presentation                                        │
│  Compose UI → ViewModel (StateFlow + UiState)        │
├──────────────────────────────────────────────────────┤
│  Domain                                              │
│  UseCase (operator invoke) → Repository Interface    │
├──────────────────────────────────────────────────────┤
│  Data                                                │
│  RepositoryImpl → DataSource → Room / DataStore      │
└──────────────────────────────────────────────────────┘
```

### Module Dependency Graph

```
                        ┌─────┐
                        │ app │
                        └──┬──┘
              ┌────────────┼────────────┐
              ▼            ▼            ▼
        ┌──────────┐ ┌──────────┐ ┌──────────┐
        │ feature/ │ │ core:ui  │ │core:nav  │
        │ dashboard│ │          │ │          │
        │ devices* │ └────┬─────┘ └──────────┘
        │ detail   │      │
        │ history  │      ▼
        │ search   │ ┌───────────┐   ┌────────────┐
        │ analyze  │ │core:design│   │ core:data   │
        │ settings │ │  system   │   │ core:db     │
        │ onboard  │ └───────────┘   │ core:ds     │
        └────┬─────┘                 └──────┬─────┘
             │                              │
             ▼                              ▼
      ┌─────────────┐              ┌──────────────┐
      │ core:domain │              │  core:model   │
      └──────┬──────┘              └──────────────┘
             │                            ▲
             └────────────────────────────┘
```

### Module Overview

23개 모듈 (1 app + 10 core + 12 feature):

| Module | Responsibility |
|--------|---------------|
| `app` | Composition Root, Hilt entry point, Navigation host |
| `core:common` | Shared qualifiers (`@IoDispatcher`), `TimeProvider` |
| `core:model` | Pure domain models — JVM 모듈, Android 의존성 없음 |
| `core:domain` | Repository Interface + UseCase Interface/Impl — JVM 모듈 |
| `core:data` | Repository Impl, DataSource 구현 |
| `core:database` | Room DB, DAO, Entity, 마이그레이션 |
| `core:datastore` | DataStore preferences 관리 |
| `core:designsystem` | Theme, Color, Typography, 공유 디자인 토큰 |
| `core:ui` | 재사용 Compose 컴포넌트 (SignalBars, PermissionHandler 등) |
| `core:navigation` | Route 정의, TopLevelDestination |
| `core:testing` | 테스트 유틸리티, Fake 구현체 |
| `feature:dashboard` | 연결 상태 대시보드 |
| `feature:devices` | 탭 컨테이너 (BLE/Wi-Fi/NFC/UWB) |
| `feature:devices-ble` | BLE 스캔 + GATT 서비스 탐색 |
| `feature:devices-wifi` | Wi-Fi 네트워크 분석 |
| `feature:devices-nfc` | NFC 태그 리더 |
| `feature:devices-uwb` | UWB 거리/각도 측정 |
| `feature:detail` | 디바이스 상세 화면 |
| `feature:history` | 탐색 이력 관리 |
| `feature:search` | 통합 검색 |
| `feature:analyze` | 사용 통계 분석 |
| `feature:settings` | 앱 설정 |
| `feature:onboarding` | 권한 온보딩 플로우 |

### Design Decisions

- **UseCase Interface 분리** — 모든 UseCase는 Interface + Impl로 분리하여 테스트 용이성 확보
- **`operator fun invoke()`** — UseCase를 함수처럼 호출 (`getDeviceHistory(params)`)
- **`kotlin.Result<T>`** — 에러 핸들링 표준화, 커스텀 Result 클래스 없이 Kotlin 표준 사용
- **`callbackFlow` 기반 스캐너** — BLE/Wi-Fi 시스템 콜백을 Flow로 래핑, `awaitClose`에서 리소스 해제 보장
- **DataStore only** — SharedPreferences 사용 없이 DataStore로 통일
- **NIA 기반 모듈화** — Now in Android 구조 참고. `core:model`은 순수 JVM으로 분리하여 빌드 속도 최적화, `core:domain`은 단일 모듈로 통합하여 UseCase 간 참조 단순화
- **Convention Plugins** — `build-logic/convention`에 8개 플러그인으로 빌드 설정 중앙 관리 (Application, Library, Compose, Hilt, Room, JVM, Feature, Serialization)

### Wireless Protocol Handling

```kotlin
// BLE 스캔 — callbackFlow로 시스템 콜백 래핑
fun scan(): Flow<List<BleDevice>> = callbackFlow {
    val callback = object : ScanCallback() {
        override fun onBatchScanResults(results: List<ScanResult>) {
            trySend(results.map { it.toDomain() })
        }
    }
    scanner.startScan(callback)
    awaitClose {
        scanner.stopScan(callback)
        // BLE 리소스 누수 방지
    }
}.flowOn(Dispatchers.IO)
```

---

## Tech Stack

| Category | Stack |
|----------|-------|
| Language | Kotlin 2.2.20, JVM 21 |
| UI | Jetpack Compose (BOM 2025.10.00), Material 3 |
| Architecture | Clean Architecture, Hilt 2.57.1, StateFlow |
| Async | Kotlin Coroutines 1.10.2, Flow |
| Navigation | Compose Navigation 2.9.5 (Type-safe routes) |
| Persistence | Room 2.8.2, DataStore 1.1.7 |
| Chart | Vico 2.4.1, Canvas (Donut chart) |
| Animation | Lottie Compose 6.7.1 |
| Build | Gradle Kotlin DSL, AGP 8.13.0, KSP 2.2.20, Convention Plugins (8개) |
| Modules | 23 modules (1 app + 10 core + 12 feature) |
| Quality | Detekt 1.23.8, Kover (70%+ coverage) |
| Test | JUnit 4, MockK 1.13.4, Turbine 1.2.0 |
| Debug | Timber 5.0.1, LeakCanary 2.14 |

---

## Project Structure

```
WirelessExplorer/
├── app/                          # Composition Root + Navigation Host
├── build-logic/convention/       # 8 Convention Plugins
├── core/
│   ├── common/                   # Shared qualifiers, TimeProvider
│   ├── model/                    # Pure domain models (JVM)
│   ├── domain/                   # Repository IF + UseCase IF/Impl (JVM)
│   ├── data/                     # Repository Impl + DataSource
│   ├── database/                 # Room DB, DAO, Entity
│   ├── datastore/                # DataStore preferences
│   ├── designsystem/             # Theme, Color, Typography
│   ├── ui/                       # Shared Compose components
│   ├── navigation/               # Routes, TopLevelDestination
│   └── testing/                  # Test utilities, fakes
├── feature/
│   ├── dashboard/                # Connection status overview
│   ├── devices/                  # Tab container (BLE/WiFi/NFC/UWB)
│   ├── devices-ble/              # BLE scan + GATT explorer
│   ├── devices-wifi/             # WiFi network analyzer
│   ├── devices-nfc/              # NFC tag reader
│   ├── devices-uwb/              # UWB ranging
│   ├── detail/                   # Device detail view
│   ├── history/                  # Scan history
│   ├── search/                   # Unified search
│   ├── analyze/                  # Usage analytics
│   ├── settings/                 # App settings
│   └── onboarding/               # Permission onboarding
└── gradle/libs.versions.toml     # Version catalog
```

---

## Getting Started

### Requirements

- Android Studio Ladybug 이상
- JDK 21
- Android 13 (API 33)+ 실기기 권장 (BLE/NFC/UWB는 에뮬레이터 미지원)

### Build & Run

```bash
# Clone
git clone https://github.com/jsh-me/WirelessExplorer.git
cd WirelessExplorer

# Build
./gradlew assembleDebug

# Test
./gradlew testDebugUnitTest

# Coverage Report
./gradlew koverHtmlReportDebug
```

### Required Permissions

| Permission | Purpose |
|------------|---------|
| `BLUETOOTH_SCAN` | BLE 디바이스 스캔 |
| `BLUETOOTH_CONNECT` | BLE GATT 연결 |
| `NEARBY_WIFI_DEVICES` | Wi-Fi 네트워크 스캔 |
| `NFC` | NFC 태그 읽기 |
| `UWB_RANGING` | UWB 거리 측정 |
| `POST_NOTIFICATIONS` | Foreground Service 알림 |

> minSdk 33 타겟이므로 `ACCESS_FINE_LOCATION` 없이 BLE/Wi-Fi 스캔이 가능합니다.
> 일부 OEM 기기 호환성을 위해 선언은 유지합니다.

---

## Design

Dark theme 기반, 각 무선 기술별 컬러 코딩으로 직관적인 구분을 제공합니다.

| Protocol | Color | Hex |
|----------|-------|-----|
| BLE | Blue | `#3B82F6` |
| Wi-Fi | Green | `#22C55E` |
| NFC | Amber | `#F59E0B` |
| UWB | Violet | `#8B5CF6` |

Primary accent: `#330DF2` (Indigo/Violet)

---

## License

```
Copyright 2026 Sungho Jung

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
