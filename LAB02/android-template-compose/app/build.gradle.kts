plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Activity adaptado para Compose sin dependencias de temas
    implementation("androidx.activity:activity-compose:1.9.2")

    // Compose BOM para sincronizar el núcleo gráfico
    val composeBom = platform("androidx.compose:compose-bom:2024.09.02")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // --- COMPOSE VANILLA (Sin Material Design) ---
    implementation("androidx.compose.ui:ui")                   // Motor básico (Canvas, Text, Modifiers)
    implementation("androidx.compose.foundation:foundation")   // Layouts base (Column, Row, Box, LazyColumn, Gestos)
    implementation("androidx.compose.runtime:runtime")         // Manejo de estado (remember, mutableStateOf)

    // Herramientas de previsualización en desarrollo
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
}