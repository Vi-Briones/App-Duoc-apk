package com.example.mastermartini.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermartini.data.AuthRepository
import com.example.mastermartini.validacion.ValidadorFormularios
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de Login, Registro y Recuperar clave.
 * Aquí se validan los formularios y se llama al repositorio.
 */
class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    val opcionesNegocio = listOf(
        "Pastelería", "Panadería", "Chocolatería", "Heladería", "Restaurante", "Casino", "Otro"
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Cuando es true, la pantalla navega
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

    var login by mutableStateOf(LoginFormState())
        private set

    var registro by mutableStateOf(RegistroFormState())
        private set

    var recuperar by mutableStateOf(RecuperarFormState())
        private set

    // Los errores salen al apretar el botón y se quitan al corregir el campo

    // ----- Login -----
    fun onLoginCorreoChange(valor: String) {
        login = login.copy(
            correo = valor,
            errorCorreo = login.errorCorreo?.let { ValidadorFormularios.validarCorreo(valor) },
            errorGeneral = null
        )
    }

    fun onLoginPasswordChange(valor: String) {
        login = login.copy(
            password = valor,
            errorPassword = login.errorPassword?.let { ValidadorFormularios.validarPasswordLogin(valor) },
            errorGeneral = null
        )
    }

    fun iniciarSesion() {
        if (_isLoading.value) return
        val form = login.copy(
            errorCorreo = ValidadorFormularios.validarCorreo(login.correo),
            errorPassword = ValidadorFormularios.validarPasswordLogin(login.password),
            errorGeneral = null
        )
        login = form
        if (form.errorCorreo != null || form.errorPassword != null) return

        viewModelScope.launch {
            _isLoading.value = true
            val ok = repository.login(form.correo, form.password)
            _isLoading.value = false
            if (ok) {
                _loginSuccess.value = true
            } else {
                login = login.copy(errorGeneral = "Correo o contraseña incorrectos.")
            }
        }
    }

    /** Limpia el login después de entrar, para que no entre solo la próxima vez. */
    fun loginNavegado() {
        _loginSuccess.value = false
        login = LoginFormState()
    }

    // ----- Registro -----
    fun onNombreChange(valor: String) {
        registro = registro.copy(
            nombre = valor,
            errorNombre = registro.errorNombre?.let { ValidadorFormularios.validarNombre(valor) },
            errorGeneral = null
        )
    }

    fun onRegistroCorreoChange(valor: String) {
        registro = registro.copy(
            correo = valor,
            errorCorreo = registro.errorCorreo?.let { ValidadorFormularios.validarCorreo(valor) },
            errorGeneral = null
        )
    }

    /** Solo deja escribir dígitos y como máximo 9. */
    fun onTelefonoChange(valor: String) {
        val digitos = valor.filter { it.isDigit() }.take(ValidadorFormularios.LARGO_TELEFONO)
        registro = registro.copy(
            telefono = digitos,
            errorTelefono = registro.errorTelefono?.let { ValidadorFormularios.validarTelefono(digitos) },
            errorGeneral = null
        )
    }

    fun onTipoNegocioChange(valor: String) {
        registro = registro.copy(
            tipoNegocio = valor,
            errorTipoNegocio = registro.errorTipoNegocio?.let { ValidadorFormularios.validarTipoNegocio(valor) },
            errorGeneral = null
        )
    }

    fun onRegistroPasswordChange(valor: String) {
        registro = registro.copy(
            password = valor,
            errorPassword = registro.errorPassword?.let { ValidadorFormularios.validarPasswordNueva(valor) },
            errorConfirmar = registro.errorConfirmar?.let {
                ValidadorFormularios.validarConfirmacion(valor, registro.confirmarPassword)
            },
            errorGeneral = null
        )
    }

    fun onConfirmarPasswordChange(valor: String) {
        registro = registro.copy(
            confirmarPassword = valor,
            errorConfirmar = registro.errorConfirmar?.let {
                ValidadorFormularios.validarConfirmacion(registro.password, valor)
            },
            errorGeneral = null
        )
    }

    fun registrarse() {
        if (_isLoading.value) return
        val form = registro.copy(
            errorNombre = ValidadorFormularios.validarNombre(registro.nombre),
            errorCorreo = ValidadorFormularios.validarCorreo(registro.correo),
            errorTelefono = ValidadorFormularios.validarTelefono(registro.telefono),
            errorTipoNegocio = ValidadorFormularios.validarTipoNegocio(registro.tipoNegocio),
            errorPassword = ValidadorFormularios.validarPasswordNueva(registro.password),
            errorConfirmar = ValidadorFormularios.validarConfirmacion(registro.password, registro.confirmarPassword),
            errorGeneral = null
        )
        registro = form
        if (form.tieneErrores) return

        viewModelScope.launch {
            _isLoading.value = true
            val ok = repository.register(
                nombre = form.nombre,
                correo = form.correo,
                telefono = "+56 ${form.telefono}",
                tipoNegocio = form.tipoNegocio,
                pass = form.password
            )
            _isLoading.value = false
            if (ok) {
                // Deja el correo listo en el login
                login = LoginFormState(correo = form.correo.trim())
                _registerSuccess.value = true
            } else {
                registro = registro.copy(errorGeneral = "No se pudo crear la cuenta. Inténtalo de nuevo.")
            }
        }
    }

    fun registroNavegado() {
        _registerSuccess.value = false
        registro = RegistroFormState()
    }

    /** Deja el registro vacío al entrar. */
    fun limpiarRegistro() {
        registro = RegistroFormState()
    }

    // ----- Recuperar clave -----
    fun onRecuperarCorreoChange(valor: String) {
        recuperar = recuperar.copy(
            correo = valor,
            errorCorreo = recuperar.errorCorreo?.let { ValidadorFormularios.validarCorreo(valor) },
            enviado = false
        )
    }

    fun enviarRecuperacion() {
        if (_isLoading.value) return
        val error = ValidadorFormularios.validarCorreo(recuperar.correo)
        recuperar = recuperar.copy(errorCorreo = error, enviado = false)
        if (error != null) return

        viewModelScope.launch {
            _isLoading.value = true
            repository.enviarRecuperacion(recuperar.correo)
            _isLoading.value = false
            recuperar = RecuperarFormState(enviado = true)
        }
    }

    /** Se llama al entrar a Recuperar clave. */
    fun limpiarRecuperar() {
        recuperar = RecuperarFormState()
    }
}
