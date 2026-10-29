package com.example.mastermartini.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores de Master Martini en el esquema de Material 3
private val ColoresMasterMartini = lightColorScheme(
    primary = Color(0xFF004D42),      // verde: botones principales
    onPrimary = Color.White,
    secondary = Color(0xFFC1A01D),    // dorado: acentos
    onSecondary = Color.Black,
    background = Color(0xFFF6F4EF),   // crema
    onBackground = Color(0xFF1C1B1B),
    surface = Color.White,            // tarjetas y campos
    onSurface = Color(0xFF1C1B1B)
)

// Sin colores dinámicos (Android 12+ cambiaba los colores de la marca)
// y siempre claro (en modo oscuro había textos invisibles)
@Composable
fun MasterMartiniTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColoresMasterMartini,
        typography = Typography,
        content = content
    )
}