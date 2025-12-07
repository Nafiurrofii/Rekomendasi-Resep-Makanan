package com.rekomendasiresepmakanan.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()
    
    // Dummy user data store (can be moved to a repository)
    private val registeredUsers = mutableMapOf<String, String>() // Email -> Password

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun register() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val state = _uiState.value
            if (state.email.isBlank() || state.password.isBlank() || state.name.isBlank()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Semua field harus diisi") }
                return@launch
            }
            if (registeredUsers.containsKey(state.email)) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Email sudah terdaftar") }
            } else {
                registeredUsers[state.email] = state.password
                // Set a flag or event for successful registration
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) } // Using isAuthenticated as success flag
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val state = _uiState.value
            if (registeredUsers.containsKey(state.email) && registeredUsers[state.email] == state.password) {
                AuthRepository.login(state.email)
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Email atau password salah") }
            }
        }
    }
    
    fun logout() {
        AuthRepository.logout()
        _uiState.value = AuthUiState() // Reset state
    }
}
