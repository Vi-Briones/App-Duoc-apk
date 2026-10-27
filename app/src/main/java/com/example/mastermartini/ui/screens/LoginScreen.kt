package com.example.mastermartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mastermartini.viewmodel.AuthViewModel
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path

// Paleta de colores corporativa
private val DarkGreen = Color(0xFF003831)
private val Gold = Color(0xFFD4A336)

@Composable
fun GuiñoItaliano(modifier: Modifier = Modifier) {
    Row(modifier = modifier.width(80.dp).height(4.dp)) {
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFF009246))) // Verde
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color.White))       // Blanco
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFCE2B37))) // Rojo
    }
}

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLogin: () -> Unit,
    onIrARegistro: () -> Unit,
    onOlvidoPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()

    LaunchedEffect(loginSuccess) {
        if (loginSuccess == true) {
            onLogin()
        }
    }

    // Estilo de los campos de texto
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = Gold,
        unfocusedBorderColor = Color.Transparent,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGreen)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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

        GuiñoItaliano(modifier = Modifier.padding(bottom = 48.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Correo electrónico", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = Color.Gray) },
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Contraseña", color = Color.Gray) },

            visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),

            trailingIcon = {
                val icon = if (passwordVisible) {
                    // Vector del Ojo Abierto
                    ImageVector.Builder(
                        name = "Visibility", defaultWidth = 24.dp, defaultHeight = 24.dp,
                        viewportWidth = 24f, viewportHeight = 24f
                    ).path(fill = SolidColor(Color.Gray)) {
                        moveTo(12f, 4.5f); curveTo(7f, 4.5f, 2.73f, 7.61f, 1f, 12f)
                        curveToRelative(1.73f, 4.39f, 6f, 7.5f, 11f, 7.5f)
                        reflectiveCurveToRelative(9.27f, -3.11f, 11f, -7.5f)
                        curveToRelative(-1.73f, -4.39f, -6f, -7.5f, -11f, -7.5f)
                        close()
                        moveTo(12f, 17f); curveToRelative(-2.76f, 0f, -5f, -2.24f, -5f, -5f)
                        reflectiveCurveToRelative(2.24f, -5f, 5f, -5f)
                        reflectiveCurveToRelative(5f, 2.24f, 5f, 5f)
                        reflectiveCurveToRelative(-2.24f, 5f, -5f, 5f)
                        close()
                        moveTo(12f, 9f); curveToRelative(-1.66f, 0f, -3f, 1.34f, -3f, 3f)
                        reflectiveCurveToRelative(1.34f, 3f, 3f, 3f)
                        reflectiveCurveToRelative(3f, -1.34f, 3f, -3f)
                        reflectiveCurveToRelative(-1.34f, -3f, -3f, -3f)
                        close()
                    }.build()
                } else {
                    // Vector del Ojo Tachado (Cerrado)
                    ImageVector.Builder(
                        name = "VisibilityOff", defaultWidth = 24.dp, defaultHeight = 24.dp,
                        viewportWidth = 24f, viewportHeight = 24f
                    ).path(fill = SolidColor(Color.Gray)) {
                        moveTo(12f, 7f); curveToRelative(2.76f, 0f, 5f, 2.24f, 5f, 5f)
                        curveToRelative(0f, 0.65f, -0.13f, 1.26f, -0.36f, 1.83f)
                        lineToRelative(2.92f, 2.92f); curveToRelative(1.51f, -1.26f, 2.7f, -2.89f, 3.43f, -4.75f)
                        curveToRelative(-1.73f, -4.39f, -6f, -7.5f, -11f, -7.5f)
                        curveToRelative(-1.4f, 0f, -2.74f, 0.25f, -3.98f, 0.7f)
                        lineToRelative(2.16f, 2.16f); curveTo(10.74f, 7.13f, 11.35f, 7f, 12f, 7f)
                        close()
                        moveTo(2f, 4.27f); lineToRelative(2.28f, 2.28f); lineToRelative(0.46f, 0.46f)
                        curveTo(3.08f, 8.3f, 1.78f, 10.02f, 1f, 12f)
                        curveToRelative(1.73f, 4.39f, 6f, 7.5f, 11f, 7.5f)
                        curveToRelative(1.55f, 0f, 3.03f, -0.3f, 4.38f, -0.84f)
                        lineToRelative(0.42f, 0.42f); lineTo(19.73f, 22f); lineTo(21f, 20.73f)
                        lineTo(3.27f, 3f); lineTo(2f, 4.27f)
                        close()
                        moveTo(7.53f, 9.8f); lineToRelative(1.55f, 1.55f)
                        curveToRelative(-0.05f, 0.21f, -0.08f, 0.43f, -0.08f, 0.65f)
                        curveToRelative(0f, 1.66f, 1.34f, 3f, 3f, 3f)
                        curveToRelative(0.22f, 0f, 0.44f, -0.03f, 0.65f, -0.08f)
                        lineToRelative(1.55f, 1.55f); curveToRelative(-0.67f, 0.33f, -1.41f, 0.53f, -2.2f, 0.53f)
                        curveToRelative(-2.76f, 0f, -5f, -2.24f, -5f, -5f)
                        curveToRelative(0f, -0.79f, 0.2f, -1.53f, 0.53f, -2.2f)
                        close()
                        moveTo(11.84f, 9.02f); lineToRelative(3.15f, 3.15f); lineToRelative(0.02f, -0.16f)
                        curveToRelative(0f, -1.66f, -1.34f, -3f, -3f, -3f); lineToRelative(-0.17f, 0.01f)
                        close()
                    }.build()
                }

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(

                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        imageVector = icon,
                        tint = Color.Gray
                    )
                }
            },
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
        )

        Button(
            onClick = { viewModel.login(email, password) },
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

        if (loginSuccess == false) {
            Text("Credenciales incorrectas", color = Color(0xFFCE2B37), modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = Color.LightGray,
            fontSize = 14.sp,
            modifier = Modifier
                .clickable { onOlvidoPassword() }
                .padding(8.dp)
        )

        Text(
            text = "Crear una cuenta",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onIrARegistro() }
        )
    }
}