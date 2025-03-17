package com.example.mysafetracking.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onTimeout = {
                navController.navigate("authorize") {
                    popUpTo("splash") {
                        inclusive = true
                    }
                }
            })
        }
        composable("authorize") {
            AuthorizeScreen(navController = navController)
        }
        composable("loginForm") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("gifScreen") { GifDrip(navController = navController) }  // La pantalla de GIF

    }
}
