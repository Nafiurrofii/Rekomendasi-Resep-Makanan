package com.rekomendasiresepmakanan.ui.screen.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.local.AppDatabase
import com.rekomendasiresepmakanan.data.repository.FavoriteRepository
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val recipeRepository = RecipeRepository
    private val favoriteRepository: FavoriteRepository
    private val recipeId: Int = checkNotNull(savedStateHandle["recipeId"])

    private val _recipeDetail = MutableStateFlow<Recipe?>(null)
    val recipeDetail: StateFlow<Recipe?> = _recipeDetail.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    init {
        // Inisialisasi FavoriteRepository dengan DAO dari database
        val database = AppDatabase.getDatabase(application)
        favoriteRepository = FavoriteRepository(database.favoriteDao())
        
        loadRecipe()
        checkFavoriteStatus()
    }

    private fun loadRecipe() {
        viewModelScope.launch {
            val recipe = recipeRepository.getRecipeById(recipeId)
            _recipeDetail.value = recipe
        }
    }

    private fun checkFavoriteStatus() {
        viewModelScope.launch {
            _isFavorite.value = favoriteRepository.isFavorite(recipeId)
        }
    }

    private val _deleteState = MutableStateFlow<Boolean?>(null)
    val deleteState: StateFlow<Boolean?> = _deleteState.asStateFlow()

    fun toggleFavorite() {
        viewModelScope.launch {
            val recipe = _recipeDetail.value ?: return@launch
            favoriteRepository.toggleFavorite(recipe)
            // Update status setelah toggle
            _isFavorite.value = favoriteRepository.isFavorite(recipeId)
        }
    }

    fun deleteRecipe() {
        viewModelScope.launch {
            val result = recipeRepository.deleteRecipe(recipeId)
            result.onSuccess {
                _deleteState.value = true
            }.onFailure {
                _deleteState.value = false
            }
        }
    }
    
    fun resetDeleteState() {
        _deleteState.value = null
    }
}
