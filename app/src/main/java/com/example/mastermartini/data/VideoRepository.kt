package com.example.mastermartini.data

import com.example.mastermartini.R
import com.example.mastermartini.model.FuenteVideo
import com.example.mastermartini.model.Video

/**
 * Lista de videos de la app.
 * Por ahora está escrita aquí; más adelante vendrá desde AWS
 * y solo habría que cambiar este archivo.
 */
object VideoRepository {

    private val videos = listOf(
        Video(
            id = 1,
            titulo = "Técnicas de Chocolatería",
            descripcion = "Aprende a trabajar coberturas de chocolate para bombones y decoraciones simples.",
            categorias = listOf("Pastelería", "Chocolatería"),
            duracion = "12 seg",
            fuente = FuenteVideo.Local(R.raw.video_chocolateria),
            miniatura = R.drawable.chocolateria
        ),
        Video(
            id = 2,
            titulo = "Masas de Hoja Perfectas",
            descripcion = "Técnica de laminado para croissants, palmeras y empanadas de hoja.",
            categorias = listOf("Panadería", "Masas"),
            duracion = "12 seg",
            fuente = FuenteVideo.Local(R.raw.video_masas_hoja),
            miniatura = R.drawable.hojaldre
        ),
        Video(
            id = 3,
            titulo = "Crema Batida para Vitrina",
            descripcion = "Cómo batir y estabilizar crema vegetal para tortas que duran en vitrina.",
            categorias = listOf("Pastelería", "Cremas"),
            duracion = "12 seg",
            fuente = FuenteVideo.Local(R.raw.video_crema_vitrina),
            miniatura = R.drawable.crema
        ),
        // Video de streaming de prueba (comentado, necesita internet)
        /**Video(
            id = 4,
            titulo = "Demo: video por streaming",
            descripcion = "Video de prueba cargado desde internet, para mostrar cómo funcionará la app " +
                "cuando los videos estén en un servicio de streaming.",
            categorias = listOf("Prueba", "Streaming"),
            duracion = "10 min",
            fuente = FuenteVideo.Streaming("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"),
            miniatura = R.drawable.hojaldre
        ),**/
        // Video de YouTube: el id es lo que va después de "watch?v=" en el link
        Video(
            id = 4,
            titulo = "DEMO YOUTUBE: CHEESECAKE de CUARENTENA | EL GUZII",
            descripcion = "Este es un Cheesecake (entre comillas) porque no tiene queso, ni huevo y no se utiliza un horno para hacerlo. Solo son 4 ingredientes que puedes conseguir en tu casa. ",
            categorias = listOf("Pastelería", "YouTube"),
            duracion = "6 min",
            fuente = FuenteVideo.YouTube("uZ9gj9HeJAY")
        )
    )

    fun obtenerVideos(): List<Video> = videos

    fun obtenerVideoPorId(id: Int): Video? = videos.find { it.id == id }
}
