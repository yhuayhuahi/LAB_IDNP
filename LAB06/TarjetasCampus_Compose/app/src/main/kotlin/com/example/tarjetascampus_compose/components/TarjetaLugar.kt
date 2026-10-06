package com.example.tarjetascampus_compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TarjetaLugar(
	nombre: String,
	descripcion: String,
	imagen: Painter,
	colorFondo: Color
) {
	Surface(
		shape = RoundedCornerShape(24.dp),
		color = colorFondo
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(16.dp)
		) {
			Image(
				painter = imagen,
				contentDescription = nombre,
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.size(90.dp)
					.clip(CircleShape)
			)

			Spacer(modifier = Modifier.width(16.dp))
			
			Column {
				Text(
					text = nombre,
					fontWeight = FontWeight.Bold,
					fontSize = 20.sp
				)
				Text(
					text = descripcion,
					fontSize = 14.sp
				)
			}
		}		
	}
}
