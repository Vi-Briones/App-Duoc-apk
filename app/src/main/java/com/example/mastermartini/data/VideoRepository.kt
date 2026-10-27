package com.example.mastermartini.data

import com.example.mastermartini.R
import com.example.mastermartini.model.FuenteVideo
import com.example.mastermartini.model.Video

/**
 * FUENTE DE DATOS de los videos (ficticia: el proyecto no usa base de datos).
 *
 * HOY: la lista está escrita aquí y los videos están en res/raw.
 * A FUTURO: esta lista vendrá de una API (por ejemplo con Retrofit) que devuelva
 * los videos con su URL de streaming o su id de YouTube. Así el equipo de marketing
 * sube videos SIN tener que publicar una nueva versión de la app.
 *
 * Lo importante: cuando eso pase, SOLO cambia este archivo.
 * Los ViewModels y las pantallas siguen igual, porque solo piden obtenerVideos().
 */
object VideoRepository {

    private val videos = listOf(
        Video(
            id = 1,
            titulo = "Técnicas de Chocolatería",
            descripcion = "Aprende a trabajar coberturas de chocolate para bombones y decoraciones simples.",
            categorias = listOf("Pastelería", "Chocolatería"),
            duracion = "12 seg",
            fuente = FuenteVideo.Local(R.raw.video_chocolateria)
        ),
        Video(
            id = 2,
            titulo = "Masas de Hoja Perfectas",
            descripcion = "Técnica de laminado para croissants, palmeras y empanadas de hoja.",
            categorias = listOf("Panadería", "Masas"),
            duracion = "12 seg",
            fuente = FuenteVideo.Local(R.raw.video_masas_hoja)
        ),
        Video(
            id = 3,
            titulo = "Crema Batida para Vitrina",
            descripcion = "Cómo batir y estabilizar crema vegetal para tortas que duran en vitrina.",
            categorias = listOf("Pastelería", "Cremas"),
            duracion = "12 seg",
            fuente = FuenteVideo.Local(R.raw.video_crema_vitrina)
        ),
        // Ejemplo de STREAMING: demuestra que la app ya puede reproducir videos desde internet.
        // (Es un video de prueba público. Necesita internet en el emulador/celular.)
        Video(
            id = 4,
            titulo = "Demo: video por streaming",
            descripcion = "Video de prueba cargado desde internet, para mostrar cómo funcionará la app " +
                "cuando los videos estén en un servicio de streaming.",
            categorias = listOf("Prueba", "Streaming"),
            duracion = "10 min",
            fuente = FuenteVideo.Streaming("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
        )
        // Ejemplo de YOUTUBE (descomenta y pon el id real de un video del canal):
        // el id es lo que va después de "watch?v=" en el link de YouTube.
        // , Video(
        //     id = 5,
        //     titulo = "Video del canal de YouTube",
        //     descripcion = "Se abre en la app de YouTube.",
        //     categorias = listOf("YouTube"),
        //     duracion = "5 min",
        //     fuente = FuenteVideo.YouTube("ID_DEL_VIDEO")
        // )
    )

    fun obtenerVideos(): List<Video> = videos

    fun obtenerVideoPorId(id: Int): Video? = videos.find { it.id == id }
}
