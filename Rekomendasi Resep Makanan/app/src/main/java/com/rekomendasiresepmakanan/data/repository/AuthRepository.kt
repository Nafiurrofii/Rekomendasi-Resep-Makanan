package com.rekomendasiresepmakanan.data.repository

import com.rekomendasiresepmakanan.data.remote.RetrofitClient
import com.rekomendasiresepmakanan.data.remote.request.LoginRequest
import com.rekomendasiresepmakanan.data.remote.request.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.rekomendasiresepmakanan.domain.model.UiState

object AuthRepository {
    private val apiService = RetrofitClient.apiService

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    suspend fun login(email: String, password: String): Result<Boolean> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.status == "success" && response.data != null) {
                _isAuthenticated.value = true
                _currentUser.value = User(response.data.user.name, response.data.user.email, response.data.token)
                Result.success(true)
            } else {
                Result.failure(Exception(response.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(parseError(e)))
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<Boolean> {
        return try {
            val response = apiService.register(RegisterRequest(name, email, password))
            if (response.status == "success" && response.data != null) {
                _isAuthenticated.value = true
                _currentUser.value = User(response.data.user.name, response.data.user.email, response.data.token)
                Result.success(true)
            } else {
                Result.failure(Exception(response.message ?: "Register failed"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(parseError(e)))
        }
    }

    suspend fun logout() {
        try {
            apiService.logout()
        } catch (e: Exception) {
            // Ignore network errors on logout
        } finally {
            _isAuthenticated.value = false
            _currentUser.value = null
        }
    }

    private fun parseError(e: Exception): String {
        return if (e is retrofit2.HttpException) {
            try {
                val errorBody = e.response()?.errorBody()?.string()
                if (errorBody != null) {
                    val json = org.json.JSONObject(errorBody)
                    val message = json.optString("message", e.message())
                    val errorsOpt = json.optJSONObject("errors")
                    if (errorsOpt != null) {
                        val details = StringBuilder()
                        val keys = errorsOpt.keys()
                        while (keys.hasNext()) {
                            val key = keys.next()
                            val messages = errorsOpt.getJSONArray(key)
                            for (i in 0 until messages.length()) {
                                details.append(messages.getString(i)).append("\n")
                            }
                        }
                        if (details.isNotEmpty()) details.toString().trim() else message
                    } else {
                        message
                    }
                } else {
                    e.message()
                }
            } catch (parsingError: Exception) {
                e.message()
            }
        } else {
            e.message ?: "Unknown error"
        }
    }
}

data class User(val name: String, val email: String, val token: String)
