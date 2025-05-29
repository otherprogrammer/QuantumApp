package com.example.quantumapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CircuitStep(val gate: String = "", val result: String = "")

sealed class CircuitState {
    object Loading : CircuitState()
    data class Success(val steps: List<CircuitStep>) : CircuitState()
    data class Error(val message: String) : CircuitState()
}

class CircuitViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _circuitState = MutableStateFlow<CircuitState>(CircuitState.Loading)
    val circuitState: StateFlow<CircuitState> = _circuitState

    init {
        fetchCircuitHistory()
    }

    fun fetchCircuitHistory() {
        viewModelScope.launch {
            db.collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid ?: "unknown")
                .collection("circuitHistory")
                .get()
                .addOnSuccessListener { result ->
                    val steps = result.map { doc ->
                        CircuitStep(
                            gate = doc.getString("gate") ?: "",
                            result = doc.getString("result") ?: ""
                        )
                    }
                    _circuitState.value = CircuitState.Success(steps)
                }
                .addOnFailureListener { exception ->
                    _circuitState.value = CircuitState.Error(exception.message ?: "Error al cargar")
                }
        }
    }

    fun saveCircuitStep(gate: String, result: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val circuitStep = hashMapOf(
            "gate" to gate,
            "result" to result,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("users")
            .document(userId)
            .collection("circuitHistory")
            .add(circuitStep)
            .addOnSuccessListener {
                // Opcional: Puedes llamar a fetchCircuitHistory() para actualizar la lista
            }
            .addOnFailureListener { e ->
                // Manejar error
            }
    }
    private var currentCircuitId: String? = null

    fun getCurrentCircuitId(): String? {
        return currentCircuitId
    }

    fun createOrGetCurrentCircuit(onComplete: (String?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return onComplete(null)
        if (currentCircuitId == null) {
            val newCircuitRef = db.collection("users")
                .document(userId)
                .collection("circuits")
                .document() // genera un nuevo id
            currentCircuitId = newCircuitRef.id

            newCircuitRef.set(
                hashMapOf(
                    "createdAt" to System.currentTimeMillis(),
                    "userId" to userId
                )
            ).addOnSuccessListener {
                onComplete(currentCircuitId)
            }.addOnFailureListener {
                onComplete(null)
            }
        } else {
            onComplete(currentCircuitId)
        }
    }
}
