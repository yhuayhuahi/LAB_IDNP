package com.example.planointeractivo_compose.model

import androidx.compose.ui.geometry.Offset

data class Marcador(
    val id: String,
    val xRatio: Float,
    val yRatio: Float,
    val radio: Float,
    val titulo: String,
    val descripcion: String
) {
    fun posicion(width: Float, height: Float): Offset =
        Offset(xRatio * width, yRatio * height)
}