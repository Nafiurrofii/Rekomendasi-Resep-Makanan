package com.rekomendasiresepmakanan.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * SearchViewModel dengan behavior:
 * - Query kosong → Tampilkan semua resep
 * - Query ada isi → Filter berdasarkan title (contains ignoreCase)
 */
class SearchViewModel : ViewModel() {
    private val repository = RecipeRepository

    // State 1: Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // State 2: All recipes (semua resep yang tersedia)
    private val _allRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val allRecipes: StateFlow<List<Recipe>> = _allRecipes.asStateFlow()

    // State 3: Search results (hasil filter)
    private val _searchResults = MutableStateFlow<List<Recipe>>(emptyList())
    val searchResults: StateFlow<List<Recipe>> = _searchResults.asStateFlow()

    init {
        // Load semua resep saat init
        loadAllRecipes()
    }

    /**
     * Load semua resep dari repository
     */
    private fun loadAllRecipes() {
        viewModelScope.launch {
            val recipes = repository.getPopularRecipes()
            _allRecipes.value = recipes
            // Tampilkan semua resep di awal (query kosong)
            _searchResults.value = recipes
        }
    }

    /**
     * Fungsi onSearchChange dengan behavior:
     * - Jika query kosong → kembalikan semua resep
     * - Jika query ada isi → filter berdasarkan title (contains ignoreCase)
     */
    fun onSearchChange(query: String) {
        _searchQuery.value = query

        if (query.isBlank()) {
            // Query kosong → tampilkan semua resep
            _searchResults.value = _allRecipes.value
        } else {
            // Query ada isi → filter berdasarkan title
            _searchResults.value = _allRecipes.value.filter { recipe ->
                recipe.title.contains(query, ignoreCase = true)
            }
        }
    }

    /**
     * Clear search dan kembali ke semua resep
     */
    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = _allRecipes.value
    }
}
