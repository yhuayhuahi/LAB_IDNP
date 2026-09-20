package com.example.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app.ui.screens.HomeScreen
import com.example.app.ui.screens.LoginScreen
import com.example.app.ui.screens.RegistroScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            
            NavHost(navController = navController, startDestination = "login") {
            
                composable("login") {
                    LoginScreen(
                        onLoginExitoso = { usuario ->
                            navController.navigate("home/$usuario")
                        },
                        onIrARegistro = {
                            navController.navigate("registro")
                        }
                    )
                }

                composable("registro") {
                    RegistroScreen(
                        onRegistroExitoso = {
                            Toast.makeText(
                                this@MainActivity,
                                "Cuenta registrada con éxito",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.popBackStack()
                        },
                        onCancelar = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(
                    route = "home/{usuario}",
                    arguments = listOf(
                        navArgument("usuario") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val usuario = backStackEntry.arguments?.getString("usuario") ?: ""
                    HomeScreen(
                        usuario = usuario,
                        onCerrarSesion = {
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}
