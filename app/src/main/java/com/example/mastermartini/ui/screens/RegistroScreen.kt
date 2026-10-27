package com.example.mastermartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mastermartini.viewmodel.AuthViewModel
import android.util.Patterns

// Paleta de colores corporativa
private val DarkGreen = Color(0xFF003831)
private val Gold = Color(0xFFD4A336)
private val BackgroundGray = Color(0xFFF5F7F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    viewModel: AuthViewModel,
    onRegistrado: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estados para el menú desplegable
    var expanded by remember { mutableStateOf(false) }
    var tipoNegocio by remember { mutableStateOf("") }
    val opcionesNegocio = listOf("Pastelería", "Panadería", "Chocolatería", "Heladería", "Restaurante")

    var errorMessage by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val registerSuccess by viewModel.registerSuccess.collectAsState()

    LaunchedEffect(registerSuccess) {
        if (registerSuccess == true) {
            viewModel.resetRegisterState()
            onRegistrado()
        } else if (registerSuccess == false) {
            errorMessage = "Error en el sistema. Inténtalo de nuevo."
        }
    }

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
                text = "REGÍSTRATE",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Formulario deslizable
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            Text("Nombre Completo", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it; errorMessage = "" },
                textStyle = blackTextStyle,
                colors = textFieldColors,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Text("Correo Electrónico", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it; errorMessage = "" },
                textStyle = blackTextStyle,
                colors = textFieldColors,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Text("Teléfono", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it; errorMessage = "" },
                textStyle = blackTextStyle,
                colors = textFieldColors,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Text("Tipo de Negocio", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = tipoNegocio,
                    onValueChange = {},
                    readOnly = true,
                    textStyle = blackTextStyle,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.menuAnchor().fillMaxWidth().padding(bottom = 16.dp)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    opcionesNegocio.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion, color = Color.Black) },
                            onClick = {
                                tipoNegocio = opcion
                                expanded = false
                                errorMessage = ""
                            }
                        )
                    }
                }
            }

            Text("Contraseña", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = "" },
                textStyle = blackTextStyle,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) {
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
                            imageVector = icon,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = Color.Gray
                        )
                    }
                },
                colors = textFieldColors,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            // Mensaje de error
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFCE2B37),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = {
                    when {
                        nombre.isBlank() -> errorMessage = "El nombre completo es obligatorio."
                        correo.isBlank() -> errorMessage = "El correo electrónico es obligatorio."
                        !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> errorMessage = "Ingresa un correo válido (ej: nombre@gmail.com)"
                        telefono.isBlank() -> errorMessage = "El teléfono es obligatorio."
                        tipoNegocio.isBlank() -> errorMessage = "Selecciona un tipo de negocio."
                        password.isBlank() -> errorMessage = "La contraseña es obligatoria."
                        else -> {
                            errorMessage = ""
                            viewModel.register(nombre, correo, telefono, password)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
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