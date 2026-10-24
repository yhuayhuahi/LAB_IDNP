package com.example.listafiltrable_compose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.listafiltrable_compose.model.Edificio

class SeleccionViewModel : ViewModel() {
    var edificioSeleccionado by mutableStateOf("Ninguno")
        private set

    var edificioActual by mutableStateOf<Edificio?>(null)
        private set

    fun seleccionarEdificio(edificio: Edificio) {
        edificioSeleccionado = edificio.nombre
        edificioActual = edificio
    }
}
