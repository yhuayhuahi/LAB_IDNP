# Android Template Compose + Material 3

Plantilla moderna, ligera y de máxima compatibilidad para Android con **Kotlin**, **Jetpack Compose** y **Material Design 3 (Material You)**.

Diseñada para servir como base limpia y lista para producción al iniciar proyectos en Android (API 34 / Android 14) con soporte de arquitectura moderna, cliente HTTP/HTTPS y librerías esenciales bien balanceadas.

---

## Características Incluidas

- **Jetpack Compose + Material Design 3**:
  - `MaterialTheme` preconfigurado con soporte para Modo Oscuro y Colores Dinámicos (Android 12+).
  - Componentes Material 3: `Scaffold`, `CenterAlignedTopAppBar`, `ElevatedCard`, `Button`, `Text`.
  - Compose BOM (`2024.09.02`) para sincronización garantizada de versiones de Compose.
- **Arquitectura y Ciclo de Vida**:
  - `lifecycle-viewmodel-compose` y `lifecycle-runtime-compose` para manejo reactivo con `collectAsStateWithLifecycle()`.
- **Navegación**:
  - `navigation-compose` con soporte para Type-Safe Navigation (rutas seguras con Kotlinx Serialization).
- **Red y Cliente HTTP/HTTPS**:
  - `Retrofit 2` + `OkHttp 3` + `logging-interceptor`: El estándar más robusto, compatible y probado en Android.
  - `kotlinx.serialization` + conversor Retrofit oficial: Serialización JSON moderna, sin reflexión en tiempo de ejecución y súper ligera.
  - Permiso `android.permission.INTERNET` configurado en el Manifest.
- **Carga de Imágenes**:
  - `Coil Compose`: Creada nativamente para Compose, basada en corrutinas y OkHttp, ligera y sin el overhead de librerías antiguas.

---

## Compatibilidad y Versiones Exactas

Siguiendo las recomendaciones LTS de máxima compatibilidad sin advertencias ni conflictos de herramientas:

| Herramienta / Librería | Versión |
| :--- | :--- |
| **Target SDK / Compile SDK** | API 34 (Android 14) |
| **Min SDK** | API 24 (Android 7.0 - cubre >96% de dispositivos) |
| **Android Gradle Plugin (AGP)** | 8.5.2 |
| **Gradle** | 8.7 |
| **Kotlin** | 2.0.21 (con plugin oficial de Compose compiler) |
| **JDK** | Java 17 |
| **Compose BOM** | 2024.09.02 |
| **Retrofit / OkHttp** | 2.11.0 / 4.12.0 |
| **Coil Compose** | 2.7.0 |

---

## Estructura del Proyecto

```text
android-template-compose-material/
├── app/
│   ├── build.gradle.kts                  # Configuración de SDK, plugins y dependencias
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml       # Declaración de componentes y permisos (INTERNET)
│       │   └── kotlin/com/example/app/
│       │       ├── MainActivity.kt       # Activity principal con interfaz Material 3
│       │       └── ui/
│       │           └── theme/            # Sistema de diseño Material 3
│       │               ├── Color.kt      # Paletas de color Light / Dark
│       │               ├── Theme.kt      # AppTheme con Dynamic Color
│       │               └── Type.kt       # Tipografías base
│       └── ...
├── gradle/wrapper/
│   └── gradle-wrapper.properties         # Configuración Gradle 8.7
├── build.gradle.kts                      # Build script raíz
├── settings.gradle.kts                   # Repositorios y resolución de plugins
├── gradlew                               # Wrapper para Linux/macOS
└── gradlew.bat                           # Wrapper para Windows
```

---

## Inicializar y Compilar

Da permisos de ejecución al wrapper:

```bash
chmod +x ./gradlew
```

Compila la variante de debug:

```bash
./gradlew assembleDebug --no-daemon
```

El APK resultante se generará en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

### Instalar y Ejecutar en un dispositivo/emulador:

```bash
./gradlew installDebug --no-daemon
```

Comprobar dispositivos conectados:

```bash
adb devices
```