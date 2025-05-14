package com.example.quantumapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.quantumapp.viewmodel.AuthState
import com.example.quantumapp.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.checkCurrentUser()
    }

    when (authState) {
        is AuthState.Success -> {
            LaunchedEffect(Unit) {
                delay(1000) // tiempo de carga simulado
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        is AuthState.Idle,
        is AuthState.Error -> {
            LaunchedEffect(Unit) {
                delay(1000)
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        AuthState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

