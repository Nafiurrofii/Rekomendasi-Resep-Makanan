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

            val result = AuthRepository.register(state.name, state.email, state.password)
            
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            }.onFailure { exception ->
                _uiState.update { it.copy(isLoading = false, errorMessage = exception.message ?: "Registrasi gagal") }
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val state = _uiState.value
            
            if (state.email.isBlank() || state.password.isBlank()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Email dan password harus diisi") }
                return@launch
            }

            val result = AuthRepository.login(state.email, state.password)
            
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            }.onFailure { exception ->
                _uiState.update { it.copy(isLoading = false, errorMessage = exception.message ?: "Login gagal") }
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            AuthRepository.logout()
            _uiState.value = AuthUiState() // Reset state
        }
    }
}
