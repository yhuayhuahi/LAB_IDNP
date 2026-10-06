package com.example.tarjetascampus_compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tarjetascampus_compose.R

@Composable
fun ListaTarjetasLugar(
	onTarjetaClick: (String) -> Unit = {}
) {
	Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
		TarjetaLugar(
			nombre = "Biblioteca Central",
			descripcion = "Zona de estudio y préstamo de libros",
			imagen = painterResource(id = R.drawable.biblioteca),
			colorFondo = Color(0xFFE3F2FD),
			onClick = { onTarjetaClick("Biblioteca Central") }
		)
		TarjetaLugar(
			nombre = "Comedor Universitario",
			descripcion = "Servicio de alimentación para estudiantes",
			imagen = painterResource(id = R.drawable.comedor),
			colorFondo = Color(0xFFFFF3E0),
			onClick = { onTarjetaClick("Comedor Universitario") }
		)
	}
}
