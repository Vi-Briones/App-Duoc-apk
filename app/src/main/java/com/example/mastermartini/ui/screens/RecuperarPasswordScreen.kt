package com.example.mastermartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mastermartini.ui.components.CampoFormulario
import com.example.mastermartini.ui.components.MensajeFormulario
import com.example.mastermartini.viewmodel.AuthViewModel

// Paleta de colores corporativa
private val DarkGreen = Color(0xFF003831)
private val Gold = Color(0xFFD4A336)
private val BackgroundGray = Color(0xFFF5F7F6)

@Composable
fun RecuperarPasswordScreen(
    viewModel: AuthViewModel,
    onVolver: () -> Unit
) {
    // El envío corre en el ViewModel: si se gira el celular mientras envía, no queda trabado
    val form = viewModel.recuperar
    val isLoading by viewModel.isLoading.collectAsState()
    val focusManager = LocalFocusManager.current

    val enviar = {
        focusManager.clearFocus()
        viewModel.enviarRecuperacion()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .imePadding()
            .verticalScroll(rememberScrollState())
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
                text = "RECUPERAR CLAVE",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Formulario
        Column(
            modifier = Modifier.padding(32.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ingresa tu correo electrónico y te enviaremos las instrucciones para restablecer tu contraseña.",
                color = Color.Black,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            CampoFormulario(
                valor = form.correo,
                onValorChange = viewModel::onRecuperarCorreoChange,
                etiqueta = "Correo electrónico",
                icono = Icons.Filled.Email,
                error = form.errorCorreo,
                placeholder = "nombre@gmail.com",
                tipoTeclado = KeyboardType.Email,
                accionTeclado = ImeAction.Done,
                onAccionTeclado = enviar,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (form.enviado) {
                MensajeFormulario(texto = "¡Enlace de recuperación enviado!", esError = false)
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
                    text = if (isLoading) "ENVIANDO..." else "ENVIAR ENLACE",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onVolver) {
                Text("Volver al Inicio de Sesión", color = DarkGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}
