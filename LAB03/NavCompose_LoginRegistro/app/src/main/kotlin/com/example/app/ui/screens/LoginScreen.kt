package com.example.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun LoginScreen(
    onLoginExitoso: (String) -> Unit,
    onIrARegistro: () -> Unit
) {
    val context = LocalContext.current
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { 
                usuario = it 
                mensajeError = ""
            },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            isError = mensajeError.isNotEmpty() && usuario.isBlank()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it 
                mensajeError = ""
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = mensajeError.isNotEmpty() && password.isBlank()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (mensajeError.isNotEmpty()) {
            Text(mensajeError, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val trimmedUser = usuario.trim()
                val trimmedPass = password.trim()

                if (trimmedUser.isEmpty() || trimmedPass.isEmpty()) {
                    mensajeError = "Por favor, complete todos los campos"
                    return@Button
                }

                var encontrado = false
                try {
                    val file = context.getFileStreamPath("cuentas.txt")
                    if (file.exists()) {
                        context.openFileInput("cuentas.txt").use { input ->
                            BufferedReader(InputStreamReader(input)).useLines { lines ->
                                for (line in lines) {
                                    val partes = line.split(",")
                                    if (partes.size >= 2) {
                                        val u = partes[0].trim()
                                        val p = partes[1].trim()
                                        if (u == trimmedUser && p == trimmedPass) {
                                            encontrado = true
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    encontrado = false
                }

                if (encontrado) {
                    mensajeError = ""
                    onLoginExitoso(trimmedUser)
                } else {
                    mensajeError = "Cuenta no encontrada"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onIrARegistro,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear cuenta")
        }
    }
}
