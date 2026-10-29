package com.example.mastermartini.data

import androidx.compose.runtime.mutableStateListOf
import com.example.mastermartini.model.Video

/**
 * Videos guardados por el usuario.
 * Es un object para que Videos, Reproductor y Perfil compartan la misma lista.
 * Por ahora está en memoria, así que se borra al cerrar la app.
 */
object GuardadosRepository {

    // Solo guardamos los ids de los videos
    private val idsGuardados = mutableStateListOf<Int>()

    fun estaGuardado(videoId: Int): Boolean = videoId in idsGuardados

    /** Si ya está guardado lo quita; si no, lo agrega al inicio. */
    fun alternar(videoId: Int) {
        if (videoId in idsGuardados) {
            idsGuardados.remove(videoId)
        } else {
            idsGuardados.add(0, videoId)
        }
    }

    /** Quita un video de la lista. */
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
