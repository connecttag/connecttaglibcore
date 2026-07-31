# connecttaglibcore

Core library for ConnectTag Android projects, providing centralized Settings screens, theming capabilities, and "About App" components.

## Features

- **Settings Screen:** Ready-to-use Compose settings UI with color seed selection and theme mode switching.
- **Dynamic Theming:** Support for Material 3 dynamic colors and custom seed colors.
- **About App:** Flexible components to display app information and social links.
- **Multi-language:** Built-in support for English and Arabic.

## Installation

### 1. Add JitPack to your project
Add it in your root `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Add the dependency
Add it to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.connecttagye:connecttaglibcore:1.0.0")
}
```

## Usage

### Displaying the Settings Screen

```kotlin
import org.connecttag.lib.kotlin.core.settings.SettingsScreen

@Composable
fun MyNavigation() {
    // ...
    composable("settings") {
        SettingsScreen()
    }
}
```

### Using Theme Settings

```kotlin
import org.connecttag.lib.kotlin.core.theme.ThemeSettings
import org.connecttag.lib.kotlin.core.theme.ThemeMode

// Example: Check current mode
val currentMode = ThemeSettings.themeMode
```

## License

```
Copyright 2026 ConnectTag

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
