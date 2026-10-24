package com.example.listafiltrable_compose.model

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

object EdificioRepository {
    private const val FILE_NAME = "edificios.txt"

    /**
     * Carga la lista de edificios desde el archivo de texto plano en el almacenamiento interno,
     * siguiendo el patrón de persistencia utilizado en el Laboratorio 03 (BufferedReader e InputStreamReader).
     * Si el archivo no existe aún en el almacenamiento interno, se inicializa a partir del asset predeterminado.
     */
    fun cargarEdificios(context: Context): List<Edificio> {
        val file = context.getFileStreamPath(FILE_NAME)

        // Inicialización en memoria interna si el archivo aún no existe
        if (!file.exists()) {
            try {
                context.assets.open(FILE_NAME).use { input ->
                    context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (e: Exception) {
                // Fallback de seguridad en caso de que assets no esté accesible
                escribirEdificiosPorDefecto(context)
            }
        }

        val lista = mutableListOf<Edificio>()
        try {
            // Patrón Laboratorio 03: context.openFileInput(...) + BufferedReader + useLines
            context.openFileInput(FILE_NAME).use { input ->
                BufferedReader(InputStreamReader(input)).useLines { lines ->
                    for (line in lines) {
                        val trimmed = line.trim()
                        if (trimmed.isEmpty() || trimmed.startsWith("#")) continue
                        val partes = trimmed.split("|")
                        if (partes.size >= 4) {
                            val id = partes[0].trim().toIntOrNull() ?: (lista.size + 1)
                            val nombre = partes[1].trim()
                            val categoria = partes[2].trim()
                            val descripcion = partes[3].trim()
                            val descripcionLarga = if (partes.size > 4) partes[4].trim() else descripcion
                            val iconoNombre = if (partes.size > 5) partes[5].trim() else "Apartment"
                            val horario = if (partes.size > 6) partes[6].trim() else "08:00 - 18:00"
                            val ubicacion = if (partes.size > 7) partes[7].trim() else "Campus UNSA"
                            val colorHex = if (partes.size > 8) partes[8].trim() else "#E3F2FD"

                            lista.add(
                                Edificio(
                                    id = id,
                                    nombre = nombre,
                                    categoria = categoria,
                                    descripcion = descripcion,
                                    descripcionLarga = descripcionLarga,
                                    iconoNombre = iconoNombre,
                                    horario = horario,
                                    ubicacion = ubicacion,
                                    colorHex = colorHex
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return lista
    }

    private fun escribirEdificiosPorDefecto(context: Context) {
        val contenido = """
            1|Biblioteca Central|Académico|Préstamo de libros, hemeroteca y salas de estudio grupal e individual.|La Biblioteca Central cuenta con tres niveles equipados con catálogo digital, bases de datos Scopus y ScienceDirect, y salas de estudio silencioso.|MenuBook|07:30 - 20:30|Área de Ingenierías, Pabellón Central|#E3F2FD
            2|Comedor Universitario|Servicios|Servicio de alimentación y nutrición balanceada para la comunidad universitaria.|Ofrece desayunos y almuerzos subsidiados para estudiantes con estrictos estándares de calidad alimentaria e inocuidad en amplios comedores.|Restaurant|11:30 - 15:00|Área de Ingenierías, Sector Sur|#FFF3E0
            3|Pabellón de Informática y Sistemas|Académico|Aulas especializadas y laboratorios de computación, desarrollo de software e IA.|Moderno complejo de 4 pisos que alberga laboratorios con computadoras de alto rendimiento, aulas interactivas y áreas para semilleros de investigación.|Computer|07:00 - 21:00|Área de Ingenierías, Bloque B|#E8F5E9
            4|Casona de San Agustín (Rectorado)|Patrimonio Histórico|Sede histórica monumental y centro cultural de la UNSA en claustros coloniales.|Joya arquitectónica de sillar del siglo XVII declarada Patrimonio Cultural. Alberga las salas protocolares del Rectorado y galerías de exposición artística.|AccountBalance|08:00 - 16:00|Calle San Agustín 106, Centro Histórico|#FFF8E1
            5|Paraninfo Universitario|Cultura y Eventos|Magno auditorio colonial para conferencias magistrales, grados y ceremonias solemnes.|Recinto con acústica tradicional y tallados en madera de cedro donde se realizan las ceremonias de graduación y actos protocolares más trascendentes.|RecordVoiceOver|Según programación|Claustros de San Agustín, Centro Histórico|#F3E5F5
            6|Coliseo Universitario|Deportes|Complejo polideportivo techado para básquetbol, vóleibol, futsal y torneos.|Instalación deportiva con graderías para 2,500 personas, piso flotante reglamentario y camerinos para selecciones universitarias.|SportsBasketball|08:00 - 20:00|Área de Ingenierías, Av. Venezuela|#E0F2F1
            7|Estadio Monumental Virgen de Chapi|Deportes|Estadio olímpico universitario con pista sintética y capacidad para 45,000 espectadores.|El escenario deportivo más imponente del sur del Perú, sede de eventos de atletismo, campeonatos nacionales y partidos internacionales.|Stadium|06:00 - 18:00|Campus Área de Sociales, Av. Avelino Cáceres|#EDE7F6
            8|Museo Arqueológico José María Arguedas|Patrimonio Histórico|Custodia y exhibición de valiosas piezas cerámicas y textiles prehispánicos.|Conserva colecciones arqueológicas de las culturas Churajón, Chuquibamba e Inca, ofreciendo recorridos guiados educativos.|Museum|08:30 - 16:30|Calle Álvarez Thomas 200, Centro Histórico|#FBE9E7
            9|Facultad de Ciencias Naturales y Formales|Académico|Aulas y laboratorios científicos de física, química y matemáticas avanzadas.|Complejo dedicado a la investigación experimental y ciencias puras, con laboratorios de espectrometría y modelado computacional.|Science|07:00 - 19:30|Área de Ingenierías, Bloque Central|#E1F5FE
        """.trimIndent()

        try {
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { output ->
                output.write(contenido.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
