package com.example.tarjetascampus_compose.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.tarjetascampus_compose.R

data class LugarCampus(
	val id: String,
	val nombre: String,
	val descripcionCorta: String,
	val descripcionLarga: String,
	@DrawableRes val imagenRes: Int,
	val colorFondo: Color = Color(0xFFE3F2FD),
	val categoria: String = "Campus",
	val horario: String = "08:00 - 18:00",
	val ubicacion: String = "Campus Central"
)

object DatosCampus {
	val listaEdificios = listOf(
		LugarCampus(
			id = "biblioteca",
			nombre = "Biblioteca Central",
			descripcionCorta = "Zona de estudio y préstamo de libros",
			descripcionLarga = "La Biblioteca Central cuenta con 3 niveles equipados con salas de lectura individuales y grupales, hemeroteca, catálogo digital, acceso a bases de datos científicas y servicio de préstamo bibliográfico.",
			imagenRes = R.drawable.biblioteca,
			colorFondo = Color(0xFFE3F2FD),
			categoria = "Académico",
			horario = "07:30 - 20:30",
			ubicacion = "Sector Central, Pabellón Principal"
		),
		LugarCampus(
			id = "comedor",
			nombre = "Comedor Universitario",
			descripcionCorta = "Servicio de alimentación para estudiantes",
			descripcionLarga = "El Comedor Universitario ofrece menús nutritivos y balanceados para la comunidad estudiantil y docente. Cuenta con capacidad para más de 400 comensales por turno y estrictos estándares de salubridad e inocuidad.",
			imagenRes = R.drawable.comedor,
			colorFondo = Color(0xFFFFF3E0),
			categoria = "Servicios",
			horario = "11:30 - 15:00",
			ubicacion = "Área Sur, junto a Canchas Deportivas"
		),
		LugarCampus(
			id = "pabellon_sistemas",
			nombre = "Pabellón de Informática",
			descripcionCorta = "Aulas especializadas y laboratorios de cómputo",
			descripcionLarga = "Moderno pabellón destinado a las carreras de Ingeniería de Sistemas e Informática. Dispone de laboratorios con hardware especializado, aulas interactivas, conectividad de alta velocidad y áreas para proyectos de investigación.",
			imagenRes = R.drawable.biblioteca,
			colorFondo = Color(0xFFE8F5E9),
			categoria = "Facultad",
			horario = "07:00 - 21:00",
			ubicacion = "Sector Ingenierías, Bloque B"
		),
		LugarCampus(
			id = "auditorio",
			nombre = "Auditorio Principal",
			descripcionCorta = "Espacio para conferencias y actos protocolares",
			descripcionLarga = "Recinto acústico de gran capacidad diseñado para eventos institucionales, congresos internacionales, ceremonias de graduación y presentaciones artísticas universitarias.",
			imagenRes = R.drawable.comedor,
			colorFondo = Color(0xFFF3E5F5),
			categoria = "Eventos",
			horario = "Según programación",
			ubicacion = "Acceso Principal, Frente a Rectorado"
		)
	)
}
