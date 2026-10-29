package com.example.mastermartini.viewmodel

/**
 * ESTADO de cada formulario: lo que escribió el usuario y el error de cada campo.
 * error == null significa que el campo está bien (o que aún no se ha validado).
 * errorGeneral es para lo que no es de un campo (ej: "Correo o contraseña incorrectos").
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
