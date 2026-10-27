package com.example.mastermartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mastermartini.ui.components.ColoresMarca
import com.example.mastermartini.ui.components.ColoresMarca.Fondo
import com.example.mastermartini.ui.components.EncabezadoVerde
import com.example.mastermartini.ui.components.TarjetaVideo
import com.example.mastermartini.viewmodel.VideosViewModel

/**
 * VIEW: lista "VIDEOS TÉCNICOS" con buscador.
 *
 * Contrato con la navegación:
 *  - onVideoClick(id): qué hacer al tocar un video (el NavHost abre el reproductor)
 */
@Composable
fun VideosScreen(
    onVideoClick: (Int) -> Unit,
    onVolverMenu: () -> Unit,
    viewModel: VideosViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
    ) {
        EncabezadoVerde(titulo = "VIDEO RECETAS") {
            TextButton(
                onClick = onVolverMenu,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("<- Volver al menú", color = Color.White, style = MaterialTheme.typography.titleMedium)
            }
            TextField(
                value = viewModel.busqueda,
                onValueChange = { viewModel.cambiarBusqueda(it) },
                placeholder = { Text("Buscar por nombre o categoría") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        val videos = viewModel.videosFiltrados

        if (videos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No encontramos videos para \"${viewModel.busqueda}\"",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(videos, key = { it.id }) { video ->
                    TarjetaVideo(video = video, onClick = { onVideoClick(video.id) })
                }
            }
        }
    }
}
