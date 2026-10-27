package com.example.persistenciaroom_compose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Update
import com.example.persistenciaroom_compose.ui.theme.PersistenciaRoom_ComposeTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import androidx.room.Index

// ============================================================================
// 1. ENTIDADES ROOM (3 Tablas con Foreign Keys)
// ============================================================================

// Entidad 1: EdificioEntity
@Entity
data class EdificioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val categoria: String
)

// Entidad 2: AmbienteEntity (FK a EdificioEntity)
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EdificioEntity::class,
            parentColumns = ["id"],
            childColumns = ["edificioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["edificioId"])]
)
data class AmbienteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val edificioId: Int,
    val nombre: String
)

// Entidad 3: NotaEntity (FK a AmbienteEntity)
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AmbienteEntity::class,
            parentColumns = ["id"],
            childColumns = ["ambienteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["ambienteId"])]
)
data class NotaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ambienteId: Int,
    val texto: String
)

// ============================================================================
// 2. MODELOS RELACIONALES (@Embedded y @Relation)
// ============================================================================

// Consulta Relacional 1: Edificio con sus Ambientes (1 a Muchos)
data class EdificioConAmbientes(
    @Embedded val edificio: EdificioEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "edificioId"
    )
    val ambientes: List<AmbienteEntity>
)

// Consulta Relacional 2: Ambiente con sus Notas (1 a Muchos)
data class AmbienteConNotas(
    @Embedded val ambiente: AmbienteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "ambienteId"
    )
    val notas: List<NotaEntity>
)

// ============================================================================
// 3. DAO COMPLETO (@Insert, @Update, @Delete para las 3 tablas y 2 consultas)
// ============================================================================

@Dao
interface EdificioDao {
    // --- EdificioEntity ---
    @Insert
    suspend fun insertarEdificio(edificio: EdificioEntity): Long

    @Update
    suspend fun actualizarEdificio(edificio: EdificioEntity)

    @Delete
    suspend fun eliminarEdificio(edificio: EdificioEntity)

    @Query("SELECT * FROM EdificioEntity")
    suspend fun obtenerTodos(): List<EdificioEntity>

    // --- AmbienteEntity ---
    @Insert
    suspend fun insertarAmbiente(ambiente: AmbienteEntity): Long

    @Update
    suspend fun actualizarAmbiente(ambiente: AmbienteEntity)

    @Delete
    suspend fun eliminarAmbiente(ambiente: AmbienteEntity)

    @Query("SELECT * FROM AmbienteEntity")
    suspend fun obtenerAmbientes(): List<AmbienteEntity>

    // --- NotaEntity ---
    @Insert
    suspend fun insertarNota(nota: NotaEntity): Long

    @Update
    suspend fun actualizarNota(nota: NotaEntity)

    @Delete
    suspend fun eliminarNota(nota: NotaEntity)

    @Query("SELECT * FROM NotaEntity")
    suspend fun obtenerNotas(): List<NotaEntity>

    // --- Consultas Relacionales ---
    // Consulta 1: Edificio con sus Ambientes
    @Transaction
    @Query("SELECT * FROM EdificioEntity")
    suspend fun obtenerConAmbientes(): List<EdificioConAmbientes>

    // Consulta 2: Ambiente con sus Notas
    @Transaction
    @Query("SELECT * FROM AmbienteEntity")
    suspend fun obtenerAmbientesConNotas(): List<AmbienteConNotas>
}

// ============================================================================
// 4. BASE DE DATOS ROOM
// ============================================================================

@Database(
    entities = [EdificioEntity::class, AmbienteEntity::class, NotaEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun edificioDao(): EdificioDao
}

fun crearBaseDeDatos(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app.db"
    )
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
}

// ============================================================================
// 5. VIEWMODEL & FACTORY
// ============================================================================

class EdificiosViewModel(private val dao: EdificioDao) : ViewModel() {
    private val _edificios = MutableStateFlow<List<EdificioConAmbientes>>(emptyList())
    val edificios: StateFlow<List<EdificioConAmbientes>> = _edificios

    private val _ambientesConNotas = MutableStateFlow<List<AmbienteConNotas>>(emptyList())
    val ambientesConNotas: StateFlow<List<AmbienteConNotas>> = _ambientesConNotas

    init {
        viewModelScope.launch {
            if (dao.obtenerTodos().isEmpty()) {
                sembrarDatosIniciales()
            }
            recargarDatos()
        }
    }

    private suspend fun sembrarDatosIniciales() {
        val id1 = dao.insertarEdificio(
            EdificioEntity(nombre = "Biblioteca Central", categoria = "Servicios")
        )
        val amb1Id = dao.insertarAmbiente(AmbienteEntity(edificioId = id1.toInt(), nombre = "Sala de lectura"))
        val amb2Id = dao.insertarAmbiente(AmbienteEntity(edificioId = id1.toInt(), nombre = "Hemeroteca"))

        dao.insertarNota(NotaEntity(ambienteId = amb1Id.toInt(), texto = "Capacidad para 120 personas"))
        dao.insertarNota(NotaEntity(ambienteId = amb1Id.toInt(), texto = "Horario: 08:00 a 20:00 hrs"))
        dao.insertarNota(NotaEntity(ambienteId = amb2Id.toInt(), texto = "Colección de periódicos históricos"))

        val id2 = dao.insertarEdificio(
            EdificioEntity(nombre = "Facultad de Ingeniería", categoria = "Académico")
        )
        val amb3Id = dao.insertarAmbiente(
            AmbienteEntity(edificioId = id2.toInt(), nombre = "Laboratorio de cómputo")
        )
        dao.insertarNota(NotaEntity(ambienteId = amb3Id.toInt(), texto = "40 estaciones con Linux"))
        dao.insertarNota(NotaEntity(ambienteId = amb3Id.toInt(), texto = "Proyector interactivo disponible"))
    }

    fun recargarDatos() {
        viewModelScope.launch {
            _edificios.value = dao.obtenerConAmbientes()
            _ambientesConNotas.value = dao.obtenerAmbientesConNotas()
        }
    }

    fun reiniciarDatos() {
        viewModelScope.launch {
            val todosEdificios = dao.obtenerTodos()
            for (e in todosEdificios) {
                dao.eliminarEdificio(e)
            }
            sembrarDatosIniciales()
            recargarDatos()
        }
    }

    // --- Operaciones Edificio ---
    fun agregarEdificio(nombre: String, categoria: String) {
        viewModelScope.launch {
            dao.insertarEdificio(EdificioEntity(nombre = nombre, categoria = categoria))
            recargarDatos()
        }
    }

    fun actualizarEdificio(edificio: EdificioEntity) {
        viewModelScope.launch {
            dao.actualizarEdificio(edificio)
            recargarDatos()
        }
    }

    fun eliminarEdificio(edificio: EdificioEntity) {
        viewModelScope.launch {
            dao.eliminarEdificio(edificio)
            recargarDatos()
        }
    }

    // --- Operaciones Ambiente ---
    fun agregarAmbiente(edificioId: Int, nombre: String) {
        viewModelScope.launch {
            dao.insertarAmbiente(AmbienteEntity(edificioId = edificioId, nombre = nombre))
            recargarDatos()
        }
    }

    fun actualizarAmbiente(ambiente: AmbienteEntity) {
        viewModelScope.launch {
            dao.actualizarAmbiente(ambiente)
            recargarDatos()
        }
    }

    fun eliminarAmbiente(ambiente: AmbienteEntity) {
        viewModelScope.launch {
            dao.eliminarAmbiente(ambiente)
            recargarDatos()
        }
    }

    // --- Operaciones Nota ---
    fun agregarNota(ambienteId: Int, texto: String) {
        viewModelScope.launch {
            dao.insertarNota(NotaEntity(ambienteId = ambienteId, texto = texto))
            recargarDatos()
        }
    }

    fun eliminarNota(nota: NotaEntity) {
        viewModelScope.launch {
            dao.eliminarNota(nota)
            recargarDatos()
        }
    }
}

class EdificiosViewModelFactory(private val dao: EdificioDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return EdificiosViewModel(dao) as T
    }
}

// ============================================================================
// 6. COMPOSABLES & INTERFAZ DE USUARIO
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: EdificiosViewModel) {
    var tabSeleccionada by remember { mutableIntStateOf(0) }
    var mostrarDialogoNuevoEdificio by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Persistencia Room (3 Tablas)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.reiniciarDatos() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Restablecer datos de muestra"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoNuevoEdificio = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Nuevo Edificio")
            }
        }
    ) { paddingVal ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingVal)
        ) {
            TabRow(selectedTabIndex = tabSeleccionada) {
                Tab(
                    selected = tabSeleccionada == 0,
                    onClick = { tabSeleccionada = 0 },
                    text = { Text("1. Edificios y Ambientes", fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = tabSeleccionada == 1,
                    onClick = { tabSeleccionada = 1 },
                    text = { Text("2. Ambientes y Notas", fontWeight = FontWeight.SemiBold) }
                )
            }

            if (tabSeleccionada == 0) {
                VistaEdificiosConAmbientes(viewModel)
            } else {
                VistaAmbientesConNotas(viewModel)
            }
        }
    }

    if (mostrarDialogoNuevoEdificio) {
        DialogoNuevoEdificio(
            onDismiss = { mostrarDialogoNuevoEdificio = false },
            onConfirm = { nombre, categoria ->
                viewModel.agregarEdificio(nombre, categoria)
                mostrarDialogoNuevoEdificio = false
            }
        )
    }
}

// ----------------------------------------------------------------------------
// Pestaña 1: Consulta 1 -> Edificios con Ambientes
// ----------------------------------------------------------------------------
@Composable
fun VistaEdificiosConAmbientes(viewModel: EdificiosViewModel) {
    val edificios by viewModel.edificios.collectAsState()

    var edificioParaEditar by remember { mutableStateOf<EdificioEntity?>(null) }
    var ambienteParaEditar by remember { mutableStateOf<AmbienteEntity?>(null) }
    var edificioParaAgregarAmbiente by remember { mutableStateOf<EdificioEntity?>(null) }

    if (edificios.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay edificios registrados. Usa el botón '+' para agregar uno.", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(edificios, key = { it.edificio.id }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Cabecera del edificio
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.edificio.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Categoría: ${item.edificio.categoria}",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Row {
                                IconButton(onClick = { edificioParaEditar = item.edificio }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar edificio", tint = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(onClick = { viewModel.eliminarEdificio(item.edificio) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar edificio", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                        // Lista de ambientes dentro del edificio
                        Text(
                            text = "Ambientes (${item.ambientes.size}):",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )

                        item.ambientes.forEach { ambiente ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, top = 2.dp, bottom = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "• ${ambiente.nombre}",
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Row {
                                    IconButton(
                                        onClick = { ambienteParaEditar = ambiente },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar ambiente", modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(
                                        onClick = { viewModel.eliminarAmbiente(ambiente) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar ambiente", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }

                        // Botón para agregar ambiente a este edificio
                        TextButton(
                            onClick = { edificioParaAgregarAmbiente = item.edificio },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Agregar ambiente", fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }

    // Diálogos de edición
    edificioParaEditar?.let { edificio ->
        DialogoEditarEdificio(
            edificio = edificio,
            onDismiss = { edificioParaEditar = null },
            onConfirm = { nombre, cat ->
                viewModel.actualizarEdificio(edificio.copy(nombre = nombre, categoria = cat))
                edificioParaEditar = null
            }
        )
    }

    ambienteParaEditar?.let { ambiente ->
        DialogoEditarAmbiente(
            ambiente = ambiente,
            onDismiss = { ambienteParaEditar = null },
            onConfirm = { nuevoNombre ->
                viewModel.actualizarAmbiente(ambiente.copy(nombre = nuevoNombre))
                ambienteParaEditar = null
            }
        )
    }

    edificioParaAgregarAmbiente?.let { edificio ->
        DialogoNuevoAmbiente(
            edificioNombre = edificio.nombre,
            onDismiss = { edificioParaAgregarAmbiente = null },
            onConfirm = { nombreAmbiente ->
                viewModel.agregarAmbiente(edificio.id, nombreAmbiente)
                edificioParaAgregarAmbiente = null
            }
        )
    }
}

// ----------------------------------------------------------------------------
// Pestaña 2: Consulta 2 -> Ambientes con Notas (@Embedded / @Relation)
// ----------------------------------------------------------------------------
@Composable
fun VistaAmbientesConNotas(viewModel: EdificiosViewModel) {
    val ambientesConNotas by viewModel.ambientesConNotas.collectAsState()

    var ambienteParaEditar by remember { mutableStateOf<AmbienteEntity?>(null) }
    var ambienteParaAgregarNota by remember { mutableStateOf<AmbienteEntity?>(null) }

    if (ambientesConNotas.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay ambientes registrados para mostrar notas.", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(ambientesConNotas, key = { it.ambiente.id }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.ambiente.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Ambiente ID: ${item.ambiente.id} (Edificio #${item.ambiente.edificioId})",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            Row {
                                IconButton(onClick = { ambienteParaEditar = item.ambiente }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar ambiente")
                                }
                                IconButton(onClick = { viewModel.eliminarAmbiente(item.ambiente) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar ambiente", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                        Text(
                            text = "Notas asociadas (${item.notas.size}):",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )

                        if (item.notas.isEmpty()) {
                            Text(
                                text = "Sin notas para este ambiente",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        } else {
                            item.notas.forEach { nota ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 8.dp, top = 3.dp, bottom = 3.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "- ${nota.texto}",
                                        fontSize = 13.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { viewModel.eliminarNota(nota) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Eliminar nota",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        TextButton(
                            onClick = { ambienteParaAgregarNota = item.ambiente },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Agregar nota", fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }

    ambienteParaEditar?.let { ambiente ->
        DialogoEditarAmbiente(
            ambiente = ambiente,
            onDismiss = { ambienteParaEditar = null },
            onConfirm = { nuevoNombre ->
                viewModel.actualizarAmbiente(ambiente.copy(nombre = nuevoNombre))
                ambienteParaEditar = null
            }
        )
    }

    ambienteParaAgregarNota?.let { ambiente ->
        DialogoNuevaNota(
            ambienteNombre = ambiente.nombre,
            onDismiss = { ambienteParaAgregarNota = null },
            onConfirm = { textoNota ->
                viewModel.agregarNota(ambiente.id, textoNota)
                ambienteParaAgregarNota = null
            }
        )
    }
}

// ============================================================================
// 7. DIÁLOGOS DE EDICIÓN Y CREACIÓN
// ============================================================================

@Composable
fun DialogoEditarEdificio(
    edificio: EdificioEntity,
    onDismiss: () -> Unit,
    onConfirm: (nombre: String, categoria: String) -> Unit
) {
    var nombre by remember { mutableStateOf(edificio.nombre) }
    var categoria by remember { mutableStateOf(edificio.categoria) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Actualizar Edificio") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Edificio") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (nombre.isNotBlank()) onConfirm(nombre, categoria) }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DialogoEditarAmbiente(
    ambiente: AmbienteEntity,
    onDismiss: () -> Unit,
    onConfirm: (nombre: String) -> Unit
) {
    var nombre by remember { mutableStateOf(ambiente.nombre) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Actualizar Ambiente") },
        text = {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Ambiente") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { if (nombre.isNotBlank()) onConfirm(nombre) }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DialogoNuevoEdificio(
    onDismiss: () -> Unit,
    onConfirm: (nombre: String, categoria: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Edificio") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Edificio") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría (Ej: Académico, Servicios)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (nombre.isNotBlank()) onConfirm(nombre, categoria) }
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DialogoNuevoAmbiente(
    edificioNombre: String,
    onDismiss: () -> Unit,
    onConfirm: (nombre: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Ambiente para '$edificioNombre'") },
        text = {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Ambiente") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { if (nombre.isNotBlank()) onConfirm(nombre) }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DialogoNuevaNota(
    ambienteNombre: String,
    onDismiss: () -> Unit,
    onConfirm: (texto: String) -> Unit
) {
    var texto by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Nota para '$ambienteNombre'") },
        text = {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Contenido de la nota") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { if (texto.isNotBlank()) onConfirm(texto) }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// ============================================================================
// 8. MAIN ACTIVITY
// ============================================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = crearBaseDeDatos(applicationContext)
        val dao = database.edificioDao()
        val factory = EdificiosViewModelFactory(dao)
        setContent {
            PersistenciaRoom_ComposeTheme {
                val viewModel: EdificiosViewModel = viewModel(factory = factory)
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainAppScreen(viewModel)
                }
            }
        }
    }
}
