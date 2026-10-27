package com.example.mastermartini.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
 * ⚠️ TEMPORAL: solo el Menú y el Perfil, que todavía no entregan tus compañeros.
 * Cuando suban los suyos, BORRA ESTE ARCHIVO.
 * Sus pantallas deben recibir los mismos parámetros:
 *   MenuScreen(onVideos: () -> Unit, onPerfil: () -> Unit)
 *   PerfilScreen(onCerrarSesion: () -> Unit)
 */

@Composable
fun MenuScreen(onVideos: () -> Unit, onPerfil: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Menú principal", style = MaterialTheme.typography.headlineMedium)
        Text("(pantalla temporal)", style = MaterialTheme.typography.bodyMedium)
        Button(onClick = onVideos, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("Videos técnicos")
        }
        OutlinedButton(onClick = onPerfil, modifier = Modifier.fillMaxWidth()) {
            Text("Mi perfil")
        }
    }
}

@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Perfil", style = MaterialTheme.typography.headlineMedium)
        Text("(pantalla temporal)", style = MaterialTheme.typography.bodyMedium)
        Button(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("Cerrar sesión")
        }
    }
}
