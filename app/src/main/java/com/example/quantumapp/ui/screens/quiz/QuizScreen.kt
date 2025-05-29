package com.example.quantumapp.ui.screens.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quantumapp.data.sampleQuiz
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(navController: NavController) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }

    val question = sampleQuiz.getOrNull(currentQuestionIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Cuántico") },
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
                .padding(paddingValues)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showResult) {
                Text(
                    text = "¡Quiz terminado!",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Puntaje: $score de ${sampleQuiz.size}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = {
                    currentQuestionIndex = 0
                    score = 0
                    showResult = false
                }) {
                    Text("Volver a intentar")
                }
            } else if (question != null) {
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(24.dp))
                question.options.forEachIndexed { index, option ->
                    Button(
                        onClick = {
                            if (index == question.correctAnswerIndex) {
                                score++
                            }
                            if (currentQuestionIndex == sampleQuiz.lastIndex) {
                                showResult = true
                            } else {
                                currentQuestionIndex++
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(option)
                    }
                }
            }
        }
    }
}