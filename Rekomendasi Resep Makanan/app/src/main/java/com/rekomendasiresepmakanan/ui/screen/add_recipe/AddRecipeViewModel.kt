package com.rekomendasiresepmakanan.ui.screen.add_recipe

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddRecipeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddRecipeUiState())
    val uiState = _uiState.asStateFlow()

    private val repository = RecipeRepository

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(category = category) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onIngredientsChange(ingredients: String) {
        _uiState.update { it.copy(ingredients = ingredients) }
    }

    fun onStepsChange(steps: String) {
        _uiState.update { it.copy(steps = steps) }
    }

    fun onImagePicked(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun submitRecipe() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val state = _uiState.value

            // Validation
            if (state.name.isBlank() || state.category.isBlank() || state.description.isBlank() || 
                state.ingredients.isBlank() || state.steps.isBlank()) {
                _uiState.update { it.copy(
                    isLoading = false, 
                    errorMessage = "Semua field harus diisi"
                ) }
                return@launch
            }
            
            // Call repository to add recipe to backend
            val result = repository.addRecipe(
                name = state.name,
                categoryId = getCategoryId(state.category),
                description = state.description,
                ingredients = state.ingredients,
                steps = state.steps,
                imageUrl = state.imageUri?.toString() ?: ""
            )
            
            // Handle result
            result.onSuccess { recipe ->
                _uiState.update { it.copy(
                    isLoading = false, 
                    isRecipeAdded = true,
                    errorMessage = null
                ) }
            }.onFailure { exception ->
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = "Gagal menambah resep: ${exception.message}"
                ) }
            }
        }
    }
    
    private fun getCategoryId(categoryName: String): Int {
        return when (categoryName) {
            "Nusantara" -> 1
            "Chinese" -> 2
            "Western" -> 3
            "Dessert" -> 4
            "Lainnya" -> 5
            else -> 5 // Default to Lainnya
        }
    }
}
