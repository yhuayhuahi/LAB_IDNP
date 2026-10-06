package com.example.tarjetascampus_compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tarjetascampus_compose.components.TarjetaCompacta
import com.example.tarjetascampus_compose.components.TarjetaInformativa
import com.example.tarjetascampus_compose.components.TarjetaLugar
import com.example.tarjetascampus_compose.model.DatosCampus
import com.example.tarjetascampus_compose.model.LugarCampus
import com.example.tarjetascampus_compose.viewmodel.SeleccionViewModel

/**
 * EdificiosScreen:
 * Reemplaza la lista simple de LAB05 (nombre + botón) por una lista rica de tarjetas
 * (TarjetaLugar, TarjetaCompacta y TarjetaInformativa) con datos de 4 edificios del campus.
 * Al pulsar cualquiera de las tarjetas, se actualiza el edificio seleccionado y se
 * navega a DetalleLugarScreen.
 */
@Composable
fun EdificiosScreen(
	seleccionViewModel: SeleccionViewModel,
	onEdificioClick: (LugarCampus) -> Unit
) {
	val edificios = DatosCampus.listaEdificios

	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = 16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		item {
			Column(modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)) {
				Text(
					text = "Edificios del Campus",
					style = MaterialTheme.typography.headlineSmall,
					fontWeight = FontWeight.Bold
				)
				Text(
					text = "Toca cualquier tarjeta para consultar su información detallada.",
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					modifier = Modifier.padding(top = 4.dp)
				)
			}
		}

		items(edificios) { edificio ->
			val clickAction = {
				seleccionViewModel.seleccionarEdificio(edificio.nombre)
				onEdificioClick(edificio)
			}

			when (edificio.id) {
				// Estilo 1: TarjetaLugar (Row con imagen circular + textos)
				"biblioteca" -> {
					TarjetaLugar(
						nombre = edificio.nombre,
						descripcion = edificio.descripcionCorta,
						imagen = painterResource(id = edificio.imagenRes),
						colorFondo = edificio.colorFondo,
						onClick = clickAction
					)
				}

				// Estilo 2: TarjetaCompacta (Box con imagen de fondo + scrim + texto inferior)
				"comedor" -> {
					TarjetaCompacta(
						nombre = edificio.nombre,
						imagen = painterResource(id = edificio.imagenRes),
						onClick = clickAction
					)
				}

				// Estilo 3: TarjetaInformativa (Reto opcional: Card + Column + Box badge + Row metadatos)
				"pabellon_sistemas" -> {
					TarjetaInformativa(
						nombre = edificio.nombre,
						descripcion = edificio.descripcionCorta,
						imagen = painterResource(id = edificio.imagenRes),
						etiqueta = edificio.categoria,
						horario = edificio.horario,
						onClick = clickAction
					)
				}

				// Estilo adicional: TarjetaLugar con paleta complementaria
				else -> {
					TarjetaLugar(
						nombre = edificio.nombre,
						descripcion = edificio.descripcionCorta,
						imagen = painterResource(id = edificio.imagenRes),
						colorFondo = edificio.colorFondo,
						onClick = clickAction
					)
				}
			}
		}

		item {
			// Margen inferior para no solapar con la barra de navegación
			Column(modifier = Modifier.padding(bottom = 16.dp)) {}
		}
	}
}
