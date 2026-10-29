package com.example.mastermartini.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mastermartini.data.UsuarioSesion
import com.example.mastermartini.model.Video
import com.example.mastermartini.ui.components.ColoresMarca
import com.example.mastermartini.ui.components.EncabezadoVerde
import com.example.mastermartini.ui.components.Poppins
import com.example.mastermartini.ui.components.TarjetaGuardada
import com.example.mastermartini.viewmodel.PerfilViewModel


/** Perfil del usuario. Sin sesión muestra los botones para entrar o registrarse. */
@Composable
fun PerfilScreen(
    onVideoClick: (Int) -> Unit,
    onIrAVideos: () -> Unit,
    onVolverMenu: () -> Unit,
    onIniciarSesion: () -> Unit,
    onRegistrarse: () -> Unit,
    viewModel: PerfilViewModel = viewModel()
) {
    val usuario = viewModel.usuario

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColoresMarca.Fondo)
            .verticalScroll(rememberScrollState())
    ) {
        // Encabezado con el avatar
        EncabezadoVerde(titulo = "MI PERFIL") {
            Avatar(iniciales = viewModel.iniciales)
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (usuario != null) {
                ContenidoConSesion(
                    usuario = usuario,
                    guardados = viewModel.videosGuardados,
                    onVideoClick = onVideoClick,
                    onQuitarGuardado = { id -> viewModel.quitarGuardado(id) },
                    onIrAVideos = onIrAVideos
                )
            } else {
                ContenidoSinSesion(
                    onIniciarSesion = onIniciarSesion,
                    onRegistrarse = onRegistrarse
                )
            }

            // Siempre visible
            BotonPrincipal(texto = "Volver al menú", onClick = onVolverMenu)

            // Cerrar sesión (solo si hay sesión)
            if (usuario != null) {
                TextButton(
                    onClick = { viewModel.cerrarSesion() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = ColoresMarca.Verde
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cerrar sesión",
                        color = ColoresMarca.Verde,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}

/** Círculo con las iniciales; si no hay sesión, muestra el ícono de persona. */
@Composable
private fun Avatar(iniciales: String) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .border(3.dp, ColoresMarca.Dorado, CircleShape)
            .padding(4.dp)
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (iniciales.isNotBlank()) {
            Text(
                text = iniciales,
                color = ColoresMarca.Verde,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = ColoresMarca.Verde,
                modifier = Modifier.size(52.dp)
            )
        }
    }
}

/** Perfil con sesión iniciada. */
@Composable
private fun ColumnScope.ContenidoConSesion(
    usuario: UsuarioSesion,
    guardados: List<Video>,
    onVideoClick: (Int) -> Unit,
    onQuitarGuardado: (Int) -> Unit,
    onIrAVideos: () -> Unit
) {
    // Nombre
    Text(
        text = usuario.nombre,
        color = ColoresMarca.TextoPrincipal,
        fontFamily = Poppins,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    // Datos de contacto
    TarjetaBlanca {
        DatoPerfil("Correo", usuario.correo)
        DatoPerfil("Teléfono", usuario.telefono)
        DatoPerfil("Tipo de negocio", usuario.tipoNegocio)
    }

    // Mis videos guardados
    Text(
        text = "Mis videos guardados (${guardados.size})",
        color = ColoresMarca.TextoPrincipal,
        fontFamily = Poppins,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )

    if (guardados.isEmpty()) {
        TarjetaBlanca(centrado = true) {
            Text(
                text = "Aún no tienes videos guardados.\nToca el marcador de un video para guardarlo aquí.",
                color = ColoresMarca.TextoPrincipal,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            BotonSecundario(texto = "Explorar videos", onClick = onIrAVideos)
        }
    } else {
        LazyRow(
            contentPadding = PaddingValues(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(guardados, key = { it.id }) { video ->
                TarjetaGuardada(
                    video = video,
                    onClick = { onVideoClick(video.id) },
                    onQuitar = { onQuitarGuardado(video.id) }
                )
            }
        }
        Text(
            text = "Toca el marcador para quitar un video de tus guardados.",
            color = ColoresMarca.TextoSecundario,
            fontSize = 14.sp
        )
    }
}

/** Perfil sin sesión: invita a entrar o registrarse. */
@Composable
private fun ContenidoSinSesion(
    onIniciarSesion: () -> Unit,
    onRegistrarse: () -> Unit
) {
    TarjetaBlanca(centrado = true) {
        Text(
            text = "No has iniciado sesión",
            color = ColoresMarca.TextoPrincipal,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Inicia sesión o crea una cuenta para ver tus datos y guardar tus videos favoritos.",
            color = ColoresMarca.TextoPrincipal,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        BotonPrincipal(texto = "Iniciar sesión", onClick = onIniciarSesion)
        BotonSecundario(texto = "Crear cuenta", onClick = onRegistrarse)
    }
}

// Componentes de esta pantalla

@Composable
private fun TarjetaBlanca(
    centrado: Boolean = false,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = ColoresMarca.TextoPrincipal   // texto oscuro siempre
        ),
        border = BorderStroke(1.dp, ColoresMarca.Borde),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = if (centrado) Alignment.CenterHorizontally else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = contenido
        )
    }
}

@Composable
private fun BotonPrincipal(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = ColoresMarca.Verde,
            contentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(texto, fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 17.sp)
    }
}

@Composable
private fun BotonSecundario(texto: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(2.dp, ColoresMarca.Verde),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = ColoresMarca.Verde),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(texto, fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 16.sp)
    }
}

/** Una línea "Etiqueta: valor" con la etiqueta en negrita. */
@Composable
private fun DatoPerfil(etiqueta: String, valor: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$etiqueta: ")
            }
            append(valor)
        },
        color = ColoresMarca.TextoPrincipal,
        fontSize = 16.sp
    )
}
