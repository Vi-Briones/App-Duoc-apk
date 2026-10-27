package com.example.mastermartini.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mastermartini.ui.screens.DashboardScreen
import com.example.mastermartini.ui.screens.LoginScreen
import com.example.mastermartini.ui.screens.PerfilScreen
import com.example.mastermartini.ui.screens.RecuperarPasswordScreen
import com.example.mastermartini.ui.screens.RegistroScreen
import com.example.mastermartini.ui.screens.VideoScreen

/** Nombres de las rutas en un solo lugar, para no escribir textos sueltos (y equivocarse). */
object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val RECUPERAR_CLAVE = "recuperar_clave"
    const val DASHBOARD = "dashboard"
    const val PERFIL = "perfil"
    const val VIDEO = "video/{cursoId}"          // {cursoId} es un "hueco" que se llena al navegar
    fun video(cursoId: Int) = "video/$cursoId"   // ej: video(2) -> "video/2"
}

/**
- El NavHost decide qué pantalla se muestra.
- Las pantallas NO conocen al navController: solo reciben lambdas (onLogin, onVolver...).
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.LOGIN) {

        composable(Rutas.LOGIN) {
            // Instanciamos el ViewModel para el Login
            val authViewModel = androidx.lifecycle.viewmodel.compose.viewModel<com.example.mastermartini.viewmodel.AuthViewModel>()

            LoginScreen(
                viewModel = authViewModel, // Pasamos el ViewModel a la pantalla
                onLogin = {
                    navController.navigate(Rutas.DASHBOARD) {
                        // Borra el login del historial: "atrás" ya no vuelve al login
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Rutas.REGISTRO) },
                onOlvidoPassword = { navController.navigate(Rutas.RECUPERAR_CLAVE) }

            )
        }

        composable(Rutas.RECUPERAR_CLAVE) {
            RecuperarPasswordScreen(
                onVolver = { navController.popBackStack() } // Esto hace que regrese al Login
            )
        }

        composable(Rutas.REGISTRO) {
            // Instanciamos el ViewModel para el Registro
            val authViewModel = androidx.lifecycle.viewmodel.compose.viewModel<com.example.mastermartini.viewmodel.AuthViewModel>()

            RegistroScreen(
                viewModel = authViewModel, // Pasamos el ViewModel a la pantalla
                onRegistrado = { navController.popBackStack() }
            )
        }

        composable(Rutas.DASHBOARD) {
            DashboardScreen(
                onCursoClick = { id -> navController.navigate(Rutas.video(id)) },
                onPerfil = { navController.navigate(Rutas.PERFIL) }
            )
        }

        // Ruta del video: recibe el id del curso como número entero
        composable(
            route = Rutas.VIDEO,
            arguments = listOf(navArgument("cursoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val cursoId = backStackEntry.arguments?.getInt("cursoId") ?: -1
            VideoScreen(
                cursoId = cursoId,
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