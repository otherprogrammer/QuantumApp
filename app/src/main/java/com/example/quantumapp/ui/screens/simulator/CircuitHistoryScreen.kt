package com.example.quantumapp.ui.screens.simulator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quantumapp.viewmodel.CircuitState
import com.example.quantumapp.viewmodel.CircuitViewModel
import androidx.compose.runtime.getValue


// Modelo de datos para cada paso del circuito
data class CircuitStep(val gate: String, val result: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircuitHistoryScreen(navController: NavHostController, circuitViewModel: CircuitViewModel) {
    val circuitState by circuitViewModel.circuitState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial del Circuito") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (circuitState) {
            is CircuitState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is CircuitState.Success -> {
                val steps = (circuitState as CircuitState.Success).steps
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    items(steps) { step ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Puerta: ${step.gate}", fontWeight = FontWeight.Bold)
                                Text("Resultado: ${step.result}")
                            }
                        }
                    }
                }
            }
            is CircuitState.Error -> {
                val message = (circuitState as CircuitState.Error).message
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $message")
                }
            }
        }
    }
}


@Composable
fun CircuitStepCard(step: CircuitStep) {
    val gateColor = when (step.gate) {
        "Hadamard" -> Color(0xFF4CAF50) // Verde
        "Pauli-X" -> Color(0xFFF44336) // Rojo
        "Pauli-Z" -> Color(0xFF3F51B5) // Azul
        else -> Color.Gray
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = gateColor.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Compuerta: ${step.gate}", style = MaterialTheme.typography.titleMedium)
            Text("Estado: ${step.result}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
