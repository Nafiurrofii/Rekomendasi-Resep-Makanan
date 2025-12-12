package com.rekomendasiresepmakanan.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AuthRepository {
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun login(email: String) {
        _isAuthenticated.value = true
        _currentUser.value = User("User", email) // Dummy user data
    }

    fun logout() {
        _isAuthenticated.value = false
        _currentUser.value = null
    }
}

data class User(val name: String, val email: String)
