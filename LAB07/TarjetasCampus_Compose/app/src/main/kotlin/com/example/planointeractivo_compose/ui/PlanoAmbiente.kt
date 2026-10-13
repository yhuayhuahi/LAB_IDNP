package com.example.planointeractivo_compose.ui

import android.graphics.Paint as NativePaint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.planointeractivo_compose.viewmodel.PlanoViewModel
import kotlin.math.min

@Composable
fun PlanoAmbiente(viewModel: PlanoViewModel) {
    val marcadores = viewModel.marcadores
    val marcadorSeleccionado = viewModel.marcadorSeleccionado

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .pointerInput(marcadores) {
                val canvasWidth = size.width.toFloat()
                val canvasHeight = size.height.toFloat()
                detectTapGestures { touch ->
                    val base = min(canvasWidth, canvasHeight)
                    val seleccionado = marcadores.firstOrNull { marcador ->
                        (touch - marcador.posicion(canvasWidth, canvasHeight)).getDistance() <=
                            marcador.radio * base
                    }
                    viewModel.seleccionarMarcador(seleccionado)
                }
            }
    ) {
        val base = min(size.width, size.height)
        val padding = base * 0.04f
        val gap = base * 0.035f
        val roomWidth = (size.width - padding * 2f - gap) / 2f
        val roomHeight = (size.height - padding * 2f - gap) / 2f
        val wallColor = Color(0xFF37474F)
        val wallWidth = base * 0.012f
        val rooms = listOf(
            Offset(padding, padding) to Color(0xFFBBDEFB),
            Offset(padding + roomWidth + gap, padding) to Color(0xFFC8E6C9),
            Offset(padding, padding + roomHeight + gap) to Color(0xFFFFE0B2),
            Offset(padding + roomWidth + gap, padding + roomHeight + gap) to Color(0xFFFFCDD2)
        )

        rooms.forEach { (topLeft, color) ->
            drawRect(
                color = color,
                topLeft = topLeft,
                size = Size(roomWidth, roomHeight)
            )
        }

        drawRect(
            color = wallColor,
            topLeft = Offset(padding, padding),
            size = Size(size.width - padding * 2f, size.height - padding * 2f),
            style = Stroke(width = wallWidth)
        )
        drawLine(
            color = wallColor,
            start = Offset(size.width / 2f, padding),
            end = Offset(size.width / 2f, size.height - padding),
            strokeWidth = wallWidth
        )
        drawLine(
            color = wallColor,
            start = Offset(padding, size.height / 2f),
            end = Offset(size.width - padding, size.height / 2f),
            strokeWidth = wallWidth
        )

        val labelPaint = NativePaint().apply {
            color = android.graphics.Color.rgb(38, 50, 56)
            textAlign = NativePaint.Align.CENTER
            textSize = min(size.width * 0.045f, size.height * 0.05f)
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        marcadores.forEach { marcador ->
            val center = marcador.posicion(size.width, size.height)
            val radius = marcador.radio * base
            drawContext.canvas.nativeCanvas.drawText(
                marcador.titulo,
                center.x,
                center.y - radius * 1.65f,
                labelPaint
            )
            drawCircle(
                color = Color.Red,
                radius = radius,
                center = center
            )
        }
    }

    marcadorSeleccionado?.let { marcador ->
        AlertDialog(
            onDismissRequest = viewModel::limpiarSeleccion,
            title = { Text(marcador.titulo) },
            text = { Text(marcador.descripcion) },
            confirmButton = {
                TextButton(onClick = viewModel::limpiarSeleccion) {
                    Text("Cerrar")
                }
            }
        )
    }
}