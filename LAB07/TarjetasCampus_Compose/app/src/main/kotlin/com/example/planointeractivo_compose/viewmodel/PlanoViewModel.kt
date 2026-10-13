package com.example.planointeractivo_compose.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.planointeractivo_compose.model.Marcador
import org.json.JSONArray

class PlanoViewModel(application: Application) : AndroidViewModel(application) {
    var marcadores by mutableStateOf<List<Marcador>>(emptyList())
        private set

    var marcadorSeleccionado by mutableStateOf<Marcador?>(null)
        private set

    init {
        marcadores = cargarMarcadores()
    }

    fun seleccionarMarcador(marcador: Marcador?) {
        marcadorSeleccionado = marcador
    }

    fun limpiarSeleccion() {
        marcadorSeleccionado = null
    }

    private fun cargarMarcadores(): List<Marcador> {
        return try {
            val contenido = getApplication<Application>().assets
                .open("marcadores.json")
                .bufferedReader()
                .use { it.readText() }
            val jsonArray = JSONArray(contenido)

            List(jsonArray.length()) { index ->
                val json = jsonArray.getJSONObject(index)
                Marcador(
                    id = json.getString("id"),
                    xRatio = json.getDouble("xRatio").toFloat(),
                    yRatio = json.getDouble("yRatio").toFloat(),
                    radio = json.getDouble("radio").toFloat(),
                    titulo = json.getString("titulo"),
                    descripcion = json.getString("descripcion")
                )
            }
        } catch (exception: Exception) {
            Log.e("PlanoViewModel", "No se pudo cargar marcadores.json", exception)
            emptyList()
        }
    }
}