package com.example.mastermartini.viewmodel

import android.app.Application
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import com.example.mastermartini.data.GuardadosRepository
import com.example.mastermartini.data.VideoRepository
import com.example.mastermartini.model.FuenteVideo
import com.example.mastermartini.model.Video

/**
 * ViewModel del reproductor.
 * El ExoPlayer vive aquí para que el video no se reinicie al girar la pantalla,
 * y se libera en onCleared() al salir.
 */
@OptIn(UnstableApi::class)
class ReproductorViewModel(application: Application) : AndroidViewModel(application) {

    // setEnableDecoderFallback: si un decodificador falla (pasa en emuladores), prueba otro
    val player: ExoPlayer = ExoPlayer.Builder(
        application,
        DefaultRenderersFactory(application).setEnableDecoderFallback(true)
    ).build()

    // ----- Estado que lee la pantalla -----
    var video by mutableStateOf<Video?>(null)
        private set

    var cargando by mutableStateOf(true)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    /** Solo se puede reintentar si no es de YouTube. */
    val puedeReintentar: Boolean
        get() = video != null && video?.fuente !is FuenteVideo.YouTube

    /** ¿El video actual está en "Mis videos guardados"? */
    val guardado: Boolean
        get() = video?.let { GuardadosRepository.estaGuardado(it.id) } ?: false

    // Escucha lo que pasa en el reproductor para actualizar el estado
    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            cargando = playbackState == Player.STATE_BUFFERING
        }

        override fun onPlayerError(error: PlaybackException) {
            this@ReproductorViewModel.error =
                if (video?.fuente is FuenteVideo.Streaming) {
                    "No se pudo cargar el video. Revisa tu conexión a internet."
                } else {
                    "No se pudo reproducir el video."
                }
            cargando = false
        }
    }

    init {
        player.addListener(listener)
    }

    /** Busca el video por id y lo prepara según su fuente. */
    fun cargarVideo(videoId: Int) {
        // Si ya estaba cargado (por ejemplo al girar), no hace nada
        if (video?.id == videoId) return

        val encontrado = VideoRepository.obtenerVideoPorId(videoId)
        video = encontrado

        if (encontrado == null) {
            error = "Video no encontrado"
            cargando = false
            return
        }
        error = null

        // Arma la URI según de dónde viene el video
        when (val fuente = encontrado.fuente) {
            is FuenteVideo.Local -> {
                val paquete = getApplication<Application>().packageName
                reproducir(Uri.parse("android.resource://$paquete/${fuente.recurso}"))
            }
            is FuenteVideo.Streaming -> reproducir(Uri.parse(fuente.url))
            is FuenteVideo.YouTube -> {
                // ExoPlayer no reproduce YouTube, la pantalla muestra un botón
                player.stop()
                cargando = false
            }
        }
    }

    private fun reproducir(uri: Uri) {
        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()            // carga el video
        player.playWhenReady = true // y lo reproduce apenas esté listo
    }

    fun reintentar() {
        if (!puedeReintentar) return
        error = null
        cargando = true
        player.prepare()
        player.playWhenReady = true
    }

    fun alternarGuardado() {
        video?.let { GuardadosRepository.alternar(it.id) }
    }

    fun pausar() {
        player.pause()
    }

    /** Se llama cuando el usuario sale de la pantalla del reproductor. */
    override fun onCleared() {
        player.removeListener(listener)
        player.release()
        super.onCleared()
    }
}
