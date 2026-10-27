package com.example.mastermartini.model

import androidx.annotation.DrawableRes

/**
 * MODEL: un video técnico de la app.
 *
 * miniatura es opcional: si es null, la tarjeta muestra un fondo verde de la marca.
 * Para usar una foto real, pon la imagen en res/drawable y escribe R.drawable.<nombre>.
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
