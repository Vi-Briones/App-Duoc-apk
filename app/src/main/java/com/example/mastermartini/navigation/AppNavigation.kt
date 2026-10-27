package com.example.mastermartini.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mastermartini.ui.screens.LoginScreen
import com.example.mastermartini.ui.screens.MenuScreen
import com.example.mastermartini.ui.screens.PerfilScreen
import com.example.mastermartini.ui.screens.RecuperarPasswordScreen
import com.example.mastermartini.ui.screens.RegistroScreen
import com.example.mastermartini.ui.screens.ReproductorScreen
import com.example.mastermartini.ui.screens.VideosScreen
import com.example.mastermartini.viewmodel.AuthViewModel

/** Nombres de las rutas en un solo lugar, para no escribir textos sueltos (y equivocarse). */
object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val RECUPERAR_CLAVE = "recuperar_clave"
    const val MENU = "menu"
    const val PERFIL = "perfil"
    const val VIDEOS = "videos"
    const val REPRODUCTOR = "reproductor/{videoId}"
    fun reproductor(videoId: Int) = "reproductor/$videoId"
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
        // Sin animaciones (el video quedaba flotando al volver)
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
                        // ← CAMBIO v6: borra TODO el historial
                        // (ni el login ni un perfil "sin sesión" quedan atrás)
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Rutas.REGISTRO) },
                onOlvidoPassword = { navController.navigate(Rutas.RECUPERAR_CLAVE) }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                viewModel = authViewModel,
                // ← CAMBIO v6: después de registrarse va al login
                // (funciona tanto desde el inicio como desde el perfil)
                onRegistrado = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.REGISTRO) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Rutas.RECUPERAR_CLAVE) {
            RecuperarPasswordScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.MENU) {
            MenuScreen(
                onVideos = { navController.navigate(Rutas.VIDEOS) },
                onPerfil = { navController.navigate(Rutas.PERFIL) }
            )
        }

        composable(Rutas.VIDEOS) {
            VideosScreen(
                onVideoClick = { id -> navController.navigate(Rutas.reproductor(id)) },
                onVolverMenu = { navController.popBackStack(Rutas.MENU, inclusive = false) }
            )
        }

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
                onVideoClick = { id -> navController.navigate(Rutas.reproductor(id)) },
                onIrAVideos = { navController.navigate(Rutas.VIDEOS) },
                onVolverMenu = { navController.popBackStack(Rutas.MENU, inclusive = false) },
                // ← CAMBIO v6: ya no hay onCerrarSesion (el perfil cierra la sesión solo
                // y se queda en modo "sin sesión"); ahora tiene estas dos acciones:
                onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
                onRegistrarse = { navController.navigate(Rutas.REGISTRO) }
            )
        }
    }
}
