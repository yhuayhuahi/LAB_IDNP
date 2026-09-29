package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
private sealed class Screen(val route: String, val label: String) {
	data object Home : Screen("home", "Home")
	data object Edificios : Screen("edificios", "Edificios")
	data object Mapa : Screen("mapa", "Mapa")
	data object Perfil : Screen("perfil", "Perfil")
}

class SeleccionViewModel : ViewModel() {
	var edificioSeleccionado by mutableStateOf("Ninguno")
		private set

	fun seleccionarEdificio(nombre: String) {
		edificioSeleccionado = nombre
	}
}

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MaterialTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					MainScreen()
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
	val navController = rememberNavController()
	val seleccionViewModel: SeleccionViewModel = viewModel()
	val screens = listOf(Screen.Home, Screen.Edificios, Screen.Mapa, Screen.Perfil)

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = { Text("Campus Navigator") },
				colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
					titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
				)
			)
		},
		bottomBar = {
			val backStackEntry by navController.currentBackStackEntryAsState()
			val currentRoute = backStackEntry?.destination?.route

			NavigationBar {
				screens.forEach { screen ->
					val icon = when (screen) {
						Screen.Home -> "🏠"
						Screen.Edificios -> "🏢"
						Screen.Mapa -> "🗺️"
						Screen.Perfil -> "👤"
					}
					NavigationBarItem(
						selected = currentRoute == screen.route,
						onClick = {
							navController.navigate(screen.route) {
								popUpTo(navController.graph.startDestinationId) {
									saveState = true
								}
								launchSingleTop = true
								restoreState = true
							}
						},
						icon = { Text(icon) },
						label = { Text(screen.label) }
					)
				}
			}
		}
	) { innerPadding ->
		NavHost(
			navController = navController,
			startDestination = Screen.Home.route,
			modifier = Modifier.padding(innerPadding)
		) {
			composable(Screen.Home.route) {
				HomeScreen(seleccionViewModel)
			}
			composable(Screen.Edificios.route) {
				EdificiosScreen(seleccionViewModel)
			}
			composable(Screen.Mapa.route) {
				PlaceholderScreen("Mapa de ubicaciones", "La integración del mapa está pendiente.")
			}
			composable(Screen.Perfil.route) {
				PlaceholderScreen("Perfil", "Pantalla de perfil en construcción.")
			}
		}
	}
}

@Composable
private fun HomeScreen(seleccionViewModel: SeleccionViewModel) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(24.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text("Bienvenido al Campus", style = MaterialTheme.typography.headlineSmall)
		Text(
			text = "Último edificio consultado: ${seleccionViewModel.edificioSeleccionado}",
			modifier = Modifier.padding(top = 16.dp),
			style = MaterialTheme.typography.bodyLarge
		)
	}
}

@Composable
private fun EdificiosScreen(seleccionViewModel: SeleccionViewModel) {
	val edificios = listOf("Biblioteca Central", "Pabellón A", "Pabellón B", "Auditorio")

	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = 16.dp, vertical = 8.dp)
	) {
		item {
			Text(
				text = "Edificios del campus",
				modifier = Modifier.padding(vertical = 12.dp),
				style = MaterialTheme.typography.headlineSmall
			)
		}
		items(edificios) { nombre ->
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 8.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(nombre, modifier = Modifier.weight(1f))
				Button(onClick = { seleccionViewModel.seleccionarEdificio(nombre) }) {
					Text(if (seleccionViewModel.edificioSeleccionado == nombre) "Seleccionado" else "Ver")
				}
			}
		}
	}
}

@Composable
private fun PlaceholderScreen(title: String, message: String) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(24.dp),
		contentAlignment = Alignment.Center
	) {
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			Text(title, style = MaterialTheme.typography.headlineSmall)
			Text(message, modifier = Modifier.padding(top = 8.dp))
		}
	}
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
fun MainScreenPreview() {
	MaterialTheme {
		MainScreen()
	}
}
