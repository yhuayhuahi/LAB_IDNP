# Campus Navigator Compose

Aplicación Android para el Laboratorio 5 de IDNP, desarrollada con Kotlin, Jetpack Compose, Material 3 y Navigation Compose.

## Funcionalidad

- Cuatro destinos en una barra inferior: Inicio, Edificios, Mapa y Perfil.
- Lista de cuatro edificios con acción para seleccionar uno.
- La pantalla Inicio muestra el último edificio seleccionado.
- Mapa y Perfil incluyen pantallas de marcador de posición.
- API 34 como `compileSdk` y `targetSdk`; `minSdk` 24.

## ViewModel compartido

`MainScreen` obtiene una instancia de `SeleccionViewModel` mediante `viewModel()` y la entrega tanto a Inicio como a Edificios. Al pulsar “Ver”, Edificios actualiza `edificioSeleccionado`, una propiedad `mutableStateOf`; Compose observa el estado y actualiza la interfaz. Al volver a Inicio se muestra el mismo valor, sin una lambda de comunicación entre esas pantallas.

Una lambda resulta apropiada para comunicar un evento puntual y mantener el estado en el padre. Un ViewModel compartido es conveniente cuando varias pantallas necesitan leer o actualizar el mismo estado de pantalla y se quiere conservarlo durante cambios de configuración. No es una regla que el ViewModel sea siempre mejor: para flujos pequeños, elevar el estado y pasar lambdas suele ser más simple. Para sobrevivir a la terminación del proceso se necesita guardar el estado, por ejemplo con `SavedStateHandle` o almacenamiento persistente.

## Compilar y ejecutar

El proyecto se compila desde VS Code con Gradle Wrapper; no requiere Android Studio. Necesitas JDK 17 o superior, Android SDK con la plataforma 34 y Build Tools 34.0.0, y un emulador o dispositivo para instalar la app.

### Configuración única de Java en Windows

No hace falta instalar Android Studio. En las variables de entorno de Windows, configura una sola vez `JAVA_HOME` de usuario con la carpeta real de un JDK 17 o superior y agrega `%JAVA_HOME%\bin` al `Path` de usuario. Si ya existe un `JAVA_HOME` de sistema que apunta a una ruta inválida, corrígelo o elimínalo. Cierra todas las ventanas de VS Code y vuelve a abrirlo para que sus terminales carguen el cambio. Comprueba el JDK que Gradle usará con `.\gradlew.bat --version`; debe mostrar JVM 17 o superior.

El SDK se resuelve mediante `local.properties`, así que no necesitas exportar sus rutas en cada comando. En este workspace ese archivo apunta a `C:/Android/Sdk` y está excluido de Git. Si clonas el proyecto en otro equipo, crea o actualiza `local.properties` una sola vez con la ruta local de su SDK.

Desde la carpeta raíz del proyecto, compila e instala con PowerShell:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
```

`installDebug` requiere un emulador iniciado o un dispositivo conectado con depuración USB. Comprueba la conexión con `adb devices`. El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`.

La app puede ejecutarse en Android API 24 o superior; para esta configuración de compilación se usa `compileSdk 34`.

## Evidencia para el informe

Prueba las cuatro pestañas y selecciona un edificio antes de volver a Inicio. Incluye en el informe capturas de cada pestaña y una captura adicional que muestre el edificio reflejado en Inicio, junto con una breve descripción de la instancia compartida del ViewModel. Las capturas deben obtenerse al ejecutar la app en el emulador o dispositivo usado para la práctica.