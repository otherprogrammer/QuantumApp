package com.example.quantumapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.quantumapp.ui.theme.QuantumAppTheme
import androidx.navigation.compose.rememberNavController
import com.example.quantumapp.navigation.QuantumNavGraph


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuantumAppTheme {
                val navController = rememberNavController()
                QuantumNavGraph(navController = navController)
            }
        }
    }
}
