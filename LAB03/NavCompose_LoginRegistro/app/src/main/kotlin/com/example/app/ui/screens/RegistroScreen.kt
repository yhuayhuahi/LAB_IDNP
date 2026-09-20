package com.example.app.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onCancelar: () -> Unit
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
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = usuario,
            onValueChange = { 
                usuario = it 
                mensajeError = ""
            },
            label = { Text("Nuevo usuario") },
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
            label = { Text("Nueva contraseña") },
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

                try {
                    context.openFileOutput("cuentas.txt", Context.MODE_APPEND).use { output ->
                        val linea = "$trimmedUser,$trimmedPass\n"
                        output.write(linea.toByteArray())
                    }
                    onRegistroExitoso()
                } catch (e: Exception) {
                    mensajeError = "Error al guardar la cuenta"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Aceptar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onCancelar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}
