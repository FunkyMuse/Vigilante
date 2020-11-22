# Vigilante

[![Kotlin Version](https://img.shields.io/badge/kotlin-1.4.10-blue.svg)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![GitHub release (including pre-releases)](https://img.shields.io/github/v/release/CraZyLegenD/Vigilante?include_prereleases)](https://github.com/CraZyLegenD/Vigilante/releases/latest)
![GitHub Repo Size](https://img.shields.io/github/repo-size/CraZyLegenD/Vigilante)


An app that focuses on your privacy and alerts you when a third-party application uses your device camera or microphone, plus few other goodies.

## Features
- Notifications when microphone/camera is used
- Screen dots when microphone/camera is used
- Screen dots customizations when microphone/camera is useed (size, color, screen position)
- History when apps requested permissions
- History when you disconnected/connected your charger
- History when you connected/disconnected your headphones
- History of your notifications
- Lock screen history
- Security sensible device informations
- No useless permissions
- Does not connect to the Internet
- All of your data is encrypted (database and preferences)
- Dark mode/light mode
- Built with many amazing open source libraries such as ([Kotlin extensions and helpers](https://github.com/FunkyMuse/KAHelpers), [Crashy](https://github.com/FunkyMuse/Crashy), [SQL Cipher](https://github.com/sqlcipher/android-database-sqlcipher), [Color picker](https://github.com/skydoves/ColorPickerView))
- and many more...

## Download

Go to the [Releases](https://github.com/CraZyLegenD/Vigilante/releases/latest) to download the latest APK.

## Screenshots

#### Light

<img src="https://raw.githubusercontent.com/CraZyLegenD/Vigilante/master/fastlane/metadata/en-US/images/phoneScreenshots/screen_1.png" width="33%"> </img><img src="https://raw.githubusercontent.com/CraZyLegenD/Vigilante/master/fastlane/metadata/en-US/images/phoneScreenshots/screen_2.png" width="33%"> </img><img src="https://raw.githubusercontent.com/CraZyLegenD/Vigilante/master/fastlane/metadata/en-US/images/phoneScreenshots/screen_3.png" width="33%"></img> <img src="https://raw.githubusercontent.com/CraZyLegenD/Vigilante/master/fastlane/metadata/en-US/images/phoneScreenshots/screen_4.png" width="33%"></img> <img src="https://raw.githubusercontent.com/CraZyLegenD/Vigilante/master/fastlane/metadata/en-US/images/phoneScreenshots/screen_5.png" width="33%"></img> <img src="https://raw.githubusercontent.com/CraZyLegenD/Vigilante/master/fastlane/metadata/en-US/images/phoneScreenshots/screen_6.png" width="33%"></img>
#### Dark

<img src="https://raw.githubusercontent.com/CraZyLegenD/Vigilante/master/fastlane/metadata/en-US/images/phoneScreenshots/screen_7.png" width="33%"></img> <img src="https://raw.githubusercontent.com/CraZyLegenD/Vigilante/master/fastlane/metadata/en-US/images/phoneScreenshots/screen_8.png" width="33%"></img> <img src="https://raw.githubusercontent.com/CraZyLegenD/Vigilante/master/fastlane/metadata/en-US/images/phoneScreenshots/screen_9.png" width="33%"></img> <img src="https://raw.githubusercontent.com/CraZyLegenD/Vigilante/master/fastlane/metadata/en-US/images/phoneScreenshots/screen_10.png" width="33%"></img> <img src="https://raw.githubusercontent.com/CraZyLegenD/Vigilante/master/fastlane/metadata/en-US/images/phoneScreenshots/screen_11.png" width="33%"></img> <img src="https://raw.githubusercontent.com/CraZyLegenD/Vigilante/master/fastlane/metadata/en-US/images/phoneScreenshots/screen_12.png" width="33%"></img>

## Translations
🇪🇸 [@miguelcanosantana](https://github.com/miguelcanosantana)

## Built With 🛠

Some of the popular libraries and MVVM clean architecture used with Room database as a source

* [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.

* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - Threads on steroids for Kotlin
* [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
* [StateFlow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/) - A live data replacement

* [Android JetPack](https://developer.android.com/jetpack) - Collection of libraries that help you design robust, testable, and maintainable apps.
  * [Paging3](https://developer.android.com/topic/libraries/architecture/paging) - Load and display small chunks of data at a time.
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
  * [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - A robust replacement for findViewById, kotlin sytnhetics and DataBinding
  * [Navigation Components](https://developer.android.com/guide/navigation/navigation-getting-started) - Navigate fragments as never easier before
  * [SavedStateHandle](https://developer.android.com/reference/androidx/lifecycle/SavedStateHandle) - A handle to saved state passed down to androidx.lifecycle.ViewModel. 
  * [Room](https://developer.android.google.cn/jetpack/androidx/releases/room) - Persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
  * [Biometrics](https://developer.android.com/jetpack/androidx/releases/biometric) - Authenticate with biometrics or device credentials, and perform cryptographic operations. 
  * [Security](https://developer.android.com/jetpack/androidx/releases/security) - Safely manage keys and encrypt files and sharedpreferences. 
  * [Start-up](https://developer.android.com/jetpack/androidx/releases/startup) - Implement a straightforward, performant way to initialize components at app startup such as our crash reporting library [Crashy](https://github.com/FunkyMuse/Crashy).
* [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
* [Dependency Injection](https://developer.android.com/training/dependency-injection) -
  * [Hilt-Dagger](https://dagger.dev/hilt/) - Standard library to incorporate Dagger dependency injection into an Android application.
  * [Hilt-ViewModel](https://developer.android.com/training/dependency-injection/hilt-jetpack) - DI for injecting `ViewModel`.

* Architecture
    * Clean Architecture
    * MVVM 
    * Offline with Room + SQL Cipher
* Tests - TBA

## Find this repository useful? ❤️

Support it by joining [stargazers](https://github.com/FunkyMuse/Vigilante/stargazers) for this repository. 🌠

And [follow me](https://github.com/FunkyMuse) or check my [blog](https://www.crazylegend.dev/) for my next creations! ⭐

## Contributions

Feature requests and translations are welcome

## License
[GNU General Public License v3.0](https://github.com/FunkyMuse/Vigilante/blob/master/LICENSE)


