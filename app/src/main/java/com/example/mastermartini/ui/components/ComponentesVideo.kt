package com.example.mastermartini.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mastermartini.R
import com.example.mastermartini.model.Video

/**
 * Colores de Master Martini usados en la app.
 * Están en un "object" propio para no chocar con el Color.kt del tema.
 */
object ColoresMarca {
    val Verde = Color(0xFF004D42)
    val VerdeOscuro = Color(0xFF00302A)
    val Dorado = Color(0xFFC1A01D)
    val Cafe = Color(0xFF5B3A29)
    val Fondo = Color(0xFFF6F4EF)          // crema suave: hace resaltar las tarjetas blancas
    val Borde = Color(0xFFE3DFD6)
    val TextoPrincipal = Color(0xFF1C1B1B)
    val TextoSecundario = Color(0xFF5F6B69)
    val VerdeItalia = Color(0xFF279046)
    val RojoItalia = Color(0xFFBD191F)
}

/**
 * Fuente Poppins para títulos (los archivos están en res/font).
 * El texto normal sigue con la fuente del sistema, que es la más fácil de leer.
 */
val Poppins = FontFamily(
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

/** Franja verde-blanco-rojo, como la que está bajo el logo de Master Martini. */
@Composable
fun FranjaItaliana(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        listOf(ColoresMarca.VerdeItalia, Color.White, ColoresMarca.RojoItalia).forEach { color ->
            Box(
                modifier = Modifier
                    .width(22.dp)
                    .height(4.dp)
                    .background(color)
            )
        }
    }
}

/** Encabezado verde con bordes redondeados abajo, título en Poppins y franja italiana. */
@Composable
fun EncabezadoVerde(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = ColoresMarca.Verde,
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
            )
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = titulo,
            color = Color.White,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        FranjaItaliana()
        contenido()
    }
}

/** Círculo blanco con el ícono de "play". */
@Composable
fun IconoPlay(modifier: Modifier = Modifier, tamano: Dp = 60.dp) {
    Box(
        modifier = modifier
            .size(tamano)
            .background(Color.White.copy(alpha = 0.92f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = null, // decorativo: la tarjeta ya dice qué video es
            tint = ColoresMarca.TextoPrincipal,
            modifier = Modifier.size(tamano * 0.6f)
        )
    }
}

/**
 * Ícono de marcador (Material Icons).
 * Relleno = guardado. Solo el borde = no guardado.
 */
@Composable
fun IconoMarcador(
    guardado: Boolean,
    color: Color,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = if (guardado) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
        contentDescription = null, // la descripción la pone el botón que lo contiene
        tint = color,
        modifier = modifier.size(26.dp)
    )
}

/** Botón pequeño con el marcador (para tarjetas). Área táctil de 48dp. */
@Composable
fun BotonGuardar(
    guardado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = ColoresMarca.Verde
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.semantics {
            contentDescription = if (guardado) "Quitar de guardados" else "Guardar video"
        }
    ) {
        IconoMarcador(guardado = guardado, color = color)
    }
}

/** Botón grande con texto (para el reproductor): más claro para usuarios mayores. */
@Composable
fun BotonGuardarGrande(guardado: Boolean, onClick: () -> Unit) {
    val colorContenido = if (guardado) Color.White else ColoresMarca.Verde
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(2.dp, ColoresMarca.Verde),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (guardado) ColoresMarca.Verde else Color.Transparent,
            contentColor = colorContenido
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        IconoMarcador(guardado = guardado, color = colorContenido)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = if (guardado) "Guardado en mi perfil" else "Guardar video",
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}

/**
 * Etiqueta de categoría. Alterna dorado y café como en el mockup,
 * pero con colores de texto que se leen bien (accesibilidad).
 */
@Composable
fun ChipCategoria(texto: String, indice: Int) {
    val fondo = if (indice % 2 == 0) ColoresMarca.Dorado else ColoresMarca.Cafe
    val colorTexto = if (indice % 2 == 0) Color.Black else Color.White
    Text(
        text = texto,
        color = colorTexto,
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        modifier = Modifier
            .background(fondo, RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 5.dp)
    )
}

@Composable
fun FilaCategorias(categorias: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        categorias.forEachIndexed { indice, categoria ->
            ChipCategoria(categoria, indice)
        }
    }
}

/** Miniatura 16:9 con degradado verde (o foto), botón de play y la duración en una esquina. */
@Composable
fun MiniaturaVideo(
    video: Video,
    modifier: Modifier = Modifier,
    tamanoPlay: Dp = 60.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(
                Brush.verticalGradient(listOf(ColoresMarca.Verde, ColoresMarca.VerdeOscuro))
            ),
        contentAlignment = Alignment.Center
    ) {
        val miniatura = video.miniatura
        if (miniatura != null) {
            Image(
                painter = painterResource(id = miniatura),
                contentDescription = null, // el título ya describe el video
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Sin foto: la categoría sobre el degradado de la marca
            Text(
                text = video.categorias.firstOrNull() ?: "Video",
                color = ColoresMarca.Dorado,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            )
        }

        IconoPlay(tamano = tamanoPlay)

        // Duración estilo YouTube (abajo a la derecha)
        Text(
            text = video.duracion,
            color = Color.White,
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

/** Tarjeta de la lista: miniatura arriba; título, botón guardar y categorías abajo. */
@Composable
fun TarjetaVideo(
    video: Video,
    guardado: Boolean,
    onAlternarGuardado: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, ColoresMarca.Borde),
        modifier = Modifier.fillMaxWidth()
    ) {
        MiniaturaVideo(video)
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = video.titulo,
                    color = ColoresMarca.TextoPrincipal,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    lineHeight = 24.sp,
                    modifier = Modifier.weight(1f)
                )
                BotonGuardar(guardado = guardado, onClick = onAlternarGuardado)
            }
            FilaCategorias(video.categorias)
        }
    }
}

/**
 * Tarjeta pequeña para "Mis videos guardados" del perfil.
 * Tocarla abre el video; el marcador lo quita de guardados.
 */
@Composable
fun TarjetaGuardada(
    video: Video,
    onClick: () -> Unit,
    onQuitar: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, ColoresMarca.Borde),
        modifier = Modifier.width(200.dp)
    ) {
        Box {
            MiniaturaVideo(video = video, tamanoPlay = 40.dp)
            // Marcador sobre la miniatura, con fondo blanco para que se vea
            BotonGuardar(
                guardado = true,
                onClick = onQuitar,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
            )
        }
        Text(
            text = video.titulo,
            color = ColoresMarca.TextoPrincipal,
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(10.dp)
        )
    }
}

/** Texto gris para datos secundarios (ej: "Duración"). */
@Composable
fun TextoSecundario(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.bodyMedium,
        color = ColoresMarca.TextoSecundario
    )
}
