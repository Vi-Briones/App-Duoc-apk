package com.example.mastermartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mastermartini.ui.components.CampoFormulario
import com.example.mastermartini.ui.components.MensajeFormulario
import com.example.mastermartini.viewmodel.AuthViewModel

// Paleta de colores corporativa
private val DarkGreen = Color(0xFF003831)
private val Gold = Color(0xFFD4A336)

@Composable
fun GuiñoItaliano(modifier: Modifier = Modifier) {
    Row(modifier = modifier.width(80.dp).height(4.dp)) {
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFF009246)))
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color.White))
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFCE2B37)))
    }
}

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLogin: () -> Unit,
    onIrARegistro: () -> Unit,
    onOlvidoPassword: () -> Unit
) {
    // El estado del formulario vive en el ViewModel (sobrevive al giro de pantalla)
    val form = viewModel.login
    val isLoading by viewModel.isLoading.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            viewModel.loginNavegado()
            onLogin()
        }
    }

    val enviar = {
        focusManager.clearFocus()
        viewModel.iniciarSesion()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGreen)
            .imePadding() // Ajusta el espacio al abrir el teclado
            .verticalScroll(rememberScrollState())
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "MASTER",
            color = Gold,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Text(
            text = "Martini",
            color = Color.White,
            fontSize = 48.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        GuiñoItaliano(modifier = Modifier.padding(bottom = 40.dp))

        CampoFormulario(
            valor = form.correo,
            onValorChange = viewModel::onLoginCorreoChange,
            etiqueta = "Correo electrónico",
            icono = Icons.Filled.Email,
            error = form.errorCorreo,
            placeholder = "nombre@gmail.com",
            tipoTeclado = KeyboardType.Email,
            fondoOscuro = true,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        CampoFormulario(
            valor = form.password,
            onValorChange = viewModel::onLoginPasswordChange,
            etiqueta = "Contraseña",
            icono = Icons.Filled.Lock,
            error = form.errorPassword,
            esPassword = true,
            accionTeclado = ImeAction.Done,
            onAccionTeclado = enviar,
            fondoOscuro = true,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = enviar,
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Gold),
            enabled = !isLoading
        ) {
            Text(
                text = if (isLoading) "CARGANDO..." else "INICIAR SESIÓN",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        form.errorGeneral?.let {
            MensajeFormulario(texto = it, esError = true, fondoOscuro = true)
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = onOlvidoPassword) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color.LightGray,
                fontSize = 15.sp
            )
        }

        TextButton(onClick = onIrARegistro) {
            Text(
                text = "Crear una cuenta",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
