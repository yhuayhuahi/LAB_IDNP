package com.example.listafiltrable_compose.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Screen("home", "Inicio", Icons.Default.Home)
    data object Edificios : Screen("edificios", "Edificios", Icons.Default.Apartment)
    data object Mapa : Screen("mapa", "Mapa", Icons.Default.Map)
    data object Perfil : Screen("perfil", "Perfil", Icons.Default.Person)
    data object Detalle : Screen("detalle/{edificioId}", "Detalle", Icons.Default.Info) {
        fun crearRuta(edificioId: Int) = "detalle/$edificioId"
    }
}
