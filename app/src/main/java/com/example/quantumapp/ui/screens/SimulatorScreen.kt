package com.example.quantumapp.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SimulatorScreen(navController: NavController) {
    var qubitState by remember { mutableStateOf("|0⟩") }
    var selectedGate by remember { mutableStateOf("Hadamard") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Simulador Cuántico") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
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
                transitionSpec = {
                    fadeIn(tween(500)) togetherWith fadeOut(tween(500))
                },
                label = "qubitState"
            ) { state ->
                Text("Estado actual: $state", style = MaterialTheme.typography.titleLarge)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Selecciona una compuerta:", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            DropdownMenuGates(selectedGate = selectedGate, onGateSelected = {
                selectedGate = it
            })

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                qubitState = applyGate(qubitState, selectedGate)
            }) {
                Text("Aplicar $selectedGate")
            }

            Spacer(modifier = Modifier.height(16.dp))

            GateExplanation(gate = selectedGate)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(onClick = {
                qubitState = "|0⟩"
            }) {
                Text("Reiniciar Estado")
            }
        }
    }
}


// Lógica de transformación cuántica simple (no física real)
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

    Crossfade(targetState = color, animationSpec = tween(500), label = "qubitColor") { currentColor ->
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
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
        "Hadamard" -> "La compuerta Hadamard coloca el qubit en una superposición entre |0⟩ y |1⟩."
        "Pauli-X" -> "La compuerta Pauli-X actúa como una NOT: cambia |0⟩ por |1⟩ y viceversa."
        "Pauli-Z" -> "Pauli-Z invierte la fase del estado |1⟩, dejando |0⟩ intacto."
        else -> ""
    }

    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text("¿Qué hace la compuerta $gate?", style = MaterialTheme.typography.bodyMedium)
        Text(explanation, style = MaterialTheme.typography.bodySmall)
    }
}