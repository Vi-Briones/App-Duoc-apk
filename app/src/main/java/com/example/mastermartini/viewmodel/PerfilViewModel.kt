package com.example.mastermartini.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mastermartini.data.AuthRepository
import com.example.mastermartini.data.GuardadosRepository
import com.example.mastermartini.data.UsuarioSesion
import com.example.mastermartini.model.Video

/** ViewModel del perfil. Si usuario es null, no hay sesión. */
class PerfilViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    // Al cambiar (por ejemplo al cerrar sesión), la pantalla se actualiza
    var usuario by mutableStateOf<UsuarioSesion?>(authRepository.obtenerUsuarioActual())
        private set

    val haySesion: Boolean
        get() = usuario != null

    /** Iniciales para el avatar, por ejemplo "Luisana Rivero" -> "LR". */
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

    /** Cierra la sesión y borra los videos guardados. */
    fun cerrarSesion() {
        authRepository.cerrarSesion()
        GuardadosRepository.limpiar()
        usuario = null
    }
}
