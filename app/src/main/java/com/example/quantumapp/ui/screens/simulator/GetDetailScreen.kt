package com.example.quantumapp.ui.screens.simulator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GateDetailScreen(gateName: String) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(gateName) })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Detailed information about $gateName", style = MaterialTheme.typography.headlineMedium)
        }
    }
}
