package com.example.quantumapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.quantumapp.ui.theme.QuantumAppTheme
import androidx.navigation.compose.rememberNavController
import com.example.quantumapp.navigation.QuantumNavGraph
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            QuantumAppTheme {
                val navController = rememberNavController()
                QuantumNavGraph(navController = navController)
            }
        }
    }
}
