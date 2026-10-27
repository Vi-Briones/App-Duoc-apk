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
 * VIEWMODEL del reproductor.
 *
 * ¿Por qué el reproductor (ExoPlayer) vive aquí y no en la pantalla?
 *  - El ViewModel sobrevive cuando giras el celular: el video NO se reinicia.
 *  - Al salir de la pantalla, Android destruye el ViewModel y llama a onCleared(),
 *    donde liberamos el reproductor (si no, seguiría sonando y gastando memoria).
 *
 * Es AndroidViewModel porque ExoPlayer necesita un Context (el Application) para crearse.
 */
@OptIn(UnstableApi::class)
class ReproductorViewModel(application: Application) : AndroidViewModel(application) {

    // setEnableDecoderFallback: si el decodificador principal falla (pasa en algunos
    // emuladores), ExoPlayer prueba con otro en vez de mostrar error.
    val player: ExoPlayer = ExoPlayer.Builder(
        application,
        DefaultRenderersFactory(application).setEnableDecoderFallback(true)
    ).build()

    // ----- ESTADO que lee la pantalla (private set: solo el ViewModel lo cambia) -----
    var video by mutableStateOf<Video?>(null)
        private set

    var cargando by mutableStateOf(true)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    /** Solo tiene sentido reintentar videos que reproduce ExoPlayer (no YouTube). */
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
        // Si ya está cargado (por ejemplo, después de girar el celular), no hacemos nada
        if (video?.id == videoId) return

        val encontrado = VideoRepository.obtenerVideoPorId(videoId)
        video = encontrado

        if (encontrado == null) {
            error = "Video no encontrado"
            cargando = false
            return
        }
        error = null

        // Según de dónde venga el video, armamos su dirección (URI)
        when (val fuente = encontrado.fuente) {
            is FuenteVideo.Local -> {
                val paquete = getApplication<Application>().packageName
                reproducir(Uri.parse("android.resource://$paquete/${fuente.recurso}"))
            }
            is FuenteVideo.Streaming -> reproducir(Uri.parse(fuente.url))
            is FuenteVideo.YouTube -> {
                // ExoPlayer no reproduce YouTube: la pantalla mostrará un botón para abrirlo
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
