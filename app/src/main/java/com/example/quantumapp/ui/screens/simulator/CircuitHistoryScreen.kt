package com.example.quantumapp.ui.screens.simulator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quantumapp.viewmodel.CircuitViewModel
import com.example.quantumapp.viewmodel.CircuitState
import com.example.quantumapp.model.CircuitStep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircuitHistoryScreen(navController: NavHostController, circuitViewModel: CircuitViewModel) {
    val circuitState by circuitViewModel.circuitState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Historial del Circuito",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                    )
                )
                .padding(paddingValues)
        ) {
            when (circuitState) {
                is CircuitState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Cyan)
                    }
                }

                is CircuitState.Success -> {
                    val steps = (circuitState as CircuitState.Success).steps
                    if (steps.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Aún no hay pasos registrados", color = Color.White)
                        }
                    } else {
                        LazyColumn {
                            items(steps) { step ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut()
                                ) {
                                    CircuitStepCard(step)
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }

                is CircuitState.Error -> {
                    val message = (circuitState as CircuitState.Error).message
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: $message", color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun CircuitStepCard(step: CircuitStep) {
    val gateColor = when (step.gate) {
        "Hadamard" -> Color(0xFF4CAF50)
        "Pauli-X" -> Color(0xFFF44336)
        "Pauli-Z" -> Color(0xFF3F51B5)
        else -> Color.Gray
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = gateColor.copy(alpha = 0.15f)),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Compuerta: ${step.gate}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Resultado: ${step.result}",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray)
            )
        }
    }
}