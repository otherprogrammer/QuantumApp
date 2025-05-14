package com.example.quantumapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quantumapp.ui.screens.*

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Simulator : Screen("simulator")
    object Dictionary : Screen("dictionary")
    object News : Screen("news")
    object Quiz : Screen("quiz")
}

@Composable
fun QuantumNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterNavigate = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onLoginNavigate = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Simulator.route) { SimulatorScreen(navController) }
        composable(Screen.Dictionary.route) { DictionaryScreen(navController) }
        composable(Screen.News.route) { NewsScreen(navController) }
        composable(Screen.Quiz.route) { QuizScreen(navController) }
    }
}