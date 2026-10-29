package com.example.mastermartini.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mastermartini.ui.components.ColoresMarca
import com.example.mastermartini.ui.components.EncabezadoVerde
import com.example.mastermartini.ui.components.Poppins

/** Menú principal. Recetas, Cursos y Eventos quedan como "Próximamente". */
@Composable
fun MenuScreen(
    onVideos: () -> Unit,
    onPerfil: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColoresMarca.Fondo)
    ) {
        // Contenido con scroll
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            EncabezadoVerde(titulo = "¡Hola, emprendedor!") {
                Text(
                    text = "¿Qué quieres aprender hoy?",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            CarruselDestacados()

            // Accesos grandes 2 x 2
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AccesoMenu(
                        texto = "RECETAS",
                        icono = Icons.Filled.Restaurant,
                        fondo = ColoresMarca.Verde,
                        colorTexto = Color.White,
                        habilitado = false,
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                    AccesoMenu(
                        texto = "VIDEOS",
                        icono = Icons.Filled.PlayCircle,
                        fondo = ColoresMarca.Dorado,
                        colorTexto = Color.Black,
                        habilitado = true,
                        onClick = onVideos,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AccesoMenu(
                        texto = "CURSOS",
                        icono = Icons.Filled.School,
                        fondo = ColoresMarca.Dorado,
                        colorTexto = Color.Black,
                        habilitado = false,
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                    AccesoMenu(
                        texto = "EVENTOS",
                        icono = Icons.Filled.Event,
                        fondo = ColoresMarca.Verde,
                        colorTexto = Color.White,
                        habilitado = false,
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Barra inferior: Inicio / Videos / Perfil
        BarraInferior(
            onVideos = onVideos,
            onPerfil = onPerfil
        )
    }
}

/** Datos de cada banner del carrusel. */
private data class Destacado(val titulo: String, val subtitulo: String, val colores: List<Color>)

private val destacados = listOf(
    Destacado("#CámbiateAVegetal", "Recetas de temporada", listOf(ColoresMarca.Verde, ColoresMarca.VerdeOscuro)),
    Destacado("Nuevos videos técnicos", "Chocolatería, masas y cremas", listOf(ColoresMarca.Cafe, Color(0xFF3B2418))),
    Destacado("Qualità italiana", "Insumos de Master Martini", listOf(Color(0xFF8C7414), ColoresMarca.Dorado))
)

/** Carrusel de banners con indicador de página. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CarruselDestacados() {
    val estadoPager = rememberPagerState(pageCount = { destacados.size })

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = estadoPager,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp
        ) { pagina ->
            val item = destacados[pagina]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Brush.linearGradient(item.colores), RoundedCornerShape(18.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Text(
                        text = item.titulo,
                        color = Color.White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Text(
                        text = item.subtitulo,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // Puntitos: el de la página actual es dorado
        Row(
            modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(destacados.size) { indice ->
                val activo = estadoPager.currentPage == indice
                Box(
                    modifier = Modifier
                        .size(if (activo) 10.dp else 8.dp)
                        .background(
                            if (activo) ColoresMarca.Dorado else ColoresMarca.Borde,
                            CircleShape
                        )
                )
            }
        }
    }
}

/** Botón grande cuadrado del menú. Si no está habilitado, muestra "Próximamente". */
@Composable
private fun AccesoMenu(
    texto: String,
    icono: ImageVector,
    fondo: Color,
    colorTexto: Color,
    habilitado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        enabled = habilitado,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = fondo,
            disabledContainerColor = fondo.copy(alpha = 0.55f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier.height(130.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null, // el texto de abajo ya dice qué es
                tint = colorTexto,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = texto,
                color = colorTexto,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            )
            if (!habilitado) {
                Text(
                    text = "Próximamente",
                    color = colorTexto,
                    fontSize = 13.sp
                )
            }
        }
    }
}

/** Barra inferior: Inicio, Videos y Perfil. */
@Composable
private fun BarraInferior(
    onVideos: () -> Unit,
    onPerfil: () -> Unit
) {
    val colores = NavigationBarItemDefaults.colors(
        selectedIconColor = ColoresMarca.Verde,
        selectedTextColor = ColoresMarca.Verde,
        unselectedIconColor = ColoresMarca.TextoSecundario,
        unselectedTextColor = ColoresMarca.TextoSecundario,
        indicatorColor = ColoresMarca.Dorado.copy(alpha = 0.25f)
    )
    NavigationBar(
        containerColor = Color.White,
        // Sin margen extra: ya lo pone MainActivity
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            label = { Text("Inicio", fontWeight = FontWeight.Bold) },
            colors = colores
        )
        NavigationBarItem(
            selected = false,
            onClick = onVideos,
            icon = { Icon(Icons.Filled.PlayCircle, contentDescription = null) },
            label = { Text("Videos") },
            colors = colores
        )
        NavigationBarItem(
            selected = false,
            onClick = onPerfil,
            icon = { Icon(Icons.Filled.Person, contentDescription = null) },
            label = { Text("Perfil") },
            colors = colores
        )
    }
}
