package com.rekomendasiresepmakanan.ui.screen.add_recipe

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.domain.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddRecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(AddRecipeUiState())
    val uiState: StateFlow<AddRecipeUiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<com.rekomendasiresepmakanan.domain.model.Category>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = RecipeRepository.getCategories()
        }
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onCategoryChange(categoryId: Int) {
        _uiState.value = _uiState.value.copy(categoryId = categoryId)
    }

    fun onDescriptionChange(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun onIngredientsChange(ingredients: String) {
        _uiState.value = _uiState.value.copy(ingredients = ingredients)
    }

    fun onStepsChange(steps: String) {
        _uiState.value = _uiState.value.copy(steps = steps)
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = uri)
    }

    fun onImageUrlChange(url: String) {
        _uiState.value = _uiState.value.copy(imageUrl = url)
    }

    fun addRecipe() {
        val currentState = _uiState.value
        val imageUri = currentState.imageUri
        val imageUrl = currentState.imageUrl

        // Validasi: Salah satu harus ada (Image File atau URL)
        if (imageUri == null && imageUrl.isBlank()) {
             // Bisa set state error atau biarkan repository handle
             // Untuk experience UI, kita return dulu jika kosong dua-duanya
             return 
        }

        val ingredientsList = currentState.ingredients.split("\n").filter { it.isNotBlank() }
        val stepsList = currentState.steps.split("\n").filter { it.isNotBlank() }

        viewModelScope.launch {
            RecipeRepository.addRecipe(
                context = getApplication(),
                name = currentState.name,
                categoryId = currentState.categoryId,
                description = currentState.description,
                ingredients = ingredientsList,
                steps = stepsList,
                imageUri = imageUri,
                imageUrl = imageUrl
            ).onEach { uploadState ->
                _uiState.value = _uiState.value.copy(uploadState = uploadState)
            }.launchIn(viewModelScope)
        }
    }

    fun resetUploadState() {
        _uiState.value = _uiState.value.copy(uploadState = null)
    }
}
