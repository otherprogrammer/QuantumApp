package com.example.quantumapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.quantumapp.navigation.QuantumNavGraph
import com.example.quantumapp.ui.components.AnimatedCyberpunkVideoBackground // Importa el nuevo composable de video
import com.example.quantumapp.ui.theme.QuantumAppTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            QuantumAppTheme {
                // Un Box para superponer el fondo y el contenido de la navegación
                Box(modifier = Modifier.fillMaxSize()) {
                    // 1. El fondo animado (ahora un video)
                    AnimatedCyberpunkVideoBackground(modifier = Modifier.fillMaxSize())

                    // 2. El contenido de tu aplicación (QuantumNavGraph)
                    // Asegúrate de que los fondos de tus pantallas en QuantumNavGraph
                    // sean transparentes o semitransparentes para que el fondo se vea.
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Transparent // ¡Importante! Hace que el fondo sea visible
                    ) {
                        val navController = rememberNavController()
                        QuantumNavGraph(navController = navController)
                    }
                }
            }
        }
    }
}