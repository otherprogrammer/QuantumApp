package com.example.quantumapp.data

data class QuantumTerm(
    val term: String,
    val definition: String
)

val quantumTerms = listOf(
    QuantumTerm(
        term = "Qubit",
        definition = "La unidad básica de información cuántica, que puede estar en superposición de 0 y 1."
    ),
    QuantumTerm(
        term = "Superposición",
        definition = "Propiedad de los qubits que permite estar en múltiples estados a la vez."
    ),
    QuantumTerm(
        term = "Entrelazamiento",
        definition = "Fenómeno donde dos qubits están conectados, de modo que el estado de uno afecta al otro instantáneamente."
    ),
    QuantumTerm(
        term = "Puerta Hadamard",
        definition = "Operación cuántica que pone a un qubit en superposición."
    ),
    QuantumTerm(
        term = "Medición",
        definition = "El proceso de observar un qubit, colapsando su estado a 0 o 1."
    )
)