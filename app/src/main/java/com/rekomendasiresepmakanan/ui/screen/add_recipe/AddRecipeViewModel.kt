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

    private val repository = RecipeRepository()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
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

            if (state.name.isBlank() || state.ingredients.isBlank() || state.steps.isBlank() || state.imageUri == null) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Semua field harus diisi") }
                return@launch
            }
            
            // Simulasi penambahan resep
            // Di aplikasi nyata, Anda akan meng-upload gambar ke server dan menyimpan URL-nya
            repository.addRecipe(
                name = state.name,
                ingredients = state.ingredients,
                steps = state.steps,
                imageUrl = state.imageUri.toString()
            )
            
            _uiState.update { it.copy(isLoading = false, isRecipeAdded = true) }
        }
    }
}
