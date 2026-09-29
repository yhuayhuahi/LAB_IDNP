plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.kotlin.plugin.compose")
	// id("org.jetbrains.kotlin.plugin.serialization") // Descomentar si usas @Serializable con Kotlinx Serialization
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
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")

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