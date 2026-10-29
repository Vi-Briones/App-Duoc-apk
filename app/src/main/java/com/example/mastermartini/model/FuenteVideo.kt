package com.example.mastermartini.model

import androidx.annotation.RawRes

/**
 * De dónde viene cada video: local (res/raw), streaming (URL) o YouTube.
 * Al ser sealed, Kotlin obliga a manejar los tres casos en cada when.
 */
sealed interface FuenteVideo {

    data class Local(@RawRes val recurso: Int) : FuenteVideo

    data class Streaming(val url: String) : FuenteVideo

    data class YouTube(val videoId: String) : FuenteVideo {
        val url: String get() = "https://www.youtube.com/watch?v=$videoId"
    }
}
