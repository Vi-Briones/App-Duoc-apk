package com.example.mastermartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Patterns

// Paleta de colores corporativa
private val DarkGreen = Color(0xFF003831)
private val Gold = Color(0xFFD4A336)
private val BackgroundGray = Color(0xFFF5F7F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarPasswordScreen(
    onVolver: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // 1. CORRECCIÓN DEL MODO OSCURO PARA LA CAJA DE TEXTO
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = DarkGreen,
        unfocusedBorderColor = Color.LightGray,
        cursorColor = DarkGreen,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )
    val blackTextStyle = TextStyle(color = Color.Black, fontSize = 16.sp)

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundGray)
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
            // 2. TEXTO INSTRUCTIVO FORZADO A NEGRO
            Text(
                text = "Ingresa tu correo electrónico y te enviaremos las instrucciones para restablecer tu contraseña.",
                color = Color.Black,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 3. TÍTULO DEL CAMPO FORZADO A NEGRO Y SEPARADO DE LA CAJA
            Text(
                text = "Correo Electrónico",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp).align(Alignment.Start)
            )

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it; mensaje = "" },
                textStyle = blackTextStyle,
                colors = textFieldColors,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Mostrar el mensaje de error o éxito
            if (mensaje.isNotEmpty()) {
                Text(
                    text = mensaje,
                    color = if (mensaje.contains("enviado")) DarkGreen else Color(0xFFCE2B37),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {
                    if (correo.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) {
                        mensaje = "Ingresa un correo válido (ej: nombre@gmail.com)"
                    } else {
                        scope.launch {
                            isLoading = true
                            delay(1500) // Simulamos envío
                            isLoading = false
                            mensaje = "¡Enlace de recuperación enviado con éxito!"
                            correo = ""
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
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