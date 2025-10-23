# Todo KMP - Kotlin Multiplatform Todo Application

A modern, feature-rich Todo application built with Kotlin Multiplatform (KMP) that runs on both Android and iOS platforms. This project demonstrates best practices in cross-platform mobile development using Clean Architecture, MVVM pattern, and modern Android/iOS development practices.

## 🚀 Features

### Core Functionality
- ✅ Create, read, update, and delete todos
- 📅 Set due dates and times for tasks
- 🔔 Set reminders with notification support
- 📊 Organize tasks into "Today" and "Later" sections
- 📜 View completed tasks history
- 🔍 Search functionality for todos
- ↩️ Undo delete functionality with Snackbar

### Platform-Specific Features
- **Android**: Native notifications with AlarmManager
- **iOS**: Platform-specific notification handling
- **Cross-platform**: Shared business logic and UI using Compose Multiplatform

### Technical Features
- 🏗️ Clean Architecture with separation of concerns
- 🗄️ Room database with SQLite for data persistence
- 💉 Dependency Injection with Koin
- 🎨 Modern Material Design 3 UI with Jetpack Compose
- 🔄 Reactive programming with Kotlin Coroutines and Flow
- 🕐 Date/time handling with kotlinx-datetime
- 📱 Responsive UI with adaptive layouts

## 🏗️ Architecture

This project follows **Clean Architecture** principles with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   UI/Views  │  │ ViewModels  │  │   Navigation        │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                     Domain Layer                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   Models    │  │  Use Cases  │  │   Repository        │  │
│  │             │  │             │  │   Interfaces        │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                      Data Layer                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   Entities  │  │   DAOs      │  │ Repository Impl     │  │
│  │             │  │             │  │                     │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Module Structure

```
todo-kmp/
├── androidApp/          # Android application
├── iosApp/             # iOS application
└── shared/             # Shared KMP module
    ├── commonMain/     # Common code for all platforms
    ├── androidMain/    # Android-specific implementations
    └── iosMain/        # iOS-specific implementations
```

## 🛠️ Technology Stack

### Core Technologies
- **Kotlin Multiplatform**: Cross-platform development
- **Jetpack Compose Multiplatform**: Declarative UI framework
- **Room Database**: Local data persistence
- **SQLite**: Database engine
- **Kotlin Coroutines**: Asynchronous programming
- **Kotlinx DateTime**: Date/time utilities

### Dependency Injection
- **Koin**: Lightweight dependency injection framework

### UI/UX
- **Material Design 3**: Modern design system
- **Compose Navigation**: Navigation component
- **Compose Animations**: Smooth transitions

### Platform-Specific
- **Android**: AlarmManager, NotificationManager
- **iOS**: Platform-specific notification handling

## 📋 Requirements

### Development Environment
- **Android Studio**: Hedgehog | 2023.1.1 or later
- **Xcode**: 15.0 or later (for iOS development)
- **JDK**: 18 or later
- **Kotlin**: 2.1.20
- **Gradle**: 8.9.1

### Runtime Requirements
- **Android**: API level 21 (Android 5.0) or later
- **iOS**: iOS 15.0 or later

## 🚀 Getting Started

### Prerequisites
1. Install Android Studio
2. Install Xcode (for iOS development)
3. Set up JDK 18 or later
4. Clone the repository

### Building the Project

#### Android
1. Open the project in Android Studio
2. Wait for Gradle synchronization to complete
3. Select the `androidApp` configuration
4. Click Run or use the shortcut `Ctrl+R` (Windows/Linux) or `Cmd+R` (macOS)

#### iOS
1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Wait for the project to load
3. Select a simulator or device
4. Click the Run button

### Command Line Build

```bash
# Build Android
./gradlew :androidApp:assembleDebug

# Build iOS (requires Xcode)
cd iosApp
xcodebuild -project iosApp.xcodeproj -scheme iosApp -destination 'platform=iOS Simulator,name=iPhone 15' build
```

## 📁 Project Structure

### Shared Module (`shared/`)

#### Common Code (`commonMain/`)
- **`app/`**: Application entry point and navigation
- **`presentation/`**: UI components, screens, and view models
- **`domain/`**: Business logic, use cases, and models
- **`data/`**: Data layer with repositories and database
- **`di/`**: Dependency injection setup
- **`util/`**: Utility classes and helpers

#### Platform-Specific Code
- **`androidMain/`**: Android-specific implementations
  - Database setup
  - Notification services
  - Platform-specific utilities
- **`iosMain/`**: iOS-specific implementations
  - Database setup
  - Notification services
  - Platform-specific utilities

### Platform Applications
- **`androidApp/`**: Android application with Compose UI
- **`iosApp/`**: iOS application with SwiftUI wrapper

## 🔧 Key Components

### Data Layer
- **`TodoEntity`**: Database entity for todos
- **`TodoDao`**: Data Access Object for database operations
- **`TodoRepository`**: Repository interface and implementation
- **Room Database**: Local SQLite database with KMP support

### Domain Layer
- **`Todo`**: Domain model for todos
- **Use Cases**: Individual business logic operations
  - `GetTodayTodosUseCase`
  - `SaveTodoUseCase`
  - `DeleteTodoUseCase`
  - And more...

### Presentation Layer
- **`MainScreen`**: Main todo list screen
- **`DetailScreen`**: Todo creation/editing screen
- **`HistoryScreen`**: Completed tasks screen
- **`ViewModels`**: State management with MVVM pattern

### Services
- **`NotificationService`**: Cross-platform notification handling
- **`DateTimeWrapper`**: Platform-specific date/time utilities

## 📱 App Features in Detail

### Todo Management
- Create new todos with title, description, due date, and reminder
- Edit existing todos
- Mark todos as complete/incomplete
- Delete todos with undo functionality
- Search todos by title or description

### Notification System
- Schedule reminders for todos
- Cancel notifications when todos are deleted or completed
- Platform-specific notification handling
- Proper permission management

### User Interface
- Material Design 3 with dynamic theming
- Smooth animations and transitions
- Responsive layouts for different screen sizes
- Intuitive navigation between screens
- Loading states and error handling

## 🔔 Notification Permissions

### Android
The app automatically requests notification permissions on Android 13+ (API 33+) when needed.

### iOS
Notification permissions are handled through the iOS notification system.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
