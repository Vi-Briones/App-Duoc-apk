package com.example.mastermartini.viewmodel

/**
 * Estado de cada formulario: lo que escribió el usuario y sus errores.
 * Si un error es null, el campo está bien.
 */
data class LoginFormState(
    val correo: String = "",
    val password: String = "",
    val errorCorreo: String? = null,
    val errorPassword: String? = null,
    val errorGeneral: String? = null
)

data class RegistroFormState(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val tipoNegocio: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorTelefono: String? = null,
    val errorTipoNegocio: String? = null,
    val errorPassword: String? = null,
    val errorConfirmar: String? = null,
    val errorGeneral: String? = null
) {
    val tieneErrores: Boolean
        get() = listOf(
            errorNombre, errorCorreo, errorTelefono,
            errorTipoNegocio, errorPassword, errorConfirmar
        ).any { it != null }
}

data class RecuperarFormState(
    val correo: String = "",
    val errorCorreo: String? = null,
    val enviado: Boolean = false
)
