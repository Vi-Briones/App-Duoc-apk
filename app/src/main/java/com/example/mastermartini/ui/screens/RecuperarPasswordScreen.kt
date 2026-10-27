package com.example.mastermartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = DarkGreen,
        unfocusedBorderColor = Color.LightGray,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )

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
            Text(
                text = "Ingresa tu correo electrónico y te enviaremos las instrucciones para restablecer tu contraseña.",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it; mensaje = "" },
                label = { Text("Correo Electrónico") },
                colors = textFieldColors,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Mostrar el mensaje de error o éxito
            if (mensaje.isNotEmpty()) {
                Text(
                    text = mensaje,
                    // Si el mensaje dice "enviado", lo ponemos verde, si es error, rojo
                    color = if (mensaje.contains("enviado")) DarkGreen else Color(0xFFCE2B37),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {
                    if (correo.isBlank() || !correo.trim().lowercase().endsWith("@mastermartini.cl")) {
                        mensaje = "Ingresa un correo válido de @mastermartini.cl"
                    } else {
                        scope.launch {
                            isLoading = true
                            delay(1500) // Simulamos que se está enviando el correo
                            isLoading = false
                            mensaje = "¡Enlace de recuperación enviado con éxito!"
                            correo = "" // Limpiamos el campo
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