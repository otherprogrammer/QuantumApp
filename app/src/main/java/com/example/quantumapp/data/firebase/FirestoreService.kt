package com.example.quantumapp.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.IgnoreExtraProperties

// Asegúrate de anotar la data class correctamente
@IgnoreExtraProperties
data class QuantumTerm(
    val id: String = "",
    val term: String = "",
    val description: String = ""
)

object FirestoreService {
    private val db = FirebaseFirestore.getInstance()
    private val termsRef = db.collection("dictionary")

    fun getAllTerms(onResult: (List<QuantumTerm>) -> Unit) {
        termsRef.get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { document ->
                    document.toObject(QuantumTerm::class.java)?.copy(id = document.id)
                }
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList()) // Manejo de error simple
            }
    }

    fun addTerm(term: QuantumTerm, onComplete: (Boolean) -> Unit) {
        termsRef.add(term)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
}
