package com.example.app

import android.content.Context
import android.util.Log

object RegistroHelper {
  private const val FILE_NAME = "libros_registro.txt"
  private const val TAG = "BookRegister"

  fun guardar(
    context: Context,
    title: String,
    author: String,
    pagesRead: String,
    onResult: (success: Boolean, message: String) -> Unit
  ) {
    if (title.isNotBlank() && author.isNotBlank() && pagesRead.isNotBlank()) {
      val record = "Título: $title | Autor: $author | Páginas: $pagesRead\n"
      try {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { output ->
          output.write(record.toByteArray())
        }
        onResult(true, "¡Datos guardados correctamente!")
      } catch (e: Exception) {
        Log.e(TAG, "Error al guardar el archivo", e)
        onResult(false, "Error al guardar los datos.")
      }
    } else {
      onResult(false, "Por favor, completa todos los campos.")
    }
  }

  fun verRegistro(
    context: Context,
    onResult: (content: String, status: String) -> Unit
  ) {
    try {
      val content = context.openFileInput(FILE_NAME).bufferedReader().use { it.readText() }
      Log.d(TAG, "Contenido del archivo:\n$content")
      onResult(content, "Registro leído y mostrado en consola (Log.d).")
    } catch (e: Exception) {
      Log.e(TAG, "Error al leer el archivo", e)
      val errorMsg = "No se encontró registro guardado o hubo un error al leer."
      Log.d(TAG, errorMsg)
      onResult(errorMsg, errorMsg)
    }
  }
}