package com.example.mastermartini.viewmodel

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.mastermartini.data.CursoRepository
import com.example.mastermartini.model.Curso

/**
 * VIEWMODEL de la pantalla de video.
 *
 * ¿Por qué el reproductor vive aquí y no en la pantalla?
 *  - El ViewModel sobrevive cuando giras el celular, así que el video NO se reinicia.
 *  - Cuando sales de la pantalla, Android destruye el ViewModel y se llama onCleared(),
 *    donde liberamos el reproductor (si no, sigue sonando y gastando memoria).
 *
 * Es AndroidViewModel (y no ViewModel) porque ExoPlayer necesita un Context para crearse,
 * y AndroidViewModel nos da el Application de forma segura.
 */
class VideoViewModel(application: Application) : AndroidViewModel(application) {

    // El reproductor. La pantalla solo lo "conecta" a la vista; quien lo maneja es el ViewModel.
    val player: ExoPlayer = ExoPlayer.Builder(application).build()

    // ----- ESTADO que la pantalla lee (private set: solo el ViewModel lo cambia) -----
    var curso by mutableStateOf<Curso?>(null)
        private set

    var cargando by mutableStateOf(true)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    // Escucha lo que pasa en el reproductor para actualizar el estado
    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            // BUFFERING = el video se está cargando
            cargando = playbackState == Player.STATE_BUFFERING
        }

        override fun onPlayerError(error: PlaybackException) {
            this@VideoViewModel.error = "No se pudo reproducir el video"
            cargando = false
        }
    }

    init {
        player.addListener(listener)
    }

    /** Busca el curso por id y prepara su video. */
    fun cargarCurso(cursoId: Int) {
        // Si ya está cargado (por ejemplo, después de girar el celular), no hacemos nada
        if (curso?.id == cursoId) return

        val encontrado = CursoRepository.obtenerCursoPorId(cursoId)
        curso = encontrado

        if (encontrado == null) {
            error = "Curso no encontrado"
            cargando = false
            return
        }

        error = null

        // Dirección del video dentro de la app: android.resource://<paquete>/<id del recurso>
        val paquete = getApplication<Application>().packageName
        val uri = Uri.parse("android.resource://$paquete/${encontrado.videoRes}")

        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()            // carga el video
        player.playWhenReady = true // y lo reproduce apenas esté listo
    }

    fun pausar() {
        player.pause()
    }

    /** Se llama cuando el usuario sale de la pantalla de video. */
    override fun onCleared() {
        player.removeListener(listener)
        player.release()
        super.onCleared()
    }
}
