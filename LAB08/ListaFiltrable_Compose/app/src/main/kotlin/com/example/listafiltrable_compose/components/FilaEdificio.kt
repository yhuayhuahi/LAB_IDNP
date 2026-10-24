package com.example.listafiltrable_compose.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.listafiltrable_compose.model.Edificio

/**
 * FilaEdificio:
 * Representa cada fila de la lista combinando los estilos de tarjeta del Laboratorio 06
 * (TarjetaLugar y TarjetaCompacta) enriquecidas con Material Icons representativos.
 * Alterna dinámicamente entre TarjetaLugar y TarjetaCompacta para una experiencia visual rica.
 */
@Composable
fun FilaEdificio(
    edificio: Edificio,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    // Alternamos estilo según el ID para combinar TarjetaLugar y TarjetaCompacta (Laboratorio 06)
    if (edificio.id % 2 != 0) {
        TarjetaLugar(
            nombre = edificio.nombre,
            descripcion = edificio.descripcion,
            categoria = edificio.categoria,
            icono = edificio.icono,
            colorFondo = edificio.colorFondo,
            modifier = modifier.padding(vertical = 6.dp),
            onClick = onClick
        )
    } else {
        TarjetaCompacta(
            nombre = edificio.nombre,
            categoria = edificio.categoria,
            icono = edificio.icono,
            colorFondo = edificio.colorFondo,
            modifier = modifier.padding(vertical = 6.dp),
            onClick = onClick
        )
    }
}
