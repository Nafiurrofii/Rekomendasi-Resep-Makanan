package com.rekomendasiresepmakanan.ui.screen.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PopularViewModel : ViewModel() {
    private val repository = RecipeRepository

    private val _popularRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val popularRecipes: StateFlow<List<Recipe>> = _popularRecipes.asStateFlow()

    init {
        loadPopularRecipes()
    }

    private fun loadPopularRecipes() {
        viewModelScope.launch {
            _popularRecipes.value = repository.getPopularRecipes()
        }
    }
}

