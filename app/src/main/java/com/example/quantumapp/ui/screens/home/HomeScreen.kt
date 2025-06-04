package com.example.quantumapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quantumapp.navigation.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Quiz

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.Transparent, // Asegura que el fondo del Scaffold sea transparente
        topBar = {
            TopAppBar(
                title = { /* Puedes poner tu título aquí si lo deseas */ },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        // Usa `WindowInsets.systemBars.asPaddingValues()` para aplicar padding
        // que respete las barras del sistema, además del padding del Scaffold.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Padding del Scaffold (para la TopAppBar)
                .padding(WindowInsets.systemBars.asPaddingValues()) // <--- ¡CAMBIO AQUÍ!
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a QuantumApp",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Explora la computación cuántica de forma interactiva",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate(Screen.Simulator.route) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8A2BE2).copy(alpha = 0.7f),
                    contentColor = Color.White
                )
            ) {
                Icon(imageVector = Icons.Default.Memory, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Simulador Cuántico")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.Dictionary.route) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BFFF).copy(alpha = 0.7f),
                    contentColor = Color.White
                )
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.MenuBook, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Diccionario")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.News.route) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF00FF).copy(alpha = 0.7f),
                    contentColor = Color.White
                )
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Article, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Noticias")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.Quiz.route) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00FFFF).copy(alpha = 0.7f),
                    contentColor = Color.White
                )
            ) {
                Icon(imageVector = Icons.Default.Quiz, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Quiz Cuántico")
            }
        }
    }
}