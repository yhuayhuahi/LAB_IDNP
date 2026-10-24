package com.example.listafiltrable_compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listafiltrable_compose.components.FilaEdificio
import com.example.listafiltrable_compose.model.Edificio
import com.example.listafiltrable_compose.model.EdificioRepository

/**
 * EdificiosScreen (Reemplazo avanzado de EdificiosScreen de LAB05/06):
 * Integra:
 * 1. Carga de edificaciones reales del campus desde archivo de texto plano (Laboratorio 03).
 * 2. Filtro en tiempo real por nombre mediante OutlinedTextField.
 * 3. Menú desplegable para filtrado por categoría (Todas, Académico, Servicios, etc.).
 * 4. LazyColumn con FilaEdificio combinando TarjetaLugar y TarjetaCompacta con Material Icons.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdificiosScreen(
    onEdificioClick: (Edificio) -> Unit = {}
) {
    val context = LocalContext.current

    // 1. Carga desde archivo de texto plano según patrón de persistencia del Laboratorio 03
    val edificios = remember {
        EdificioRepository.cargarEdificios(context)
    }

    // Estados de filtrado
    var textoNombre by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todas") }
    var menuExpandido by remember { mutableStateOf(false) }

    // Categorías dinámicas detectadas a partir de la lista cargada
    val categoriasDisponibles = remember(edificios) {
        listOf("Todas") + edificios.map { it.categoria }.distinct()
    }

    // Filtrado reactivo en tiempo real combinando nombre y categoría
    val edificiosFiltrados = edificios.filter { edificio ->
        val coincideNombre = edificio.nombre.contains(textoNombre, ignoreCase = true) ||
                edificio.descripcion.contains(textoNombre, ignoreCase = true)
        val coincideCategoria = categoriaSeleccionada == "Todas" ||
                edificio.categoria.equals(categoriaSeleccionada, ignoreCase = true)
        coincideNombre && coincideCategoria
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Encabezado descriptivo
        Text(
            text = "Edificaciones del Campus",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Explora y filtra las instalaciones históricas y académicas.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo 1: Búsqueda por nombre en tiempo real
        OutlinedTextField(
            value = textoNombre,
            onValueChange = { textoNombre = it },
            label = { Text("Buscar por nombre o descripción") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
            },
            trailingIcon = {
                if (textoNombre.isNotEmpty()) {
                    IconButton(onClick = { textoNombre = "" }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpiar")
                    }
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo 2: Menú desplegable para selección de categoría
        ExposedDropdownMenuBox(
            expanded = menuExpandido,
            onExpandedChange = { menuExpandido = !menuExpandido },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = if (categoriaSeleccionada == "Todas") "Todas las categorías" else categoriaSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Filtrar por categoría") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filtro")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido)
                },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = { menuExpandido = false }
            ) {
                categoriasDisponibles.forEach { categoria ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (categoria == "Todas") "Todas las categorías" else categoria,
                                fontWeight = if (categoria == categoriaSeleccionada) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            categoriaSeleccionada = categoria
                            menuExpandido = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Indicador de resultados
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${edificiosFiltrados.size} de ${edificios.size} edificios encontrados",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (textoNombre.isNotEmpty() || categoriaSeleccionada != "Todas") {
                Text(
                    text = "Filtro activo",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Lista de edificios filtrada usando LazyColumn y Tarjetas del LAB06
        if (edificiosFiltrados.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No se encontraron edificios con los criterios indicados",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(edificiosFiltrados, key = { it.id }) { edificio ->
                    FilaEdificio(
                        edificio = edificio,
                        onClick = { onEdificioClick(edificio) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
