package com.rekomendasiresepmakanan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Category
import com.rekomendasiresepmakanan.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val repository = RecipeRepository

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = listOf(
                Category(1, "Nusantara"),
                Category(2, "Chinese"),
                Category(3, "Western"),
                Category(4, "Dessert"),
                Category(5, "Lainnya")
            )
        }
    }

    fun getRecipeByCategory(categoryName: String): StateFlow<List<Recipe>> {
        val recipesFlow = MutableStateFlow<List<Recipe>>(emptyList())
        viewModelScope.launch {
            recipesFlow.value = repository.getRecipesByCategory(categoryName)
        }
        return recipesFlow.asStateFlow()
    }
}
