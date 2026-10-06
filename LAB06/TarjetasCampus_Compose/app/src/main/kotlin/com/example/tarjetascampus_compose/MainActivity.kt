package com.example.tarjetascampus_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tarjetascampus_compose.components.ListaTarjetasLugar
import com.example.tarjetascampus_compose.components.TarjetaCompacta
import com.example.tarjetascampus_compose.ui.theme.TarjetasCampus_ComposeTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
			TarjetasCampus_ComposeTheme {
				Surface(modifier = Modifier.fillMaxSize()) {
					PantallaTarjetas()
				}
			}
    }
  }
}

@Composable
fun PantallaTarjetas() {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		// Estilo 1: tarjetas con Row (imagen circular + texto)
		ListaTarjetasLugar()

		Spacer(modifier = Modifier.height(8.dp))
		// Estilo 2: tarjetas apiladas con Box (imagen de fondo + scrim + texto)
		
		TarjetaCompacta(
			nombre = "Biblioteca Central",
			imagen = painterResource(id = R.drawable.biblioteca)
		)
		
		Spacer(modifier = Modifier.height(12.dp))
		
		TarjetaCompacta(
			nombre = "Comedor Universitario",
			imagen = painterResource(id = R.drawable.comedor)
		)
	}
}

@Preview(showBackground = true)
@Composable
fun PantallaTarjetasPreview() {
	TarjetasCampus_ComposeTheme {
		PantallaTarjetas()
	}
}