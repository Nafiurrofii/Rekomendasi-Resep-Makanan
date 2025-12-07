package com.rekomendasiresepmakanan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {
    private val repository = RecipeRepository()

    private val _favoriteRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val favoriteRecipes: StateFlow<List<Recipe>> = _favoriteRecipes.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _favoriteRecipes.value = repository.getFavoriteRecipes()
        }
    }

    // Fungsi remove ini opsional, karena tombol favorit di DetailScreen yang akan handle add/remove.
    // Namun, fungsi ini berguna jika Anda ingin menambahkan tombol hapus langsung di FavoriteScreen.
    fun removeFavorite(id: Int) {
        viewModelScope.launch {
            repository.toggleFavoriteStatus(id)
            // Muat ulang daftar favorit setelah menghapus
            loadFavorites()
        }
    }
}
