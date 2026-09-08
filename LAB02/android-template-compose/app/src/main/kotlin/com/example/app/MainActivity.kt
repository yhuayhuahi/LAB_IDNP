package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Contenedor principal sin temas de Material
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White), 
                contentAlignment = Alignment.Center
            ) {
                Greeting("Android con Jetpack Compose Vanilla")
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    // BasicText es el componente puro de dibujo de texto
    BasicText(
        text = "¡Hola, $name!",
        style = TextStyle(
            color = Color.Black,
            fontSize = 20.sp
        )
    )
}