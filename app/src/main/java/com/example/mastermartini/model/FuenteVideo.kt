package com.example.mastermartini.model

import androidx.annotation.RawRes

/**
 * DE DÓNDE VIENE CADA VIDEO.
 *
 * Esta es la clave para que el módulo sea ESCALABLE:
 * la app no está amarrada a "videos dentro del APK". Cada video dice su origen,
 * y el ViewModel sabe reproducir cualquiera de los tres.
 *
 *  - Local:     el .mp4 está en res/raw (lo que usamos ahora para el MVP).
 *  - Streaming: el video está en internet (un .mp4 o un .m3u8 de un servicio de
 *               streaming como Vimeo, Mux, Cloudflare Stream, Firebase Storage...).
 *  - YouTube:   el video está en el canal de YouTube de Master Martini.
 *
 * "sealed" significa que solo existen estas 3 opciones: si mañana agregamos una cuarta,
 * Kotlin nos obliga a manejarla en todos los "when" (no se nos puede olvidar).
 */
sealed interface FuenteVideo {

    data class Local(@RawRes val recurso: Int) : FuenteVideo

    data class Streaming(val url: String) : FuenteVideo

    data class YouTube(val videoId: String) : FuenteVideo {
        val url: String get() = "https://www.youtube.com/watch?v=$videoId"
    }
}
