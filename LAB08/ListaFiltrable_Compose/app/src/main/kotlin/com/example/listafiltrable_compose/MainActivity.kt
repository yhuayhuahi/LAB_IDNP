package com.example.listafiltrable_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.listafiltrable_compose.model.Edificio
import com.example.listafiltrable_compose.navigation.Screen
import com.example.listafiltrable_compose.screens.DetalleLugarScreen
import com.example.listafiltrable_compose.screens.EdificiosScreen
import com.example.listafiltrable_compose.screens.HomeScreen
import com.example.listafiltrable_compose.screens.PlaceholderScreen
import com.example.listafiltrable_compose.ui.theme.ListaFiltrable_ComposeTheme
import com.example.listafiltrable_compose.viewmodel.SeleccionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListaFiltrable_ComposeTheme {
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

/**
 * MainScreen:
 * Estructura de navegación completa del Laboratorio 06 que integra el reto opcional:
 * Sustituye EdificiosScreen por la nueva lista con filtrado en tiempo real,
 * persistencia de texto plano (Laboratorio 03) y tarjetas visuales enriquecidas con Material Icons.
 */
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
                    val isSelected = currentRoute == screen.route
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.label
                            )
                        },
                        label = { Text(screen.label) },
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(Screen.Home.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
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
            // Pantalla Home (LAB06)
            composable(Screen.Home.route) {
                HomeScreen(
                    seleccionViewModel = seleccionViewModel,
                    onExplorarClick = {
                        navController.navigate(Screen.Edificios.route)
                    }
                )
            }

            // EdificiosScreen: Lista Filtrable ampliada (LAB08 - Reto Opcional)
            composable(Screen.Edificios.route) {
                EdificiosScreen(
                    onEdificioClick = { edificio ->
                        seleccionViewModel.seleccionarEdificio(edificio)
                        navController.navigate(Screen.Detalle.crearRuta(edificio.id))
                    }
                )
            }

            // DetalleLugarScreen: Información ampliada del edificio seleccionado
            composable(
                route = Screen.Detalle.route,
                arguments = listOf(navArgument("edificioId") { type = NavType.IntType })
            ) {
                DetalleLugarScreen(
                    edificio = seleccionViewModel.edificioActual,
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }

            // Mapa (Placeholder LAB06)
            composable(Screen.Mapa.route) {
                PlaceholderScreen(
                    titulo = "Mapa del Campus",
                    subtitulo = "Visualización geoespacial e interactiva en desarrollo.",
                    icono = Icons.Default.Map
                )
            }

            // Perfil (Placeholder LAB06)
            composable(Screen.Perfil.route) {
                PlaceholderScreen(
                    titulo = "Perfil del Usuario",
                    subtitulo = "Información académica del estudiante y configuración.",
                    icono = Icons.Default.Person
                )
            }
        }
    }
}

/**
 * Composable ListaEdificios mantenido para compatibilidad directa y vista independiente:
 */
@Composable
fun ListaEdificios() {
    EdificiosScreen()
}

@Preview(showBackground = true)
@Composable
fun ListaEdificiosPreview() {
    ListaFiltrable_ComposeTheme {
        ListaEdificios()
    }
}
