package com.example.quantumapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SimulatorScreen(navController: NavController) {
    var qubitState by remember { mutableStateOf("|0⟩") }
    var selectedGate by remember { mutableStateOf("Hadamard") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Simulador Cuántico", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        QubitAnimation(state = qubitState)

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedContent(targetState = qubitState, label = "qubitState") { state ->
            Text(
                text = "Estado: $state",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Selecciona una compuerta:", style = MaterialTheme.typography.bodyMedium)

        // MOSTRAR EL DROPDOWN
        DropdownMenuGates(
            selectedGate = selectedGate,
            onGateSelected = { selectedGate = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // BOTÓN PARA APLICAR LA COMPUERTA
        Button(onClick = {
            qubitState = applyGate(selectedGate, qubitState)
        }) {
            Text("Aplicar Compuerta")
        }

        // EXPLICACIÓN DE LA COMPUERTA
        GateExplanation(gate = selectedGate)
    }
}

fun applyGate(gate: String, currentState: String): String {
    return when (gate) {
        "Hadamard" -> {
            if (currentState == "|0⟩") "(|0⟩ + |1⟩) / √2"
            else if (currentState == "|1⟩") "(|0⟩ - |1⟩) / √2"
            else "|0⟩"
        }
        "Pauli-X" -> {
            if (currentState == "|0⟩") "|1⟩"
            else "|0⟩"
        }
        "Pauli-Z" -> {
            if (currentState == "|1⟩") "-|1⟩"
            else currentState
        }
        else -> currentState
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

    Crossfade(targetState = color, label = "qubitColor") { currentColor ->
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .padding(8.dp)
                .background(currentColor),
            contentAlignment = Alignment.Center
        ) {
            Text("Q", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        }
    }
}

@Composable
fun GateExplanation(gate: String) {
    val explanation = when (gate) {
        "Hadamard" -> "Hadamard transforma el qubit en una superposición entre |0⟩ y |1⟩."
        "Pauli-X" -> "Pauli-X actúa como una NOT cuántica: cambia |0⟩ por |1⟩ y viceversa."
        "Pauli-Z" -> "Pauli-Z cambia el signo del estado |1⟩ sin afectar |0⟩."
        else -> ""
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text("¿Qué hace $gate?", style = MaterialTheme.typography.bodyMedium)
    Text(explanation, style = MaterialTheme.typography.bodySmall)
}