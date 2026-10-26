package com.example.mastermartini.data

import com.example.mastermartini.R
import com.example.mastermartini.model.Curso

/**
 * Fuente de datos FICTICIA (el proyecto no usa base de datos).
 *
 * Es un "object" porque solo necesitamos UNA lista de cursos para toda la app.
 * Si más adelante los cursos vienen de una API, solo se cambia este archivo:
 * el ViewModel y las pantallas quedan igual.
 */
object CursoRepository {

    private val cursos = listOf(
        Curso(
            id = 1,
            titulo = "Chocolatería básica",
            categoria = "Chocolatería",
            descripcion = "Aprende a trabajar coberturas de chocolate para bombones y decoraciones simples.",
            duracion = "12 seg",
            videoRes = R.raw.curso_chocolateria
        ),
        Curso(
            id = 2,
            titulo = "Masas de hoja",
            categoria = "Panadería",
            descripcion = "Técnica de laminado para croissants, palmeras y empanadas de hoja.",
            duracion = "12 seg",
            videoRes = R.raw.curso_masas_hoja
        ),
        Curso(
            id = 3,
            titulo = "Crema batida para vitrina",
            categoria = "Pastelería",
            descripcion = "Cómo batir y estabilizar crema vegetal para tortas que duran en vitrina.",
            duracion = "12 seg",
            videoRes = R.raw.curso_crema_vitrina
        )
    )

    fun obtenerCursos(): List<Curso> = cursos

    fun obtenerCursoPorId(id: Int): Curso? = cursos.find { it.id == id }
}
