package com.example.mastermartini.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mastermartini.ui.screens.LoginScreen
import com.example.mastermartini.ui.screens.MenuScreen
import com.example.mastermartini.ui.screens.PerfilScreen
import com.example.mastermartini.ui.screens.RegistroScreen
import com.example.mastermartini.ui.screens.ReproductorScreen
import com.example.mastermartini.ui.screens.VideosScreen
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition

/** Nombres de las rutas en un solo lugar, para no escribir textos sueltos (y equivocarse). */
object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val MENU = "menu"
    const val PERFIL = "perfil"
    const val VIDEOS = "videos"
    const val REPRODUCTOR = "reproductor/{videoId}"           // {videoId} es un "hueco"
    fun reproductor(videoId: Int) = "reproductor/$videoId"   // ej: reproductor(2) -> "reproductor/2"
}

/**
 * El NavHost decide qué pantalla se muestra.
 * Las pantallas NO conocen al navController: solo reciben lambdas (onVideos, onVolver...).
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.LOGIN) {

        // ---------- Pantallas de tus compañeros (temporales por ahora) ----------
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLogin = {
                    navController.navigate(Rutas.MENU) {
                        popUpTo(Rutas.LOGIN) { inclusive = true } // "atrás" ya no vuelve al login
                    }
                },
                onIrARegistro = { navController.navigate(Rutas.REGISTRO) }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(onRegistrado = { navController.popBackStack() })
        }

        composable(Rutas.MENU) {
            MenuScreen(
                onVideos = { navController.navigate(Rutas.VIDEOS) },
                onPerfil = { navController.navigate(Rutas.PERFIL) }
            )
        }

        composable(Rutas.PERFIL) {
            PerfilScreen(
                onCerrarSesion = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true } // borra todo el historial
                    }
                }
            )
        }

        // ---------- MÓDULO DE VIDEO (tu parte) ----------
        composable(Rutas.VIDEOS,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }) {
            VideosScreen(
                onVideoClick = { id -> navController.navigate(Rutas.reproductor(id)) },
                onVolverMenu = {navController.popBackStack(Rutas.MENU, inclusive = false)}
            )
        }

        composable(
            route = Rutas.REPRODUCTOR,
            arguments = listOf(navArgument("videoId") { type = NavType.IntType }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getInt("videoId") ?: -1
            ReproductorScreen(
                videoId = videoId,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
