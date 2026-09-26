package com.example.batterymonitor_compose

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.app.PendingIntent
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.batterymonitor_compose.ui.theme.BatteryMonitor_ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BatteryMonitor_ComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        BatteryScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun BatteryScreen() {
    var porcentaje by remember { mutableStateOf(0) }
    var estaCargando by remember { mutableStateOf(false) }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        fun actualizarEstado(intent: Intent?) {
            val nivel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val escala = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            if (nivel != -1 && escala != -1) {
                porcentaje = (nivel * 100) / escala
            }

            val estado = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            estaCargando = estado == BatteryManager.BATTERY_STATUS_CHARGING ||
                estado == BatteryManager.BATTERY_STATUS_FULL
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                actualizarEstado(intent)
            }
        }
        val manualReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d("BatteryScreen", "Receiver manual recibido: ${intent?.action}")
                val estadoActual = context?.registerReceiver(
                    null,
                    IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                )
                actualizarEstado(estadoActual)
            }
        }

        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        context.registerReceiver(
            manualReceiver,
            IntentFilter("com.example.app.ACTUALIZAR_BATERIA")
        )
        Log.d("BatteryScreen", "Receiver registrado")
        Log.d("BatteryScreen", "Receiver manual registrado")
        onDispose {
            context.unregisterReceiver(receiver)
            context.unregisterReceiver(manualReceiver)
            Log.d("BatteryScreen", "Receiver desregistrado")
            Log.d("BatteryScreen", "Receiver manual desregistrado")
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Batería: $porcentaje%",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = "Estado: ${if (estaCargando) "Cargando" else "En batería"}")
        Button(
            onClick = {
                Log.d("BatteryScreen", "Botón presionado: disparando actualización manual")
                val intent = Intent("com.example.app.ACTUALIZAR_BATERIA")
                    .setPackage(context.packageName)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                pendingIntent.send()
            }
        ) {
            Text("Actualizar manualmente")
        }
    }
}

