package com.example.mysafetracking.screens.general

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mysafetracking.data.db.viewmodels.TutorViewModel
import com.example.mysafetracking.screens.child.ChildMenuScreen
import com.example.mysafetracking.screens.child.LoginAsChildScreen
import com.example.mysafetracking.screens.tutor.ChildInformationScreen
import com.example.mysafetracking.screens.tutor.MapScreen
import com.example.mysafetracking.screens.tutor.MenuScreenTutor

@Composable
fun NavigationGraph(navController: NavHostController, tutorViewModel: TutorViewModel) {
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
            LoginScreen(navController = navController, tutorViewModel = tutorViewModel)
        }
        composable("register") {
            RegisterScreen(navController = navController, tutorViewModel = tutorViewModel)
        }

        // Tutor
        composable("menuTutor") {
            MenuScreenTutor(navController = navController)
        }
        composable("mapScreen") {
            MapScreen(navController = navController)
        }
        composable("childInformationScreen/{lat}/{lng}") { backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull()
            val lng = backStackEntry.arguments?.getString("lng")?.toDoubleOrNull()
            ChildInformationScreen(navController, lat, lng)
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