package com.example.mastermartini.data

import android.util.Patterns
import kotlinx.coroutines.delay

/** Datos del usuario que se muestran en el perfil. */
data class UsuarioSesion(
    val nombre: String,
    val correo: String,
    val telefono: String,
    val tipoNegocio: String
)

class AuthRepository {

    companion object {
        // Cuenta de prueba (se reemplaza al registrarse)
        private var currentName = "Usuario Master Martini"
        private var currentEmail = "usuario@mastermartini.cl"
        private var currentPhone = "+56 9 0000 0000"
        private var currentBusiness = "Pastelería"
        private var currentPass = "123456"

        // ¿Hay alguien con la sesión iniciada?
        private var sesionActiva = false
    }

    suspend fun login(email: String, pass: String): Boolean {
        delay(1500) // Simula tiempo de red
        val ok = email.trim().equals(currentEmail, ignoreCase = true) && pass == currentPass
        if (ok) sesionActiva = true
        return ok
    }

    suspend fun register(
        nombre: String,
        correo: String,
        telefono: String,
        tipoNegocio: String,
        pass: String
    ): Boolean {
        delay(1500)

        if (nombre.isNotBlank() && correo.isNotBlank() && telefono.isNotBlank() &&
            tipoNegocio.isNotBlank() && pass.isNotBlank()
        ) {
            // Acepta cualquier correo válido (gmail, hotmail, duocuc, etc.)
            if (Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) {
                currentName = nombre.trim()
                currentEmail = correo.trim()
                currentPhone = telefono.trim()
                currentBusiness = tipoNegocio
                currentPass = pass
                return true
            }
        }
        return false
    }

    /** Simula el envío del correo de recuperación (no envía nada). */
    suspend fun enviarRecuperacion(correo: String): Boolean {
        delay(1500)
        return true
    }

    /** Usuario con sesión iniciada, o null si no hay sesión. */
    fun obtenerUsuarioActual(): UsuarioSesion? =
        if (sesionActiva) UsuarioSesion(currentName, currentEmail, currentPhone, currentBusiness) else null

    /** Se llama al apretar "Cerrar sesión". */
    fun cerrarSesion() {
        sesionActiva = false
    }
}
