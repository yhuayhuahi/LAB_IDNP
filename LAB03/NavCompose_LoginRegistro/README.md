# Laboratorio: Aplicación de Login, Registro y Home con Jetpack Compose

## Integrantes
- Estudiante 1: [Huayhua Hillpa, Yourdyy Yossimar]
- Estudiante 2: [Quispe Saavedra, Dennis Javier]

---

## Resumen del Proyecto

Esta aplicación desarrollada en **Android con Jetpack Compose** implementa un flujo completo de autenticación y navegación entre pantallas, con persistencia de datos en el almacenamiento interno del dispositivo.

### Principales Funcionalidades Implementadas:

1. **Modularización y Arquitectura:**
   - Desacoplamiento de las pantallas en el paquete `com.example.app.ui.screens`: [`LoginScreen.kt`](app/src/main/kotlin/com/example/app/ui/screens/LoginScreen.kt), [`RegistroScreen.kt`](app/src/main/kotlin/com/example/app/ui/screens/RegistroScreen.kt) y [`HomeScreen.kt`](app/src/main/kotlin/com/example/app/ui/screens/HomeScreen.kt).
   - Manejo centralizado de navegación en [`MainActivity.kt`](app/src/main/kotlin/com/example/app/MainActivity.kt) mediante `NavHost`.

2. **Navegación con Argumentos (`HomeScreen`):**
   - Ruta parametrizada `home/{usuario}` que recibe el nombre de usuario autenticado y muestra el mensaje `"Bienvenido <usuario>"` con opción para cerrar sesión.

3. **Persistencia en Almacenamiento Interno (`cuentas.txt`):**
   - **Registro:** Guardado de credenciales (`usuario,contraseña`) mediante `openFileOutput("cuentas.txt", Context.MODE_APPEND)` y retorno a Login con mensaje de confirmación (`Toast`).
   - **Login:** Lectura y verificación de credenciales con `openFileInput("cuentas.txt")`. Si no coincide o no existe, muestra `"Cuenta no encontrada"`.

4. **Validación de Formularios:**
   - Control de campos vacíos en Login y Registro con alertas visuales de error (`isError`) y mensajes descriptivos.

---

## Compilación y Ejecución

Compilar el proyecto sin daemon de Gradle:

```bash
./gradlew assembleDebug --no-daemon
```

Instalar en un dispositivo o emulador conectado:

```bash
./gradlew installDebug --no-daemon
```
