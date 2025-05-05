package com.example.quantumapp.data

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

val sampleQuiz = listOf(
    QuizQuestion(
        question = "¿Qué es un qubit?",
        options = listOf("Una unidad de energía", "Un bit cuántico", "Un programa", "Una puerta lógica"),
        correctAnswerIndex = 1
    ),
    QuizQuestion(
        question = "¿Qué propiedad permite a los qubits estar en múltiples estados a la vez?",
        options = listOf("Superposición", "Entrelazamiento", "Decoherencia", "Interferencia"),
        correctAnswerIndex = 0
    )
    // agregar más
)