package com.example.mastermartini.model

import androidx.annotation.DrawableRes

/**
 * Un video de la app.
 * Si miniatura es null, la tarjeta muestra un fondo verde.
 */
data class Video(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val categorias: List<String>,
    val duracion: String,
    val fuente: FuenteVideo,
    @DrawableRes val miniatura: Int? = null
)
