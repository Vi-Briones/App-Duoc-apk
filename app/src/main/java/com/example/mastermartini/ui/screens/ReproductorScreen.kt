package com.example.mastermartini.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView
import com.example.mastermartini.model.FuenteVideo
import com.example.mastermartini.ui.components.BotonGuardarGrande
import com.example.mastermartini.ui.components.ColoresMarca
import com.example.mastermartini.ui.components.EncabezadoVerde
import com.example.mastermartini.ui.components.FilaCategorias
import com.example.mastermartini.ui.components.TextoSecundario
import com.example.mastermartini.viewmodel.ReproductorViewModel

/**
 * VIEW: reproducción de un video.
 *
 * Contrato con la navegación:
 *  - videoId: qué video mostrar (llega desde la ruta "reproductor/{videoId}")
 *  - onVolver: qué hacer al apretar "Volver" (lo decide el NavHost)
 */
@Composable
fun ReproductorScreen(
    videoId: Int,
    onVolver: () -> Unit,
    viewModel: ReproductorViewModel = viewModel()
) {
    // 1) Cuando llega un videoId, le pedimos al ViewModel que lo cargue
    LaunchedEffect(videoId) {
        viewModel.cargarVideo(videoId)
    }

    // 2) Si la app pasa a segundo plano (botón Home, llamada...), pausamos el video
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) viewModel.pausar()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // 3) Al salir: escondemos el video ANTES de navegar (si no, queda "flotando")
    var saliendo by remember { mutableStateOf(false) }
    val salir: () -> Unit = {
        saliendo = true
        viewModel.pausar()
        onVolver()
    }
    BackHandler(onBack = salir) // el botón "atrás" del teléfono también usa salir()

    val video = viewModel.video
    val fuente = video?.fuente
    val mensajeError = viewModel.error
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColoresMarca.Fondo)
            .verticalScroll(rememberScrollState())
    ) {
        EncabezadoVerde(titulo = video?.titulo ?: "Cargando video…")

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 4) Zona del video (16:9, fondo negro)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                when {
                    // a) Hubo un error
                    mensajeError != null -> Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = mensajeError,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (viewModel.puedeReintentar) {
                            Button(onClick = { viewModel.reintentar() }) {
                                Text("Reintentar")
                            }
                        }
                    }

                    // b) Video de YouTube: se abre en la app de YouTube
                    fuente is FuenteVideo.YouTube -> Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Este video está en YouTube", color = Color.White)
                        Button(onClick = { uriHandler.openUri(fuente.url) }) {
                            Text("Ver en YouTube")
                        }
                    }

                    // c) Video local o streaming: lo reproduce ExoPlayer
                    else -> {
                        if (!saliendo) {
                            // PlayerView es una vista "antigua" (no Compose); AndroidView es el puente
                            AndroidView(
                                factory = { context ->
                                    PlayerView(context).apply {
                                        player = viewModel.player
                                    }
                                },
                                modifier = Modifier.fillMaxSize(),
                                onRelease = { playerView -> playerView.player = null }
                            )
                            if (viewModel.cargando) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                    }
                }
            }

            // 5) Guardar / quitar de "Mis videos guardados"
            if (video != null) {
                BotonGuardarGrande(
                    guardado = viewModel.guardado,
                    onClick = { viewModel.alternarGuardado() }
                )
            }

            // 6) Información del video
            if (video != null) {
                FilaCategorias(video.categorias)
                TextoSecundario("Duración: ${video.duracion}")
                Text(
                    text = video.descripcion,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // 7) Botón grande y con buen contraste (usuarios mayores, como pide el caso)
            Button(
                onClick = salir,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColoresMarca.Verde,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Volver", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
