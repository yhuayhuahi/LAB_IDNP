pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
  plugins {
    // Para Plantilla 1 (API 34): agp = "8.5.2", kotlin = "2.0.21"
    // Para Plantilla 2 (API 36): agp = "8.9.0", kotlin = "2.1.0"
    id("com.android.application") version "8.5.2"
    id("org.jetbrains.kotlin.android") version "2.0.21"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" // Nuevo estándar para Compose
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "BatteryMonitor_Compose"
include(":app")