package com.example.mastermartini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView
import com.example.mastermartini.viewmodel.VideoViewModel

/**
 * VIEW: pantalla de reproducción de video.
 *
 * Contrato con la navegación:
 *  - cursoId: qué curso mostrar (llega desde la ruta "video/{cursoId}")
 *  - onVolver: qué hacer al apretar "Volver" (lo decide el NavHost, no esta pantalla)
 */
@Composable
fun VideoScreen(
    cursoId: Int,
    onVolver: () -> Unit,
    viewModel: VideoViewModel = viewModel()
) {
    // 1) Cuando llega un cursoId, le pedimos al ViewModel que cargue ese video
    LaunchedEffect(cursoId) {
        viewModel.cargarCurso(cursoId)
    }

    // 2) Si la app pasa a segundo plano (botón Home, llamada, etc.), pausamos el video
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) viewModel.pausar()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val curso = viewModel.curso

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = curso?.titulo ?: "Cargando curso…",
            style = MaterialTheme.typography.headlineMedium
        )

        // 3) El reproductor, en formato 16:9 y fondo negro
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            val mensajeError = viewModel.error
            if (mensajeError == null) {
                // PlayerView es una vista "antigua" (no Compose); AndroidView es el puente
                AndroidView(
                    factory = { context ->
                        PlayerView(context).apply {
                            player = viewModel.player // conectamos la vista con el reproductor
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    // Al salir, desconectamos la vista del reproductor
                    onRelease = { playerView -> playerView.player = null }
                )
                if (viewModel.cargando) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                Text(
                    text = mensajeError,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // 4) Información del curso
        curso?.let {
            Text(
                text = "${it.categoria} · ${it.duracion}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = it.descripcion,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // 5) Botón grande (pensado en usuarios mayores, como pide el caso)
        Button(
            onClick = onVolver,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Volver a los cursos", style = MaterialTheme.typography.titleMedium)
        }
    }
}
