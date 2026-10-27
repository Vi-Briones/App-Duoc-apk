package com.example.mastermartini.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mastermartini.data.GuardadosRepository
import com.example.mastermartini.data.VideoRepository
import com.example.mastermartini.model.Video
import java.text.Normalizer

/**
 * VIEWMODEL de la lista de videos: guarda lo que el usuario busca,
 * decide qué videos se muestran y maneja el botón "guardar".
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

    // ----- Guardados -----
    fun estaGuardado(videoId: Int): Boolean = GuardadosRepository.estaGuardado(videoId)

    fun alternarGuardado(videoId: Int) {
        GuardadosRepository.alternar(videoId)
    }

    /** Quita tildes y mayúsculas: así "chocolateria" encuentra "Chocolatería". */
    private fun normalizar(texto: String): String =
        Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace(Regex("\\p{M}"), "")
            .lowercase()
            .trim()
}
