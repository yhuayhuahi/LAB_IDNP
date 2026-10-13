package com.example.tarjetascampus_compose.navigation

sealed class Screen(val route: String, val label: String) {
	data object Home : Screen("home", "Home")
	data object Edificios : Screen("edificios", "Edificios")
	data object Mapa : Screen("mapa", "Mapa")
	data object Perfil : Screen("perfil", "Perfil")
	data object Detalle : Screen("detalle/{edificioId}", "Detalle") {
		fun crearRuta(edificioId: String) = "detalle/$edificioId"
	}
}
