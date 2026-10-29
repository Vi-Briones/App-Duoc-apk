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

/** Rutas de la app en un solo lugar. */
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

/** Navegación de la app. Las pantallas no usan el navController, solo reciben lambdas. */
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
                        // Borra el historial para que "atrás" no vuelva al login
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                // Cada formulario parte vacío al entrar
                onIrARegistro = {
                    authViewModel.limpiarRegistro()
                    navController.navigate(Rutas.REGISTRO) { launchSingleTop = true }
                },
                onOlvidoPassword = {
                    authViewModel.limpiarRecuperar()
                    navController.navigate(Rutas.RECUPERAR_CLAVE) { launchSingleTop = true }
                }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                viewModel = authViewModel,
                // Después de registrarse vuelve al login
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
                viewModel = authViewModel,
                // Vuelve al login (evita la pantalla en blanco con doble toque)
                onVolver = { navController.popBackStack(Rutas.LOGIN, inclusive = false) }
            )
        }

        // launchSingleTop evita abrir la misma pantalla dos veces
        composable(Rutas.MENU) {
            MenuScreen(
                onVideos = { navController.navigate(Rutas.VIDEOS) { launchSingleTop = true } },
                onPerfil = { navController.navigate(Rutas.PERFIL) { launchSingleTop = true } }
            )
        }

        composable(Rutas.VIDEOS) {
            VideosScreen(
                onVideoClick = { id -> navController.navigate(Rutas.reproductor(id)) { launchSingleTop = true } },
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
                onVideoClick = { id -> navController.navigate(Rutas.reproductor(id)) { launchSingleTop = true } },
                onIrAVideos = { navController.navigate(Rutas.VIDEOS) { launchSingleTop = true } },
                onVolverMenu = { navController.popBackStack(Rutas.MENU, inclusive = false) },
                // Botones del perfil cuando no hay sesión
                onIniciarSesion = { navController.navigate(Rutas.LOGIN) { launchSingleTop = true } },
                onRegistrarse = {
                    authViewModel.limpiarRegistro()
                    navController.navigate(Rutas.REGISTRO) { launchSingleTop = true }
                }
            )
        }
    }
}
