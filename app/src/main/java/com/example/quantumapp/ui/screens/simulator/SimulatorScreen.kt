package com.example.quantumapp.ui.screens.simulator

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quantumapp.navigation.Screen
import com.example.quantumapp.viewmodel.CircuitViewModel

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SimulatorScreen(navController: NavController) {
    var qubitState by remember { mutableStateOf("|0⟩") }
    var selectedGate by remember { mutableStateOf("Hadamard") }

    val circuitViewModel: CircuitViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Simulador Cuántico") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.CircuitHistory.route) }) {
                        Icon(Icons.Filled.History, contentDescription = "Historial")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            QubitAnimation(state = qubitState)

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(
                targetState = qubitState,
                transitionSpec = { fadeIn(tween(500)) togetherWith fadeOut(tween(500)) }
            ) { state ->
                Text("Estado actual: $state", style = MaterialTheme.typography.titleLarge)
            }

            Spacer(modifier = Modifier.height(24.dp))

            DropdownMenuGates(selectedGate = selectedGate, onGateSelected = { selectedGate = it })

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    qubitState = applyGate(qubitState, selectedGate)
                    circuitViewModel.saveCircuitStep(selectedGate, qubitState)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text("Aplicar $selectedGate", color = Color.White)
            }

            OutlinedButton(
                onClick = { qubitState = "|0⟩" },
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
            ) {
                Text("Reiniciar Estado", color = MaterialTheme.colorScheme.secondary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    circuitViewModel.createOrGetCurrentCircuit { circuitId ->
                        val route = Screen.CircuitTimeline(circuitId.toString()).route
                        navController.navigate(route)
                    }
                }
            ) {
                Text("Ver Línea de Tiempo del Circuito")
            }


            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate(Screen.GateDetail(selectedGate).route) }
            ) {
                Text("Detalles de Compuerta $selectedGate")
            }
        }
    }
}

fun applyGate(current: String, gate: String): String {
    return when (gate) {
        "Hadamard" -> if (current == "|0⟩") "(|0⟩ + |1⟩) / √2" else "(|0⟩ - |1⟩) / √2"
        "Pauli-X" -> if (current == "|0⟩") "|1⟩" else "|0⟩"
        "Pauli-Z" -> if (current == "|1⟩") "-|1⟩" else current
        else -> current
    }
}

@Composable
fun DropdownMenuGates(selectedGate: String, onGateSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val gates = listOf("Hadamard", "Pauli-X", "Pauli-Z")

    Box {
        Button(onClick = { expanded = true }) {
            Text(selectedGate)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            gates.forEach { gate ->
                DropdownMenuItem(
                    text = { Text(gate) },
                    onClick = {
                        onGateSelected(gate)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun QubitAnimation(state: String) {
    val color = when (state) {
        "|0⟩" -> Color.Blue
        "|1⟩" -> Color.Red
        "-|1⟩" -> Color.Magenta
        "(|0⟩ + |1⟩) / √2" -> Color.Green
        "(|0⟩ - |1⟩) / √2" -> Color.Yellow
        else -> Color.Gray
    }

    val scale by animateFloatAsState(
        targetValue = if (state == "|0⟩") 1f else 1.2f,
        animationSpec = tween(500)
    )

    Crossfade(targetState = color, animationSpec = tween(500), label = "qubitColor") { currentColor ->
        Box(
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    rotationZ = if (state != "|0⟩") 15f else 0f
                }
                .clip(CircleShape)
                .background(currentColor),
            contentAlignment = Alignment.Center
        ) {
            Text("Q", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        }
    }
}
