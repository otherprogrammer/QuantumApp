package com.example.quantumapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quantumapp.ui.screens.*
import com.example.quantumapp.ui.screens.dictionary.DictionaryScreen
import com.example.quantumapp.ui.screens.home.HomeScreen
import com.example.quantumapp.ui.screens.login.LoginScreen
import com.example.quantumapp.ui.screens.login.RegisterScreen
import com.example.quantumapp.ui.screens.news.NewsScreen
import com.example.quantumapp.ui.screens.quiz.QuizScreen
import com.example.quantumapp.ui.screens.simulator.CircuitHistoryScreen
import com.example.quantumapp.ui.screens.simulator.CircuitTimelineScreen
import com.example.quantumapp.ui.screens.simulator.CompGateDetailScreen
import com.example.quantumapp.ui.screens.simulator.SimulatorScreen
import com.example.quantumapp.viewmodel.CircuitViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Simulator : Screen("simulator")
    object Dictionary : Screen("dictionary")
    object News : Screen("news")
    object Quiz : Screen("quiz")
    object CircuitHistory : Screen("circuit_history")
    data class CircuitTimeline(val circuitId: String) : Screen("circuit_timeline/$circuitId")
    data class GateDetail(val gateName: String) : Screen("gate_detail/$gateName")
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

        // CircuitHistory con ViewModel
        composable(Screen.CircuitHistory.route) {
            val circuitViewModel = androidx.lifecycle.viewmodel.compose.viewModel<CircuitViewModel>()
            CircuitHistoryScreen(navController, circuitViewModel)
        }

        // Timeline con argumento circuitId
        composable("circuit_timeline/{circuitId}") { backStackEntry ->
            val circuitId = backStackEntry.arguments?.getString("circuitId") ?: ""
            CircuitTimelineScreen(navController, circuitId)
        }

        // GateDetail con argumento gateName
        composable("gate_detail/{gateName}") { backStackEntry ->
            val gateName = backStackEntry.arguments?.getString("gateName") ?: ""
            CompGateDetailScreen(gateName)
        }
    }
}
