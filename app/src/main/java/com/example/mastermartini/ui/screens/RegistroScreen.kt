package com.example.mastermartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mastermartini.ui.components.CampoFormulario
import com.example.mastermartini.ui.components.MensajeFormulario
import com.example.mastermartini.ui.components.MensajeFormularioAnimado
import com.example.mastermartini.ui.components.SelectorFormulario
import com.example.mastermartini.validacion.ValidadorFormularios
import com.example.mastermartini.viewmodel.AuthViewModel

// Paleta de colores corporativa
private val DarkGreen = Color(0xFF003831)
private val Gold = Color(0xFFD4A336)
private val BackgroundGray = Color(0xFFF5F7F6)

@Composable
fun RegistroScreen(
    viewModel: AuthViewModel,
    onRegistrado: () -> Unit
) {
    // El formulario y las validaciones están en el ViewModel
    val form = viewModel.registro
    val isLoading by viewModel.isLoading.collectAsState()
    val registerSuccess by viewModel.registerSuccess.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            viewModel.registroNavegado()
            onRegistrado()
        }
    }

    val enviar = {
        focusManager.clearFocus()
        viewModel.registrarse()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .imePadding()
            .verticalScroll(rememberScrollState()) // el scroll mueve todo, incluso el cabezal
    ) {
        // Cabezal Verde
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = DarkGreen,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(top = 48.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "REGÍSTRATE",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CampoFormulario(
                valor = form.nombre,
                onValorChange = viewModel::onNombreChange,
                etiqueta = "Nombre completo",
                icono = Icons.Filled.Person,
                error = form.errorNombre,
                placeholder = "Ej: María González",
                mayusculas = KeyboardCapitalization.Words
            )

            CampoFormulario(
                valor = form.correo,
                onValorChange = viewModel::onRegistroCorreoChange,
                etiqueta = "Correo electrónico",
                icono = Icons.Filled.Email,
                error = form.errorCorreo,
                placeholder = "nombre@gmail.com",
                tipoTeclado = KeyboardType.Email
            )

            CampoFormulario(
                valor = form.telefono,
                onValorChange = viewModel::onTelefonoChange,
                etiqueta = "Teléfono",
                icono = Icons.Filled.Phone,
                error = form.errorTelefono,
                placeholder = "912345678",
                prefijo = "+56 ",
                tipoTeclado = KeyboardType.Phone
            )

            SelectorFormulario(
                valor = form.tipoNegocio,
                opciones = viewModel.opcionesNegocio,
                onSeleccionar = viewModel::onTipoNegocioChange,
                etiqueta = "Tipo de negocio",
                icono = Icons.Filled.Store,
                error = form.errorTipoNegocio
            )

            CampoFormulario(
                valor = form.password,
                onValorChange = viewModel::onRegistroPasswordChange,
                etiqueta = "Contraseña",
                icono = Icons.Filled.Lock,
                error = form.errorPassword,
                ayuda = "Mínimo ${ValidadorFormularios.MIN_PASSWORD} caracteres, con letras y números.",
                esPassword = true
            )

            CampoFormulario(
                valor = form.confirmarPassword,
                onValorChange = viewModel::onConfirmarPasswordChange,
                etiqueta = "Confirmar contraseña",
                icono = Icons.Filled.Lock,
                error = form.errorConfirmar,
                esPassword = true,
                accionTeclado = ImeAction.Done,
                onAccionTeclado = enviar
            )

            // Error general (por ejemplo, si falla el registro)
            form.errorGeneral?.let {
                MensajeFormularioAnimado(texto = it, esError = true)
            }

            Button(
                onClick = enviar,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Gold),
                enabled = !isLoading
            ) {
                Text(
                    text = if (isLoading) "PROCESANDO..." else "REGISTRARSE",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
