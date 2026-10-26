package com.example.mastermartini.model

import androidx.annotation.RawRes

/**
 * MODEL: representa un curso de la app.
 *
 * videoRes guarda el ID del video que está en res/raw (por ejemplo R.raw.curso_chocolateria).
 * @RawRes le avisa a Android Studio que ese Int debe ser un recurso de la carpeta raw.
 */
data class Curso(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val descripcion: String,
    val duracion: String,
    @RawRes val videoRes: Int
)
