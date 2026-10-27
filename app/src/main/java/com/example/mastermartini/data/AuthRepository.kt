package com.example.mastermartini.data

import android.util.Patterns
import kotlinx.coroutines.delay

/** Datos de la persona que inició sesión (lo que se muestra en el perfil). */
data class UsuarioSesion(
    val nombre: String,
    val correo: String,
    val telefono: String
)

class AuthRepository {

    companion object {
        // Cuenta de prueba inicial (se reemplaza cuando alguien se registra)
        private var currentName = "Usuario Master Martini"
        private var currentEmail = "usuario@mastermartini.cl"
        private var currentPhone = "+56 9 0000 0000"
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

    /**
     * OJO: el tercer parámetro se llama "rut" por compatibilidad con el código de
     * RegistroScreen/AuthViewModel, pero lo que llega ahí es el TELÉFONO del formulario.
     */
    suspend fun register(name: String, email: String, rut: String, pass: String): Boolean {
        delay(1500)

        if (name.isNotBlank() && email.isNotBlank() && rut.isNotBlank() && pass.isNotBlank()) {
            // Acepta cualquier correo válido (gmail, hotmail, duocuc, etc.)
            if (Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                currentName = name.trim()
                currentEmail = email.trim()
                currentPhone = rut.trim()   // ← es el teléfono
                currentPass = pass
                return true
            }
        }
        return false
    }

    /** Datos de quien inició sesión, o null si nadie ha entrado. */
    fun obtenerUsuarioActual(): UsuarioSesion? =
        if (sesionActiva) UsuarioSesion(currentName, currentEmail, currentPhone) else null

    /** Se llama al apretar "Cerrar sesión". */
    fun cerrarSesion() {
        sesionActiva = false
    }
}
