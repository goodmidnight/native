# Native Scanner

A cross-platform document scanner powered by a shared C++ core engine (OpenCV) with an Android application built on Jetpack Compose and MVI architecture.

## Key Features

- **Real-time Document Detection** — Multi-criteria contour scoring with EMA smoothing for stable detection
- **Auto-stability Detection** — Hands-free capture trigger when document corners stabilize
- **Perspective Correction** — Four-point warp transform for distortion-free output
- **Dual Processing Modes** — Scan mode (clean whitening) and OCR mode (sharp binarization) via Strategy pattern
- **Image Quality Validation** — Laplacian-based blur detection and overexposed pixel ratio glare check
- **ML Kit OCR** — Korean text recognition on captured documents
- **Custom Design System** — "Soft Monotone" theme built with Jetpack Compose

## Architecture Overview

The project is organized into three layers designed for cross-platform reuse:

- **C++ Core** — Platform-agnostic scanning engine built on OpenCV. Handles all image processing, contour detection, perspective correction, and post-processing.
- **Platform Bindings** — JNI bridge for Android. iOS (Objective-C++) and desktop bindings are scaffolded.
- **Android App** — Jetpack Compose UI with custom MVI architecture, Clean Architecture domain layer, and Hilt dependency injection.

## Project Structure

```
native-scanner/
├── cpp/                        # C++ core engine & platform bindings
│   ├── core/                   # Scanning engine (OpenCV)
│   ├── android/                # JNI bridge (ScannerWrapperJni.cpp)
│   ├── ios/                    # iOS bindings (planned)
│   └── desktop/                # Desktop parameter tuning GUI
├── android/                    # Android application
│   ├── app/                    # Main app module
│   ├── core/                   # Core library modules
│   └── build-system/           # Convention plugins (includeBuild)
├── ios/                        # iOS application (planned)
├── libs/                       # OpenCV prebuilt libraries
└── docs/                       # Specifications & documentation
```

---

## C++ Core

### Class Diagram

```mermaid
classDiagram
    class ScannerEngine {
        -ScannerConfig config_
        -LoggerCallback logger_
        -vector~Point2f~ prev_points_
        -mutex config_mutex_
        -int stable_frame_count_
        -unique_ptr~IPostprocessor~ scan_processor_
        -unique_ptr~IPostprocessor~ ocr_processor_
        +ScannerEngine(ScannerConfig)
        +updateConfig(ScannerConfig)
        +setLogger(LoggerCallback)
        +detectDocument(Mat, DocumentType, int) DocumentFrame
        +captureDocument(Mat, DocumentFrame, Size, ProcessingMode, DocumentType, int) CaptureResult
    }

    class ImagePreprocessor {
        +preprocess(Mat, DocumentType, ScannerConfig)$ Mat
    }

    class GeometryUtils {
        +findLargestArea(Mat, double)$ vector~Point2f~
        +orderPoints(vector~Point2f~)$ vector~Point2f~
        +smoothPoints(vector~Point2f~, vector~Point2f~, float)$ vector~Point2f~
    }

    class ImageUtils {
        +downscale(Mat, int)$ Mat
        +applyWarp(Mat, vector~Point2f~)$ Mat
        +rotateImage(Mat, int)$ Mat
    }

    class ImageValidator {
        +checkBlur(Mat, double)$ bool
        +checkGlare(Mat, double)$ bool
    }

    class IPostprocessor {
        <<interface>>
        +process(Mat, DocumentType)* Mat
    }

    class ScanProcessor {
        +process(Mat, DocumentType) Mat
    }

    class OcrProcessor {
        +process(Mat, DocumentType) Mat
    }

    ScannerEngine --> ImagePreprocessor : preprocess
    ScannerEngine --> GeometryUtils : contour detection
    ScannerEngine --> ImageUtils : transform
    ScannerEngine --> ImageValidator : quality check
    ScannerEngine --> IPostprocessor : post-process

    IPostprocessor <|.. ScanProcessor
    IPostprocessor <|.. OcrProcessor
```

### Document Detection Flow

Real-time processing pipeline for each camera preview frame:

```mermaid
flowchart LR
    A[Input Frame] --> B[Rotate]
    B --> C[Downscale]
    C --> D[Preprocess]
    D --> E[Find Contour]
    E --> F[Stability Check]
    F --> G[Scale Back]
    G --> H[DocumentFrame]
```

### Document Capture Flow

High-resolution processing pipeline triggered on shutter:

```mermaid
flowchart LR
    A[Full-res Image] --> B[Scale Points]
    B --> C[Perspective Warp]
    C --> D[Validate Quality]
    D --> E[Post-process]
    E --> F[CaptureResult]
```

### Desktop Tuning Tool

The `cpp/desktop/` module provides an OpenCV GUI application for real-time parameter tuning. It opens a webcam feed with trackbar sliders for blur, glare, canny, and target width parameters, enabling rapid iteration on detection thresholds without deploying to a mobile device.

---

## Android

### Project Structure

```
android/
├── app/                            # Main app module (UI, navigation, ViewModels)
├── core/
│   ├── model/                      # Data classes (DocumentFrame, CaptureResult, ScannerConfig, ...)
│   ├── domain/                     # Use cases & repository interfaces
│   ├── data/                       # Repository implementations & data source abstractions
│   ├── jni/                        # JNI bridge wrapper (NativeScanner.kt → libnative-scanner.so)
│   ├── designsystem/               # Custom Compose design system ("Soft Monotone" theme)
│   ├── datastore/                  # Preferences persistence (AndroidX DataStore)
│   └── ml/                         # ML Kit OCR integration (Korean text recognizer)
└── build-system/                   # Convention plugins (standalone includeBuild)
```

### Document Detection Sequence

```mermaid
sequenceDiagram
    participant Camera
    participant ViewModel
    participant DetectUseCase
    participant JNI
    participant ScannerEngine

    Camera->>ViewModel: Preview frame (Bitmap)
    ViewModel->>DetectUseCase: detectDocument()
    DetectUseCase->>JNI: nativeDetect()
    JNI->>ScannerEngine: detectDocument(Mat)
    ScannerEngine-->>JNI: DocumentFrame
    JNI-->>DetectUseCase: DocumentFrame
    DetectUseCase-->>ViewModel: DocumentFrame
    ViewModel-->>Camera: Draw detection overlay
```

### Document Capture Sequence

```mermaid
sequenceDiagram
    participant Camera
    participant ViewModel
    participant CaptureUseCase
    participant JNI
    participant ScannerEngine
    participant OcrUseCase

    Camera->>ViewModel: Shutter tap → high-res Bitmap
    ViewModel->>ViewModel: Crop edit (user adjusts corners)
    ViewModel->>CaptureUseCase: captureDocument()
    CaptureUseCase->>JNI: nativeCapture()
    JNI->>ScannerEngine: captureDocument(Mat)
    ScannerEngine-->>JNI: CaptureResult
    JNI-->>CaptureUseCase: CaptureResult
    CaptureUseCase-->>ViewModel: CaptureResult

    opt OCR Mode
        ViewModel->>OcrUseCase: recognizeText()
        OcrUseCase-->>ViewModel: OcrBlocks
    end

    ViewModel-->>Camera: Display result
```

### MVI Architecture

The app uses a custom MVI (Model-View-Intent) framework built on a generic `BaseViewModel`:

```mermaid
classDiagram
    class BaseState {
        <<interface>>
    }
    class BaseEvent {
        <<interface>>
    }
    class BaseEffect {
        <<interface>>
    }
    class BaseError {
        <<interface>>
    }

    class BaseViewModel~STATE, EVENT, EFFECT, ERROR~ {
        -MutableStateFlow~STATE~ _state
        -Channel~EVENT~ _event
        -MutableSharedFlow~EFFECT~ _effect
        -MutableSharedFlow~ERROR~ _error
        +state: StateFlow~STATE~
        +onEvent: (EVENT) → Unit
        #updateState(STATE.() → STATE)
        #emitEffect: suspend (EFFECT) → Unit
        #emitError: suspend (ERROR) → Unit
        #bindEvent(suspend (EVENT) → Unit)
        #bindError(suspend (ERROR) → Unit)
        +bindEffect(CoroutineScope, suspend (EFFECT) → Unit)
    }

    BaseViewModel --> BaseState : STATE
    BaseViewModel --> BaseEvent : EVENT
    BaseViewModel --> BaseEffect : EFFECT
    BaseViewModel --> BaseError : ERROR
```

**MVI Cycle:**

```mermaid
flowchart LR
    View -->|"onEvent()"| Event[EVENT Channel]
    Event -->|"bindEvent{}"| ViewModel[ViewModel]
    ViewModel -->|"updateState{}"| State[STATE Flow]
    ViewModel -->|"emitEffect()"| Effect[EFFECT Flow]
    State -->|"collect"| View
    Effect -->|"bindEffect{}"| View
```

| Channel | Type | Behavior |
|---|---|---|
| `STATE` | `MutableStateFlow` | Always holds current value, observers receive latest state on subscription |
| `EVENT` | `Channel` | Buffered, `DROP_OLDEST` backpressure — user intents never block the UI |
| `EFFECT` | `MutableSharedFlow` | 64-capacity buffer — one-shot side effects (navigation, toast) survive config changes |
| `ERROR` | `MutableSharedFlow` | 1-capacity buffer, `DROP_OLDEST` — dedicated error propagation stream |

### Multi-Module & Build System

#### Module Dependency Graph

```mermaid
graph TB
    app --> core:data
    app --> core:datastore
    app --> core:designsystem
    app --> core:domain
    app --> core:model
    app --> core:ml
    app --> core:jni

    core:jni --> core:data
    core:jni --> core:model

    core:data --> core:domain
    core:data --> core:model

    core:domain --> core:model

    core:datastore --> core:data
    core:datastore --> core:model

    core:ml --> core:model
    core:ml --> core:data
```

#### Build System Architecture

The project uses a **convention plugin** architecture via Gradle's `includeBuild` mechanism. The `build-system/` directory is a standalone Gradle project that compiles independently and provides 15 custom plugins to the main build.

```mermaid
flowchart TB
    subgraph build-system["build-system/ (includeBuild)"]
        direction TB
        Config["AndroidBuildConfig\n(compileSdk, targetSdk, minSdk,\nversionName, flavors, javaVersion)"]
        Extensions["Extensions\n(Android.kt, AndroidCompose.kt, Flavor.kt)"]
        Plugins["Convention Plugins\n(15 plugins)"]
        Config --> Extensions
        Extensions --> Plugins
    end

    subgraph VersionCatalog["gradle/libs.versions.toml"]
        Versions["Versions & Libraries"]
    end

    VersionCatalog --> build-system

    subgraph modules["Application Modules"]
        app["app"]
        model["core:model"]
        domain["core:domain"]
        data["core:data"]
        jni["core:jni"]
        ds["core:designsystem"]
        datastore["core:datastore"]
        ml["core:ml"]
    end

    Plugins -->|"applied via\nplugins { alias(...) }"| modules
```

Each module applies plugins declaratively — a single `alias(libs.plugins.scanner.android.library.compose)` replaces hundreds of lines of boilerplate configuration. Plugins compose on top of each other: for example, `scanner.android.library.compose` builds upon `scanner.android.library` by adding Compose compiler configuration.


## Screenshots

<!-- Add your screenshot images here -->
<!-- Example: ![Camera Preview](docs/screenshots/camera_preview.png) -->

| Camera Preview |
|:-:|
| *Add screenshot here* |

---

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.