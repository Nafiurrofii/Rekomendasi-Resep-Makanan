package com.rekomendasiresepmakanan.viewmodel

import androidx.lifecycle.ViewModel
import com.rekomendasiresepmakanan.model.Recipe
import com.rekomendasiresepmakanan.model.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel : ViewModel() {
    // Backing property untuk state (mutable)
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    
    // Public property untuk diobservasi oleh UI (immutable)
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        // Mengambil data dari Model (Repository)
        _recipes.value = RecipeRepository.getRecipes()
    }
}
