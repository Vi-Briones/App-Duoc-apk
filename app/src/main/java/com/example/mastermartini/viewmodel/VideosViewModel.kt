package com.example.mastermartini.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mastermartini.data.VideoRepository
import com.example.mastermartini.model.Video
import java.text.Normalizer

/**
 * VIEWMODEL de la lista "Videos técnicos": guarda lo que el usuario busca
 * y decide qué videos se muestran.
 */
class VideosViewModel : ViewModel() {

    private val todos: List<Video> = VideoRepository.obtenerVideos()

    var busqueda by mutableStateOf("")
        private set

    /** Videos que coinciden con la búsqueda (por título o por categoría). */
    val videosFiltrados: List<Video>
        get() {
            val texto = normalizar(busqueda)
            if (texto.isBlank()) return todos
            return todos.filter { video ->
                normalizar(video.titulo).contains(texto) ||
                    video.categorias.any { normalizar(it).contains(texto) }
            }
        }

    fun cambiarBusqueda(texto: String) {
        busqueda = texto
    }

    /**
     * Quita tildes y mayúsculas: así "chocolateria" encuentra "Chocolatería".
     * Pensado en usuarios que escriben rápido o sin tildes.
     */
    private fun normalizar(texto: String): String =
        Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace(Regex("\\p{M}"), "")
            .lowercase()
            .trim()
}
