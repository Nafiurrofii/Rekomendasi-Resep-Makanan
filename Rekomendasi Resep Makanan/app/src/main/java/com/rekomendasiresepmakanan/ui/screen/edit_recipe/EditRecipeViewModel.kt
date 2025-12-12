package com.rekomendasiresepmakanan.ui.screen.edit_recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.remote.request.RecipeRequest
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.domain.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditRecipeViewModel : ViewModel() {
    private val repository = RecipeRepository

    private val _uiState = MutableStateFlow<UiState<Recipe>>(UiState.Loading)
    val uiState: StateFlow<UiState<Recipe>> = _uiState.asStateFlow()

    private val _updateState = MutableStateFlow<UiState<Boolean>?>(null)
    val updateState: StateFlow<UiState<Boolean>?> = _updateState.asStateFlow()
    
    private val _deleteState = MutableStateFlow<UiState<Boolean>?>(null)
    val deleteState: StateFlow<UiState<Boolean>?> = _deleteState.asStateFlow()

    fun loadRecipe(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val recipe = repository.getRecipeById(id)
            if (recipe != null) {
                _uiState.value = UiState.Success(recipe)
            } else {
                _uiState.value = UiState.Error("Resep tidak ditemukan")
            }
        }
    }

    var newImageUri: android.net.Uri? = null
        private set

    fun onImagePicked(uri: android.net.Uri?) {
        newImageUri = uri
    }

    fun updateRecipe(
        id: Int,
        title: String,
        categoryId: Int,
        description: String,
        ingredients: String,
        steps: String,
        currentImageUrl: String
    ) {
        viewModelScope.launch {
            _updateState.value = UiState.Loading
            
            // Gunakan URI baru jika ada, jika tidak gunakan URL lama
            val finalImage = newImageUri?.toString() ?: currentImageUrl

            val request = RecipeRequest(
                nama_resep = title,
                category_id = categoryId,
                deskripsi = description,
                bahan = ingredients.split("\n").filter { it.isNotBlank() },
                langkah_langkah = steps.split("\n").filter { it.isNotBlank() },
                gambar = finalImage
            )

            val result = repository.updateRecipe(id, request)
            
            result.onSuccess {
                _updateState.value = UiState.Success(true)
            }.onFailure { e ->
                _updateState.value = UiState.Error(e.message ?: "Gagal update resep")
            }
        }
    }

    fun deleteRecipe(id: Int) {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            
            val result = repository.deleteRecipe(id)
            
            result.onSuccess {
                _deleteState.value = UiState.Success(true)
            }.onFailure { e ->
                _deleteState.value = UiState.Error(e.message ?: "Gagal hapus resep")
            }
        }
    }
    
    fun resetUpdateState() {
        _updateState.value = null
    }
}
