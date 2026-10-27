plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
	namespace = "com.example.persistenciaroom_compose"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.example.persistenciaroom_compose"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	buildFeatures {
		compose = true
	}
}

dependencies {
	// Activity adaptado para Jetpack Compose
	implementation("androidx.activity:activity-compose:1.9.2")

	// Compose BOM para sincronizar versiones del ecosistema Compose
	val composeBom = platform("androidx.compose:compose-bom:2024.09.02")
	implementation(composeBom)
	androidTestImplementation(composeBom)

	// --- COMPOSE CORE ---
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.foundation:foundation")
	implementation("androidx.compose.runtime:runtime")

	// --- MATERIAL DESIGN 3 ---
	implementation("androidx.compose.material3:material3")
	implementation("androidx.compose.material:material-icons-core")

	// --- PREVIEW & TOOLING (Desarrollo) ---
	implementation("androidx.compose.ui:ui-tooling-preview")
	debugImplementation("androidx.compose.ui:ui-tooling")

	// --- LIFECYCLE & VIEWMODEL (Arquitectura recomendada en Compose) ---
	// Necesaria para viewModel(factory = ...) en Compose
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")

	// Room: las tres líneas deben llevar la MISMA versión (2.7.0 o superior)
	implementation("androidx.room:room-runtime:2.7.2")
	implementation("androidx.room:room-ktx:2.7.2")
	ksp("androidx.room:room-compiler:2.7.2")

	// --- NAVEGACIÓN TIPO-SEGURA (Type-Safe Navigation) ---
	implementation("androidx.navigation:navigation-compose:2.8.1")

	// --- SERIALIZACIÓN JSON (Oficial de Kotlin, ligera y sin reflexión) ---
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

	// --- CLIENTE HTTP/HTTPS Y NETWORKING (Retrofit + OkHttp) ---
	implementation("com.squareup.retrofit2:retrofit:2.11.0")
	implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")
	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

	// --- CARGA DE IMÁGENES ASÍNCRONAS (Coil para Compose) ---
	implementation("io.coil-kt:coil-compose:2.7.0")
}