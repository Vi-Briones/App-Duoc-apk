package com.example.mastermartini.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/*
 * ⚠️ PANTALLAS TEMPORALES ⚠️
 * Solo sirven para poder navegar y probar el video.
 * Las pantallas reales las hacen tus compañeros.
 * Cuando unan el proyecto: BORRA ESTE ARCHIVO y usa las de ellos
 * (deben recibir los mismos parámetros: onLogin, onIrARegistro, etc.).
 */

@Composable
private fun PantallaBase(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineMedium)
        Text("(pantalla temporal)", style = MaterialTheme.typography.bodyMedium)
        contenido()
    }
}

@Composable
private fun BotonGrande(texto: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().height(56.dp)) {
        Text(texto, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun MenuScreen(onVideos: () -> Unit, onPerfil: () -> Unit) {
    PantallaBase("Menú principal") {
        // En la pantalla real: el botón "VIDEOS TÉCNICOS" y la pestaña "Videos" llaman a onVideos()
        BotonGrande("Videos técnicos", onVideos)
        OutlinedButton(onClick = onPerfil, modifier = Modifier.fillMaxWidth()) {
            Text("Mi perfil")
        }
    }
}

@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit) {
    PantallaBase("Perfil") {
        BotonGrande("Cerrar sesión", onCerrarSesion)
    }
}
