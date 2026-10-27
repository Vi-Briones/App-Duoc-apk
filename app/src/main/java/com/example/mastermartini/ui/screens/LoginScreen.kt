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
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = Color.Gray) },
            visualTransformation = PasswordVisualTransformation(),
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
            modifier = Modifier.padding(bottom = 16.dp).clickable { onOlvidoPassword() }
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