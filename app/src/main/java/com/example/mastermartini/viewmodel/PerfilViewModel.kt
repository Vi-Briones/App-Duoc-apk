package com.example.mastermartini.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mastermartini.data.AuthRepository
import com.example.mastermartini.data.GuardadosRepository
import com.example.mastermartini.data.UsuarioSesion
import com.example.mastermartini.model.Video

/**
 * VIEWMODEL del perfil.
 *
 * Tiene dos estados:
 *  - usuario != null  -> sesión iniciada: muestra nombre, correo, teléfono y guardados
 *  - usuario == null  -> sin sesión: la pantalla ofrece "Iniciar sesión" y "Crear cuenta"
 */
class PerfilViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    // Es un estado: cuando cambia (al cerrar sesión), la pantalla se redibuja sola
    var usuario by mutableStateOf<UsuarioSesion?>(authRepository.obtenerUsuarioActual())
        private set

    val haySesion: Boolean
        get() = usuario != null

    /** "Luisana Nayarit Rivero" -> "LN" (para el círculo del avatar). */
    val iniciales: String
        get() = (usuario?.nombre ?: "")
            .split(" ")
            .filter { it.isNotBlank() }
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .take(2)
            .joinToString("")

    // ----- Videos guardados -----
    val videosGuardados: List<Video>
        get() = GuardadosRepository.obtenerGuardados()

    /** Desmarcar un video lo elimina de la lista de guardados. */
    fun quitarGuardado(videoId: Int) {
        GuardadosRepository.quitar(videoId)
    }

    /** Cierra la sesión, borra los guardados y deja el perfil en modo "sin sesión". */
    fun cerrarSesion() {
        authRepository.cerrarSesion()
        GuardadosRepository.limpiar()
        usuario = null
    }
}
