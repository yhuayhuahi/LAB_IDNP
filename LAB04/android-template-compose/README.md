# BatteryMonitor_Compose

## Instrucciones de uso

### 1. Compilación e instalación con tu clásico Gradle

Sí, como ya dejamos configurado el wrapper (`gradlew`), puedes compilar e instalar directamente desde la raíz del proyecto (`android-template-compose`) con:

```bash
./gradlew assembleDebug installDebug --no-daemon
```

Una vez instalada, ábrela en tu teléfono (aparecerá con el nombre `BatteryMonitor_Compose`).

---

### 2. Simular señales de batería en un dispositivo físico con ADB

Abre una terminal y ejecuta lo siguiente mientras tu app está abierta en la pantalla del teléfono:

#### Paso A: Simular que desconectas el cargador (necesario en algunos modelos)
```bash
adb shell dumpsys battery unplug
```

#### Paso B: Cambiar el nivel de batería a cualquier valor (0 - 100)
Al ejecutar estos comandos, el sistema operativo enviará el broadcast `Intent.ACTION_BATTERY_CHANGED` exactamente igual que si cambiara físicamente:

* Simular **85%**:
  ```bash
  adb shell dumpsys battery set level 85
  ```
* Simular **15%**:
  ```bash
  adb shell dumpsys battery set level 15
  ```
* Simular **50%**:
  ```bash
  adb shell dumpsys battery set level 50
  ```
Verás cómo el texto `"Batería: X%"` en la pantalla de tu móvil cambia al instante.

#### Paso C: RESTAURAR la batería real (¡Muy importante!)
Cuando termines de probar, para que tu teléfono vuelva a leer la batería física real del hardware, ejecuta:
```bash
adb shell dumpsys battery reset
```

---

### 3. Verificar el Logcat (Paso 10)

Para ver los logs que imprimimos (`"Receiver registrado"` y `"Receiver desregistrado"`):

* **Desde Android Studio**: en la pestaña inferior **Logcat**, pon en el filtro:
  ```text
  tag:BatteryScreen
  ```
* **O directamente desde la terminal con adb**:
  ```bash
  adb logcat -s BatteryScreen
  ```

Cuando abras la aplicación verás:
```text
D/BatteryScreen: Receiver registrado
```
Y si sales o cierras la app:
```text
D/BatteryScreen: Receiver desregistrado
```