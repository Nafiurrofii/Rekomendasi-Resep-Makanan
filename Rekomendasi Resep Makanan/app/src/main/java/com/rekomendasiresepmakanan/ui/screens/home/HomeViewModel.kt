package com.rekomendasiresepmakanan.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.remote.RetrofitClient
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.domain.model.UiState
import com.rekomendasiresepmakanan.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * HomeViewModel dengan UiState sealed class untuk state management yang lebih baik.
 * Mendukung 3 state: Loading, Success, dan Error dengan retry mechanism.
 */
class HomeViewModel : ViewModel() {

    // State menggunakan UiState sealed class
    private val _uiState = MutableStateFlow<UiState<List<Recipe>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Recipe>>> = _uiState.asStateFlow()

    init {
        fetchRecipes()
    }

    /**
     * Fetch recipes dari API dengan error handling lengkap
     */
    fun fetchRecipes() {
        viewModelScope.launch {
            // Set state ke Loading
            _uiState.value = UiState.Loading

            try {
                // Call API
                val response = RetrofitClient.instance.getRecipes()

                if (response.status == "success") {
                    // Langsung gunakan data dari API (image URL sudah ada di field 'image')
                    // Tidak perlu hardcoded mapping lagi
                    _uiState.value = UiState.Success(response.data)
                    
                    android.util.Log.d("HomeViewModel", "✅ Berhasil load ${response.data.size} resep")
                } else {
                    // API response tidak success
                    _uiState.value = UiState.Error("API Error: ${response.status}")
                }

            } catch (e: Exception) {
                // Handle berbagai jenis error
                val errorMessage = when {
                    e.message?.contains("Unable to resolve host") == true -> 
                        "Tidak dapat terhubung ke server.\nPastikan Laravel running dan HP terhubung ke WiFi yang sama."
                    e.message?.contains("timeout") == true -> 
                        "Koneksi timeout.\nCoba periksa koneksi internet Anda."
                    e.message?.contains("Connection refused") == true -> 
                        "Server menolak koneksi.\nPastikan Laravel running dengan: php artisan serve --host=0.0.0.0"
                    else -> 
                        "Terjadi kesalahan: ${e.message}"
                }
                
                _uiState.value = UiState.Error(errorMessage)
                android.util.Log.e("HomeViewModel", "❌ Error: ${e.message}", e)
            }
        }
    }

    /**
     * Retry untuk reload data setelah error
     */
    fun retry() {
        fetchRecipes()
    }
}
