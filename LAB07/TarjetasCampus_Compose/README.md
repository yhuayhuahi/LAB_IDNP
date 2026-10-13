# Laboratorio 06: Tarjetas y Navegación en Campus Navigator (Compose)

Aplicación desarrollada en Android con **Kotlin**, **Jetpack Compose** y **Material Design 3**, que integra diseño avanzado de tarjetas personalizadas con el flujo de navegación del campus universitario (LAB05).

---

## Cambios Implementados desde la Versión Base

A partir de la versión base inicial del laboratorio, se realizaron las siguientes mejoras:

1. **Integración de Navegación y Arquitectura Modular (LAB05 + LAB06):**
   - Se conectó la estructura de navegación con `Scaffold`, `CenterAlignedTopAppBar`, `NavigationBar` y `NavHost`.
   - El código se organizó de forma modular en paquetes: `model`, `components`, `screens`, `navigation` y `viewmodel`.

2. **Interactividad en las Tarjetas:**
   - Se envolvieron los componentes `TarjetaLugar` y `TarjetaCompacta` con `Modifier.clickable { ... }` para permitir la selección y navegación al detalle.

3. **Nueva Pantalla de Detalle (`DetalleLugarScreen`):**
   - Composable que muestra la información completa del edificio (imagen destacada ampliada, categoría, descripción detallada, ubicación y horario), reutilizando y expandiendo la estética de `TarjetaLugar`.

4. **Listado Completo en `EdificiosScreen`:**
   - Se reemplazó la lista simple de texto y botones por un `LazyColumn` que presenta los diferentes estilos de tarjetas con datos de 4 edificios del campus (Biblioteca Central, Comedor Universitario, Pabellón de Informática y Auditorio Principal).

5. **Tercer Estilo de Tarjeta Propio (`TarjetaInformativa` - Reto Opcional):**
   - Diseño personalizado que combina:
     - `Card` y `Surface` con bordes redondeados y sombra tonal.
     - `Box` para apilar la imagen con un filtro translúcido y una insignia (badge) flotante.
     - `Column` y `Row` para organizar la descripción y metadatos inferiores (horario y enlace a detalle).

6. **Iconos Vectoriales de Material Design:**
   - Se reemplazaron todos los emojis de la interfaz por iconos vectoriales oficiales de Material Design (`ic_home`, `ic_apartment`, `ic_map`, `ic_person`, `ic_place`, `ic_schedule`, `ic_arrow_back`, `ic_arrow_forward`).

---

## Estructura del Código

```text
com.example.tarjetascampus_compose/
├── MainActivity.kt          # Punto de entrada con Scaffold y rutas de navegación
├── model/
│   └── LugarCampus.kt       # Datos y catálogo de edificios del campus
├── components/
│   ├── TarjetaLugar.kt      # Estilo 1: Imagen circular + textos en fila
│   ├── TarjetaCompacta.kt   # Estilo 2: Imagen de fondo panorámica con scrim
│   ├── TarjetaInformativa.kt# Estilo 3: Badge flotante + metadatos (Reto)
│   └── ListaTarjetasLugar.kt# Lista modular de tarjetas
├── screens/
│   ├── DetalleLugarScreen.kt# Detalle a pantalla completa
│   ├── EdificiosScreen.kt   # Listado interactivo con tarjetas
│   ├── HomeScreen.kt        # Pantalla de bienvenida con último edificio visto
│   └── PlaceholderScreen.kt # Pantallas para Mapa y Perfil
├── navigation/
│   └── Screen.kt            # Definición de rutas y destinos
└── viewmodel/
    └── SeleccionViewModel.kt# Estado reactivo del edificio consultado
```

---

## Compilación y Ejecución

```bash
# Compilar la aplicación
./gradlew assembleDebug

# Instalar en dispositivo o emulador conectado
./gradlew installDebug
```