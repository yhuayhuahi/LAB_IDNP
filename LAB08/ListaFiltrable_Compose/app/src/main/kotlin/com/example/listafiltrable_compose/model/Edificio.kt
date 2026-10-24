package com.example.listafiltrable_compose.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Edificio(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val descripcionLarga: String = descripcion,
    val iconoNombre: String = "Apartment",
    val horario: String = "08:00 - 18:00",
    val ubicacion: String = "Campus Universitario",
    val colorHex: String = "#E3F2FD"
) {
    val icono: ImageVector
        get() = when (iconoNombre) {
            "MenuBook" -> Icons.Default.MenuBook
            "Restaurant" -> Icons.Default.Restaurant
            "Computer" -> Icons.Default.Computer
            "AccountBalance" -> Icons.Default.AccountBalance
            "RecordVoiceOver" -> Icons.Default.RecordVoiceOver
            "SportsBasketball" -> Icons.Default.SportsBasketball
            "Stadium" -> Icons.Default.Stadium
            "Museum" -> Icons.Default.Museum
            "Science" -> Icons.Default.Science
            else -> Icons.Default.Apartment
        }

    val colorFondo: Color
        get() = try {
            Color(android.graphics.Color.parseColor(colorHex))
        } catch (e: Exception) {
            Color(0xFFE3F2FD)
        }
}
