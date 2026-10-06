package com.example.tarjetascampus_compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
 * Estilo 3 (Reto Opcional): Tarjeta Informativa con Insignia (Badge) e Íconos de Material Design.
 *
 * Combinación de Layouts y Modifiers utilizados:
 * - Card / Surface: Contenedor con elevación tonal y bordes redondeados (clip + clickable).
 * - Column: Disposición vertical global que apila la imagen de cabecera y el cuerpo descriptivo.
 * - Box: Apilamiento en la cabecera que contiene la imagen recortada y una insignia flotante (badge)
 *   alineada en la esquina superior derecha con `Modifier.align(Alignment.TopEnd)`.
 * - Row: Disposición horizontal utilizada tanto dentro de la insignia (indicador de estado + texto)
 *   como en la barra inferior de metadatos (ubicación y horario con espaciado SpaceBetween).
 */
@Composable
fun TarjetaInformativa(
	nombre: String,
	descripcion: String,
	imagen: Painter,
	etiqueta: String,
	horario: String,
	modifier: Modifier = Modifier,
	colorEtiqueta: Color = MaterialTheme.colorScheme.primary,
	onClick: () -> Unit = {}
) {
	Card(
		shape = RoundedCornerShape(20.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
		),
		elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
		modifier = modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(20.dp))
			.clickable { onClick() }
	) {
		Column {
			// Box para apilar imagen, filtro scrim y badge flotante
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(130.dp)
			) {
				Image(
					painter = imagen,
					contentDescription = nombre,
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.fillMaxWidth()
						.height(130.dp)
				)
				// Capa oscura para contraste
				Box(
					modifier = Modifier
						.matchParentSize()
						.background(Color.Black.copy(alpha = 0.25f))
				)
				// Badge flotante en la esquina superior derecha
				Surface(
					color = colorEtiqueta,
					shape = RoundedCornerShape(12.dp),
					modifier = Modifier
						.align(Alignment.TopEnd)
						.padding(10.dp)
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
					) {
						Box(
							modifier = Modifier
								.size(7.dp)
								.clip(CircleShape)
								.background(Color.White)
						)
						Spacer(modifier = Modifier.width(6.dp))
						Text(
							text = etiqueta,
							color = Color.White,
							fontWeight = FontWeight.Bold,
							fontSize = 12.sp
						)
					}
				}
			}

			// Column para detalles textuales
			Column(modifier = Modifier.padding(14.dp)) {
				Text(
					text = nombre,
					fontWeight = FontWeight.Bold,
					fontSize = 19.sp,
					color = MaterialTheme.colorScheme.onSurface
				)
				Spacer(modifier = Modifier.height(4.dp))
				Text(
					text = descripcion,
					fontSize = 13.sp,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					lineHeight = 18.sp
				)
				Spacer(modifier = Modifier.height(10.dp))

				// Row para metadatos (horario e indicador de navegación con iconos vectoriales Material)
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Row(verticalAlignment = Alignment.CenterVertically) {
						Icon(
							painter = painterResource(id = R.drawable.ic_schedule),
							contentDescription = "Horario",
							tint = MaterialTheme.colorScheme.primary,
							modifier = Modifier.size(16.dp)
						)
						Spacer(modifier = Modifier.width(5.dp))
						Text(
							text = horario,
							fontSize = 12.sp,
							color = MaterialTheme.colorScheme.primary,
							fontWeight = FontWeight.Medium
						)
					}
					Row(verticalAlignment = Alignment.CenterVertically) {
						Text(
							text = "Ver detalle",
							fontSize = 12.sp,
							color = MaterialTheme.colorScheme.secondary,
							fontWeight = FontWeight.SemiBold
						)
						Spacer(modifier = Modifier.width(4.dp))
						Icon(
							painter = painterResource(id = R.drawable.ic_arrow_forward),
							contentDescription = null,
							tint = MaterialTheme.colorScheme.secondary,
							modifier = Modifier.size(14.dp)
						)
					}
				}
			}
		}
	}
}
