package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicText

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      BookRegisterScreen()
    }
  }
}

@Composable
fun BookRegisterScreen() {
  val context = LocalContext.current

  var title by remember { mutableStateOf("") }
  var author by remember { mutableStateOf("") }
  var pagesRead by remember { mutableStateOf("") }

  var savedLogText by remember { mutableStateOf("") }
  var statusMessage by remember { mutableStateOf("") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp)
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    BasicText(
      text = "Registro de Libros",
      style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
    )

    Spacer(modifier = Modifier.height(4.dp))

    // Campo: Título
    BasicText(text = "Título del libro:", style = TextStyle(fontWeight = FontWeight.Medium, color = Color.White))
    CustomTextField(
      value = title,
      onValueChange = { title = it },
      placeholder = "Ej. Cien años de soledad"
    )

    // Campo: Autor
    BasicText(text = "Autor:", style = TextStyle(fontWeight = FontWeight.Medium, color = Color.White))
    CustomTextField(
      value = author,
      onValueChange = { author = it },
      placeholder = "Ej. Gabriel García Márquez"
    )

    // Campo: Páginas leídas
    BasicText(text = "Páginas leídas:", style = TextStyle(fontWeight = FontWeight.Medium, color = Color.White))
    CustomTextField(
      value = pagesRead,
      onValueChange = { pagesRead = it },
      placeholder = "Ej. 150",
      keyboardType = KeyboardType.Number
    )

    Spacer(modifier = Modifier.height(8.dp))

// Botones
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      CustomButton(
        text = "Guardar",
        modifier = Modifier.weight(1f),
        backgroundColor = Color(0xFF572C8C),
        onClick = {
          RegistroHelper.guardar(context, title, author, pagesRead) { success, message ->
            statusMessage = message
            if (success) {
              title = ""
              author = ""
              pagesRead = ""
            }
          }
        }
      )

      CustomButton(
        text = "Ver registro",
        modifier = Modifier.weight(1f),
        backgroundColor = Color(0xFF29774C),
        onClick = {
          RegistroHelper.verRegistro(context) { content, status ->
            savedLogText = content
            statusMessage = status
          }
        }
      )
    }

    if (statusMessage.isNotEmpty()) {
      BasicText(
        text = statusMessage,
        style = TextStyle(fontSize = 14.sp, color = Color(0xFF388E3C))
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 4. Visualización en pantalla con Text (BasicText)
    if (savedLogText.isNotEmpty()) {
      BasicText(
        text = "Contenido guardado:",
        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
      )
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFFC3AED8))
          .padding(12.dp)
      ) {
        BasicText(
          text = savedLogText,
          style = TextStyle(fontSize = 14.sp, color = Color.DarkGray)
        )
      }
    }
  }
}

@Composable
fun CustomTextField(
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String,
  keyboardType: KeyboardType = KeyboardType.Text
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0xFFEFEFEF))
      .padding(horizontal = 12.dp, vertical = 10.dp)
  ) {
    if (value.isEmpty()) {
      BasicText(
        text = placeholder,
        style = TextStyle(color = Color.Gray, fontSize = 14.sp)
      )
    }
    BasicTextField(
      value = value,
      onValueChange = onValueChange,
      singleLine = true,
      textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
      keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
      modifier = Modifier.fillMaxWidth()
    )
  }
}

@Composable
fun CustomButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  backgroundColor: Color = Color(0xFF2196F3)
) {
  Box(
    modifier = modifier
      .background(backgroundColor)
      .clickable { onClick() }
      .padding(vertical = 12.dp, horizontal = 16.dp),
    contentAlignment = Alignment.Center
  ) {
    BasicText(
      text = text,
      style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    )
  }
}