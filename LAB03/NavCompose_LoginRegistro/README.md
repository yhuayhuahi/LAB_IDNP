# Android Template Compose

Plantilla mínima de Android con Kotlin y Jetpack Compose. El proyecto usa un único módulo, `app`, y Compose sin Material Design.

## Requisitos

- JDK 17.
- Android SDK con API 34.
- Un dispositivo Android o emulador para ejecutar la aplicación.

No es necesario instalar Gradle de forma global: el proyecto incluye su propio **Gradle Wrapper** (`gradlew`). El wrapper descarga y usa la versión de Gradle definida en `gradle/wrapper/gradle-wrapper.properties`, lo que mantiene los builds reproducibles.

## Inicializar y compilar

Desde la raíz del proyecto, da permisos de ejecución al wrapper si es necesario:

```bash
chmod +x ./gradlew
```

Para compilar la variante de debug usando el wrapper y ahorrar memoria, desactiva el daemon de Gradle:

```bash
./gradlew assembleDebug --no-daemon
```

El APK se genera normalmente en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

`--no-daemon` hace que Gradle no deje un proceso persistente en segundo plano. Puede hacer cada compilación un poco más lenta, pero reduce el consumo de RAM cuando se trabaja en equipos con recursos limitados.

## Instalar y ejecutar

Con un dispositivo o emulador conectado y visible mediante `adb`, instala la variante debug con:

```bash
./gradlew installDebug --no-daemon
```

Después, abre la aplicación desde el launcher de Android. Para comprobar que el dispositivo está conectado:

```bash
adb devices
```

También se puede compilar e instalar en una sola invocación:

```bash
./gradlew assembleDebug installDebug --no-daemon
```

## Estructura de Jetpack Compose

```text
android-template-compose/
├── app/
│   ├── build.gradle.kts                  # Plugins, SDK y dependencias
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml       # Componentes, permisos y configuración
│       │   ├── kotlin/com/example/app/   # Código Kotlin y UI Compose
│       │   │   ├── MainActivity.kt       # Activity que inicia setContent { }
│       │   │   ├── ui/                   # Pantallas, componentes y tema visual
│       │   │   │   ├── components/       # Composables reutilizables
│       │   │   │   ├── screens/          # Una carpeta por pantalla o flujo
│       │   │   │   ├── navigation/       # Rutas y NavHost, si se añade navegación
│       │   │   │   └── theme/            # Colores, tipografías y formas
│       │   │   ├── data/                 # Repositorios, fuentes de datos y modelos DTO
│       │   │   ├── domain/               # Casos de uso y modelos de negocio, opcional
│       │   │   └── di/                   # Inyección de dependencias, opcional
│       │   │
│       │   └── res/                      # Recursos Android que no son código Kotlin
│       │       ├── drawable/              # Imágenes, vectores y drawables XML
│       │       ├── drawable-night/        # Drawables alternativos para modo oscuro
│       │       ├── mipmap-*/              # Iconos del launcher en distintas densidades
│       │       ├── values/                # strings.xml, colors.xml, themes.xml, dimens.xml
│       │       ├── values-es/             # Valores localizados para español
│       │       ├── values-night/          # Valores alternativos para modo oscuro
│       │       ├── font/                  # Fuentes incluidas en la aplicación
│       │       ├── raw/                   # Archivos sin procesar: JSON, audio, etc.
│       │       ├── xml/                   # Configuraciones XML, por ejemplo FileProvider
│       │       └── navigation/            # Gráficos de navegación XML, si se usan
│       │
│       ├── test/                         # Tests unitarios ejecutados en la JVM
│       │   └── kotlin/com/example/app/
│       └── androidTest/                  # Tests instrumentados en dispositivo/emulador
│           └── kotlin/com/example/app/
├── gradle/wrapper/
│   └── gradle-wrapper.properties        # Versión de Gradle del wrapper
├── build.gradle.kts                      # Configuración raíz y plugins compartidos
├── gradle.properties                     # Propiedades globales de Gradle
├── settings.gradle.kts                   # Nombre del proyecto y módulos incluidos
├── gradlew                               # Wrapper para Linux/macOS
└── gradlew.bat                           # Wrapper para Windows
```

### Dónde colocar la UI

En Jetpack Compose, la UI se escribe principalmente en archivos Kotlin dentro de `app/src/main/kotlin`. No se coloca en `res/layout` como ocurría con las vistas XML tradicionales.

Una organización habitual es:

```text
kotlin/com/example/app/
├── MainActivity.kt
└── ui/
	├── screens/
	│   ├── home/HomeScreen.kt
	│   └── settings/SettingsScreen.kt
	├── components/
	│   ├── AppButton.kt
	│   └── AppTopBar.kt
	├── navigation/AppNavHost.kt
	└── theme/
		├── Color.kt
		├── Theme.kt
		└── Type.kt
```

Cada archivo de pantalla o componente suele contener funciones marcadas con `@Composable`. Por ejemplo, `HomeScreen.kt` puede declarar `HomeScreen()`, mientras que `AppButton.kt` puede declarar un botón reutilizable. `MainActivity` debería encargarse principalmente de conectar Android con Compose mediante `setContent { App() }`; la UI crece mejor cuando las pantallas y componentes se mantienen fuera de la Activity.

En esta plantilla todavía todo el ejemplo vive en `MainActivity.kt` y utiliza `Box`, `BasicText` y `Modifier`. Cuando aumente la aplicación, se puede mover ese contenido a `ui/screens/` sin cambiar la idea principal de Compose.

### Qué va dentro de `res`

La carpeta `res` contiene recursos que Android puede cargar por identificador, por ejemplo `R.string.app_name` o `R.drawable.logo`. Aunque una pantalla Compose se escriba en Kotlin, sigue utilizando `res` para recursos compartidos, localización y archivos específicos de Android:

- `res/values/strings.xml`: textos visibles para poder traducirlos. En Compose se leen con `stringResource(R.string.nombre)`, en lugar de escribir textos fijos cuando deban localizarse.
- `res/values/colors.xml`: colores XML que necesiten otros componentes Android. Los colores propios del tema Compose suelen vivir en `ui/theme/Color.kt`.
- `res/values/themes.xml`: tema base de la Activity y configuración Android heredada. No sustituye necesariamente al tema Compose.
- `res/drawable/`: imágenes, iconos vectoriales y fondos. En Compose se cargan con `painterResource(R.drawable.nombre)` o `vectorResource(...)`.
- `res/mipmap/`: iconos de launcher. No suele ser la ubicación para imágenes normales de la UI.
- `res/font/`: fuentes locales, que pueden cargarse desde el tema Compose.
- `res/xml/`: configuraciones que exigen XML, como reglas de backup, `FileProvider` o preferencias.
- `res/raw/`: archivos que se quieren leer sin que Android los transforme, como JSON, audio o vídeo.

Con Compose normalmente no se crea `res/layout`. Esa carpeta solo es necesaria si se mantiene alguna pantalla o vista basada en XML, o si una librería la requiere. Tampoco se deben guardar clases Kotlin dentro de `res`: el código va en `kotlin/com/example/app`.

### Organización del código por responsabilidad

Para una aplicación pequeña, `ui/screens` y `ui/components` pueden ser suficientes. Al crecer el proyecto, una separación frecuente es:

- `ui/`: estado visual, pantallas, componentes reutilizables, navegación y tema.
- `data/`: acceso a red, base de datos, preferencias y repositorios. La UI no debería conocer los detalles de Retrofit, Room o almacenamiento.
- `domain/`: reglas de negocio y casos de uso cuando la aplicación los necesite. Esta capa es opcional para proyectos pequeños.
- `di/`: módulos de inyección de dependencias, por ejemplo con Hilt o Koin.
- `test/`: lógica que puede probarse sin Android, como validaciones, ViewModels o casos de uso.
- `androidTest/`: pruebas que necesitan un dispositivo o emulador, como pruebas de interacción Compose.

Los `ViewModel` suelen colocarse junto a la pantalla o en un paquete `ui/.../viewmodel`, y exponen estado observable para que los composables lo representen. La pantalla recibe ese estado y eventos; así se evita colocar lógica de negocio compleja directamente dentro del bloque `@Composable`.

## Comandos útiles

```bash
# Ver las tareas disponibles
./gradlew tasks --no-daemon

# Limpiar compilaciones anteriores y volver a compilar
./gradlew clean assembleDebug --no-daemon

# Desinstalar la aplicación debug del dispositivo conectado
./gradlew uninstallDebug --no-daemon
```