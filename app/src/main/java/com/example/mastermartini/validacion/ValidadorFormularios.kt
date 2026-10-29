package com.example.mastermartini.validacion

/**
 * REGLAS DE VALIDACIÓN de los formularios (login, registro y recuperar clave).
 *
 * Están separadas de las pantallas: cada función recibe el texto y devuelve
 * el mensaje de error, o null si el dato está bien.
 * Así la misma regla sirve en varias pantallas y la pantalla no decide qué es válido.
 */
object ValidadorFormularios {

    const val LARGO_TELEFONO = 9
    const val MIN_PASSWORD = 6

    // Cualquier dominio: gmail, hotmail, duocuc, mastermartini, etc.
    private val regexCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    // Letras (con tildes y ñ), espacios, apóstrofo, punto y guion
    private val regexNombre = Regex("^[\\p{L} '.-]+$")

    fun validarNombre(nombre: String): String? {
        val limpio = nombre.trim()
        return when {
            limpio.isEmpty() -> "Ingresa tu nombre completo."
            !regexNombre.matches(limpio) -> "El nombre solo puede tener letras."
            limpio.split(" ").count { it.isNotBlank() } < 2 -> "Ingresa tu nombre y apellido."
            else -> null
        }
    }

    fun validarCorreo(correo: String): String? {
        val limpio = correo.trim()
        return when {
            limpio.isEmpty() -> "Ingresa tu correo electrónico."
            !regexCorreo.matches(limpio) -> "Revisa tu correo (ej: nombre@gmail.com)."
            else -> null
        }
    }

    /** El teléfono llega solo con dígitos y sin el +56 (ej: 912345678). */
    fun validarTelefono(telefono: String): String? = when {
        telefono.isEmpty() -> "Ingresa tu teléfono."
        telefono.length != LARGO_TELEFONO || !telefono.all { it.isDigit() } ->
            "El teléfono debe tener $LARGO_TELEFONO dígitos (ej: 912345678)."
        else -> null
    }

    fun validarTipoNegocio(tipoNegocio: String): String? =
        if (tipoNegocio.isBlank()) "Selecciona el tipo de negocio." else null

    /** En el login solo se revisa que no esté vacía (la clave la comprueba el repositorio). */
    fun validarPasswordLogin(password: String): String? =
        if (password.isEmpty()) "Ingresa tu contraseña." else null

    /** Contraseña nueva (registro): largo mínimo y al menos una letra y un número. */
    fun validarPasswordNueva(password: String): String? = when {
        password.isEmpty() -> "Crea una contraseña."
        password.length < MIN_PASSWORD -> "La contraseña debe tener al menos $MIN_PASSWORD caracteres."
        password.none { it.isLetter() } || password.none { it.isDigit() } ->
            "Usa al menos una letra y un número."
        else -> null
    }

    fun validarConfirmacion(password: String, confirmacion: String): String? = when {
        confirmacion.isEmpty() -> "Repite tu contraseña."
        confirmacion != password -> "Las contraseñas no coinciden."
        else -> null
    }
}
