package com.example.tarjetascampus_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tarjetascampus_compose.components.ListaTarjetasLugar
import com.example.tarjetascampus_compose.components.TarjetaCompacta
import com.example.tarjetascampus_compose.model.DatosCampus
import com.example.tarjetascampus_compose.navigation.Screen
import com.example.tarjetascampus_compose.screens.DetalleLugarScreen
import com.example.tarjetascampus_compose.screens.EdificiosScreen
import com.example.tarjetascampus_compose.screens.HomeScreen
import com.example.tarjetascampus_compose.screens.PlaceholderScreen
import com.example.tarjetascampus_compose.ui.theme.TarjetasCampus_ComposeTheme
import com.example.tarjetascampus_compose.viewmodel.SeleccionViewModel

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			TarjetasCampus_ComposeTheme {
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

	val backStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = backStackEntry?.destination?.route

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					val titulo = when {
						currentRoute == Screen.Home.route -> "Campus Navigator"
						currentRoute == Screen.Edificios.route -> "Edificios del Campus"
						currentRoute?.startsWith("detalle") == true -> "Detalle del Edificio"
						currentRoute == Screen.Mapa.route -> "Mapa de Ubicaciones"
						currentRoute == Screen.Perfil.route -> "Perfil de Usuario"
						else -> "Campus Navigator"
					}
					Text(titulo)
				},
				colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
					titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
				)
			)
		},
		bottomBar = {
			NavigationBar {
				screens.forEach { screen ->
					val iconRes = when (screen) {
						Screen.Home -> R.drawable.ic_home
						Screen.Edificios -> R.drawable.ic_apartment
						Screen.Mapa -> R.drawable.ic_map
						Screen.Perfil -> R.drawable.ic_person
						else -> R.drawable.ic_place
					}
					val isSelected = currentRoute == screen.route ||
							(screen == Screen.Edificios && currentRoute?.startsWith("detalle") == true)

					NavigationBarItem(
						selected = isSelected,
						onClick = {
							navController.navigate(screen.route) {
								popUpTo(navController.graph.startDestinationId) {
									saveState = true
								}
								launchSingleTop = true
								restoreState = true
							}
						},
						icon = {
							Icon(
								painter = painterResource(id = iconRes),
								contentDescription = screen.label
							)
						},
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
			// Pantalla Home (LAB05)
			composable(Screen.Home.route) {
				HomeScreen(
					seleccionViewModel = seleccionViewModel,
					onExplorarEdificios = {
						navController.navigate(Screen.Edificios.route)
					}
				)
			}

			// Pantalla Edificios con lista de tarjetas (LAB06 - Reemplazo de lista simple)
			composable(Screen.Edificios.route) {
				EdificiosScreen(
					seleccionViewModel = seleccionViewModel,
					onEdificioClick = { lugar ->
						navController.navigate(Screen.Detalle.crearRuta(lugar.id))
					}
				)
			}

			// Pantalla Detalle de Lugar (Requisito 1: Detalle a tamaño completo)
			composable(
				route = Screen.Detalle.route,
				arguments = listOf(navArgument("edificioId") { type = NavType.StringType })
			) { entry ->
				val edificioId = entry.arguments?.getString("edificioId")
				val lugar = DatosCampus.listaEdificios.find { it.id == edificioId }
					?: DatosCampus.listaEdificios.first()

				DetalleLugarScreen(
					nombre = lugar.nombre,
					descripcion = lugar.descripcionLarga,
					imagen = painterResource(id = lugar.imagenRes),
					colorFondo = lugar.colorFondo,
					horario = lugar.horario,
					ubicacion = lugar.ubicacion,
					categoria = lugar.categoria,
					onVolver = { navController.popBackStack() }
				)
			}

			// Pantalla Mapa (LAB05) con icono Material
			composable(Screen.Mapa.route) {
				PlaceholderScreen(
					title = "Mapa de ubicaciones",
					message = "La integración del mapa interactivo está en desarrollo.",
					iconRes = R.drawable.ic_map
				)
			}

			// Pantalla Perfil (LAB05) con icono Material
			composable(Screen.Perfil.route) {
				PlaceholderScreen(
					title = "Perfil de Usuario",
					message = "Pantalla de perfil institucional en construcción.",
					iconRes = R.drawable.ic_person
				)
			}
		}
	}
}

/**
 * Composable original de la clase del docente, mantenido para pruebas directas de tarjetas
 */
@Composable
fun PantallaTarjetas() {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		ListaTarjetasLugar()

		Spacer(modifier = Modifier.height(8.dp))

		TarjetaCompacta(
			nombre = "Biblioteca Central",
			imagen = painterResource(id = R.drawable.biblioteca)
		)

		Spacer(modifier = Modifier.height(12.dp))

		TarjetaCompacta(
			nombre = "Comedor Universitario",
			imagen = painterResource(id = R.drawable.comedor)
		)
	}
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
	TarjetasCampus_ComposeTheme {
		MainScreen()
	}
}

@Preview(showBackground = true)
@Composable
fun PantallaTarjetasPreview() {
	TarjetasCampus_ComposeTheme {
		PantallaTarjetas()
	}
}