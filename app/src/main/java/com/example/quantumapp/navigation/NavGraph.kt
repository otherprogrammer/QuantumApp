package com.example.quantumapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quantumapp.ui.screens.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Simulator : Screen("simulator")
    object Dictionary : Screen("dictionary")
    object News : Screen("news")
    object Quiz : Screen("quiz")
}

@Composable
fun QuantumNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Simulator.route) { SimulatorScreen(navController) }
        composable(Screen.Dictionary.route) { DictionaryScreen(navController) }
        composable(Screen.News.route) { NewsScreen(navController) }
        composable(Screen.Quiz.route) { QuizScreen(navController) }
    }
}