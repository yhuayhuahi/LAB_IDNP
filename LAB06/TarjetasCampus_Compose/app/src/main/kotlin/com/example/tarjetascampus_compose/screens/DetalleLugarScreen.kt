package com.example.tarjetascampus_compose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tarjetascampus_compose.R

/**
 * DetalleLugarScreen muestra la información del lugar a tamaño completo,
 * reutilizando y ampliando el estilo de TarjetaLugar con iconos vectoriales de Material Design:
 * - Contenedor con esquinas redondeadas (RoundedCornerShape(24.dp)).
 * - Imagen circular destacada (CircleShape) con tamaño completo (160.dp).
 * - Tipografía con jerarquía visual (negrita en el nombre y cuerpo para la descripción).
 * - Iconos oficiales de Material Design (ArrowBack, Place, Schedule).
 */
@Composable
fun DetalleLugarScreen(
	nombre: String,
	descripcion: String,
	imagen: Painter,
	colorFondo: Color = Color(0xFFE3F2FD),
	horario: String = "08:00 - 18:00",
	ubicacion: String = "Campus Universitario",
	categoria: String = "Instalación",
	onVolver: () -> Unit = {}
) {
	val scrollState = rememberScrollState()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.verticalScroll(scrollState)
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		// Botón superior para volver atrás con icono Material
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			OutlinedButton(
				onClick = onVolver,
				shape = RoundedCornerShape(12.dp)
			) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						painter = painterResource(id = R.drawable.ic_arrow_back),
						contentDescription = "Volver",
						modifier = Modifier.size(18.dp)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text("Volver a Edificios")
				}
			}
		}

		// Reutilización directa del estilo de TarjetaLugar a tamaño completo
		Surface(
			shape = RoundedCornerShape(24.dp),
			color = colorFondo,
			modifier = Modifier.fillMaxWidth()
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(24.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				// Imagen circular a tamaño completo (como en TarjetaLugar)
				Image(
					painter = imagen,
					contentDescription = nombre,
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.size(160.dp)
						.clip(CircleShape)
				)

				Spacer(modifier = Modifier.height(18.dp))

				// Chip de categoría
				Surface(
					shape = RoundedCornerShape(16.dp),
					color = MaterialTheme.colorScheme.primaryContainer,
					modifier = Modifier.padding(bottom = 8.dp)
				) {
					Text(
						text = categoria,
						color = MaterialTheme.colorScheme.onPrimaryContainer,
						fontWeight = FontWeight.Bold,
						fontSize = 12.sp,
						modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
					)
				}

				// Nombre destacado con tipografía fuerte
				Text(
					text = nombre,
					fontWeight = FontWeight.Bold,
					fontSize = 24.sp,
					color = MaterialTheme.colorScheme.onSurface
				)

				Spacer(modifier = Modifier.height(12.dp))

				// Descripción del lugar
				Text(
					text = descripcion,
					fontSize = 15.sp,
					lineHeight = 22.sp,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}

		// Tarjeta de información contextual complementaria con iconos Material
		Card(
			shape = RoundedCornerShape(20.dp),
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
			),
			modifier = Modifier.fillMaxWidth()
		) {
			Column(
				modifier = Modifier.padding(18.dp),
				verticalArrangement = Arrangement.spacedBy(14.dp)
			) {
				Text(
					text = "Información del Edificio",
					fontWeight = FontWeight.Bold,
					fontSize = 16.sp,
					color = MaterialTheme.colorScheme.onSurface
				)

				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						painter = painterResource(id = R.drawable.ic_place),
						contentDescription = "Ubicación",
						tint = MaterialTheme.colorScheme.primary,
						modifier = Modifier.size(22.dp)
					)
					Spacer(modifier = Modifier.width(10.dp))
					Column {
						Text(text = "Ubicación", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
						Text(text = ubicacion, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
					}
				}

				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						painter = painterResource(id = R.drawable.ic_schedule),
						contentDescription = "Horario",
						tint = MaterialTheme.colorScheme.primary,
						modifier = Modifier.size(22.dp)
					)
					Spacer(modifier = Modifier.width(10.dp))
					Column {
						Text(text = "Horario de Atención", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
						Text(text = horario, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
					}
				}
			}
		}

		// Botón de acción para volver
		Button(
			onClick = onVolver,
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(14.dp)
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.Center,
				modifier = Modifier.padding(vertical = 4.dp)
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_arrow_back),
					contentDescription = null,
					modifier = Modifier.size(18.dp)
				)
				Spacer(modifier = Modifier.width(8.dp))
				Text("Regresar al Listado de Edificios")
			}
		}
	}
}
