package com.example.quantumapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quantumapp.data.auth.AuthService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userEmail: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userEmail = auth.currentUser?.email ?: "Usuario sin email"
                    _authState.value = AuthState.Success(userEmail)
                } else {
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    _authState.value = AuthState.Error(errorMessage)
                }
            }
    }

    fun register(email: String, password: String, displayName: String = "User") {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            AuthService.registerUser(email, password, displayName) { success, error ->
                if (success) {
                    val userEmail = auth.currentUser?.email ?: "Usuario sin email"
                    _authState.value = AuthState.Success(userEmail)
                } else {
                    _authState.value = AuthState.Error(error ?: "Error desconocido")
                }
            }
        }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Idle
    }

    fun checkCurrentUser() {
        val user = auth.currentUser
        _authState.value = if (user != null) {
            AuthState.Success(user.email ?: "Usuario sin email")
        } else {
            AuthState.Idle
        }
    }
}