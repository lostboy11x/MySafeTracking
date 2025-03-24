package com.example.mysafetracking.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {

        //Inicialització
        composable("splash") {
            SplashScreen(onTimeout = {
                navController.navigate("authorize") {
                    popUpTo("splash") {
                        inclusive = true
                    }
                }
            })
        }

        // Autorització
        composable("authorize") {
            AuthorizeScreen(navController = navController)
        }
        composable("loginForm") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }

        // Tutor
        composable("menuTutor") {
            MenuScreenTutor(navController = navController)
        }
        composable("mapScreen") {
            MapScreen(navController = navController)
        }

        // Child
        composable("logincChild") {
            LoginAsChildScreen(navController = navController)
        }
        composable("menuChild") {
            ChildMenuScreen(navController = navController)
        }

        // Extra
        composable("gifScreen") {
            GifDrip(navController = navController)  // La pantalla de GIF
        }
    }
}