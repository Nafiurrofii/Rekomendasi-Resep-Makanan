package com.rekomendasiresepmakanan.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.domain.model.Resource
import com.rekomendasiresepmakanan.domain.model.UiState
import com.rekomendasiresepmakanan.util.NetworkConnectivityHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * HomeViewModel yang mendukung Hybrid System (Offline/Online).
 * Menggunakan AndroidViewModel untuk akses Context.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Main UI State for Recipes
    private val _uiState = MutableStateFlow<UiState<List<Recipe>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Recipe>>> = _uiState.asStateFlow()
    
    // Online Status Indicator
    private val _isOnline = MutableStateFlow(NetworkConnectivityHelper.isOnline(application))
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        fetchRecipes()
    }

    /**
     * Fetch recipes menggunakan Hybrid Repository
     * 1. Loading
     * 2. Cache Data (Success)
     * 3. Fresh Data (Success Update)
     * or Error
     */
    fun fetchRecipes() {
        viewModelScope.launch {
            // Update online status terkini
            _isOnline.value = NetworkConnectivityHelper.isOnline(getApplication())
            
            RecipeRepository.getRecipes(getApplication()).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Hanya set loading jika belum ada data sebelumnya (atau state awal)
                        if (_uiState.value !is UiState.Success) {
                            _uiState.value = UiState.Loading
                        }
                    }
                    is Resource.Success -> {
                        val data = resource.data ?: emptyList()
                        _uiState.value = UiState.Success(data)
                        android.util.Log.d("HomeViewModel", "✅ Loaded ${data.size} recipes (Is Cache: ${!_isOnline.value})")
                    }
                    is Resource.Error -> {
                        // Jika offline dan cache kosong, tampilkan error
                        // Jika ada cache sebelumnya, biarkan Success state (jangan overwrite dengan Error)
                        if (_uiState.value !is UiState.Success) {
                            _uiState.value = UiState.Error(resource.message ?: "Unknown error")
                        }
                    }
                }
            }
        }
    }

    fun refresh() {
        fetchRecipes()
    }
}
