package com.rekomendasiresepmakanan.ui.screen.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.local.AppDatabase
import com.rekomendasiresepmakanan.data.repository.FavoriteRepository
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.domain.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteRepository: FavoriteRepository
    
    private val _uiState = MutableStateFlow<UiState<Recipe>>(UiState.Loading)
    val uiState: StateFlow<UiState<Recipe>> = _uiState.asStateFlow()

    private val _deleteState = MutableStateFlow<Boolean?>(null)
    val deleteState: StateFlow<Boolean?> = _deleteState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()
    
    private var currentRecipeId: Int? = null

    init {
        val database = AppDatabase.getDatabase(application)
        favoriteRepository = FavoriteRepository(database.favoriteDao())
    }

    fun getRecipeDetail(recipeId: Int) {
        currentRecipeId = recipeId
        _uiState.value = UiState.Loading
        
        viewModelScope.launch {
            // Cek status favorit
            // Repository isFavorite is Flow, so we collect it
             launch {
                RecipeRepository.isFavorite(getApplication(), recipeId).collect {
                    _isFavorite.value = it
                }
            }

            val recipe = RecipeRepository.getRecipeById(getApplication(), recipeId)
            
            if (recipe != null) {
                _uiState.value = UiState.Success(recipe)
            } else {
                _uiState.value = UiState.Error("Failed to get recipe detail")
            }
        }
    }
    
    fun deleteRecipe() {
        val id = currentRecipeId ?: return
        viewModelScope.launch {
            val result = RecipeRepository.deleteRecipe(getApplication(), id)
            if (result.isSuccess) {
                _deleteState.value = true
            } else {
                _deleteState.value = false
            }
        }
    }
    
    fun resetDeleteState() {
        _deleteState.value = null
    }
    
    fun toggleFavorite() {
        val uiStateValue = _uiState.value
        if (uiStateValue is UiState.Success) {
            val recipe = uiStateValue.data
            viewModelScope.launch {
                // Use RecipeRepository to toggle, passing context
                RecipeRepository.toggleFavorite(getApplication(), recipe)
                // Flow in getRecipeDetail will update _isFavorite automatically
            }
        }
    }
}
