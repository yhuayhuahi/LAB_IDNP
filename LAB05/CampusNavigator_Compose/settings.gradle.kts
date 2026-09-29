pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
  plugins {
    // Esta plantilla compila contra API 34. API 36 requiere una combinación compatible de AGP y Gradle.
    id("com.android.application") version "8.5.2"
    id("org.jetbrains.kotlin.android") version "2.0.21"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" // Nuevo estándar para Compose
    // id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" // Descomentar si usas Kotlinx Serialization
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "CampusNavigator_Compose"
include(":app")