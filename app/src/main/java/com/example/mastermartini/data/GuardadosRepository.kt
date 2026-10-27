package com.example.mastermartini.data

import androidx.compose.runtime.mutableStateListOf
import com.example.mastermartini.model.Video

/**
 * VIDEOS GUARDADOS por el usuario.
 *
 * Es un "object" (una sola instancia para toda la app) porque tres pantallas
 * necesitan ver la MISMA lista: Videos, Reproductor y Perfil.
 * Si guardas un video en el reproductor, el perfil lo muestra al instante.
 *
 * Se guarda solo en memoria: al cerrar la app la lista se vacía
 * (esta entrega no usa base de datos). A futuro se cambiaría por Room o DataStore,
 * y solo cambiaría este archivo.
 *
 * mutableStateListOf: una lista que Compose "observa". Cuando cambia,
 * las pantallas que la leen se redibujan solas.
 */
object GuardadosRepository {

    // Guardamos solo los ids; el video completo se busca en VideoRepository
    private val idsGuardados = mutableStateListOf<Int>()

    fun estaGuardado(videoId: Int): Boolean = videoId in idsGuardados

    /** Si está guardado lo quita; si no, lo agrega al principio (el más reciente primero). */
    fun alternar(videoId: Int) {
        if (videoId in idsGuardados) {
            idsGuardados.remove(videoId)
        } else {
            idsGuardados.add(0, videoId)
        }
    }

    /** Quita un video de la lista (si no estaba, no pasa nada). */
    fun quitar(videoId: Int) {
        idsGuardados.remove(videoId)
    }

    /** Vacía la lista (al cerrar sesión). */
    fun limpiar() {
        idsGuardados.clear()
    }

    fun obtenerGuardados(): List<Video> =
        idsGuardados.mapNotNull { id -> VideoRepository.obtenerVideoPorId(id) }
}
