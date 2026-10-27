package com.example.mastermartini.navigation

import androidx.compose.animation.EnterTransition                 // ← CAMBIO (nuevo)
import androidx.compose.animation.ExitTransition                  // ← CAMBIO (nuevo)
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mastermartini.ui.screens.LoginScreen
import com.example.mastermartini.ui.screens.MenuScreen            // ← CAMBIO (antes DashboardScreen)
import com.example.mastermartini.ui.screens.PerfilScreen
import com.example.mastermartini.ui.screens.RegistroScreen
import com.example.mastermartini.ui.screens.RecuperarPasswordScreen
import com.example.mastermartini.ui.screens.ReproductorScreen     // ← CAMBIO (antes VideoScreen)
import com.example.mastermartini.ui.screens.VideosScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mastermartini.viewmodel.AuthViewModel// ← CAMBIO (nuevo)

/** Nombres de las rutas en un solo lugar, para no escribir textos sueltos (y equivocarse). */
object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val RECUPERAR_CLAVE = "recuperar_clave"
    const val MENU = "menu"                                        // ← CAMBIO (antes DASHBOARD)
    const val PERFIL = "perfil"
    const val VIDEOS = "videos"                                    // ← CAMBIO (nuevo)
    const val REPRODUCTOR = "reproductor/{videoId}"                // ← CAMBIO (antes VIDEO)
    fun reproductor(videoId: Int) = "reproductor/$videoId"        // ← CAMBIO (antes video())
}

/**
 * El NavHost decide qué pantalla se muestra.
 * Las pantallas NO conocen al navController: solo reciben lambdas (onLogin, onVolver...).
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN,
        // ← CAMBIO: sin animaciones (el video quedaba flotando al volver)
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {

        composable(Rutas.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLogin = {
                    navController.navigate(Rutas.MENU) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Rutas.REGISTRO) },
                onOlvidoPassword = { navController.navigate(Rutas.RECUPERAR_CLAVE) }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                viewModel = authViewModel,
                onRegistrado = { navController.popBackStack() }
            )
        }

        composable(Rutas.RECUPERAR_CLAVE) {
            RecuperarPasswordScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        // ← CAMBIO: el Dashboard ahora es el Menú principal
        composable(Rutas.MENU) {
            MenuScreen(
                onVideos = { navController.navigate(Rutas.VIDEOS) },
                onPerfil = { navController.navigate(Rutas.PERFIL) }
            )
        }

        // ← CAMBIO: lista de videos (nueva)
        composable(Rutas.VIDEOS) {
            VideosScreen(
                onVideoClick = { id -> navController.navigate(Rutas.reproductor(id)) },
                onVolverMenu = { navController.popBackStack(Rutas.MENU, inclusive = false) }
            )
        }

        // ← CAMBIO: reproductor (antes VideoScreen con cursoId)
        composable(
            route = Rutas.REPRODUCTOR,
            arguments = listOf(navArgument("videoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getInt("videoId") ?: -1
            ReproductorScreen(
                videoId = videoId,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.PERFIL) {
            PerfilScreen(
                onCerrarSesion = {
                    navController.navigate(Rutas.LOGIN) {
                        // Borra TODO el historial: después de cerrar sesión no se puede volver atrás
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }
}
