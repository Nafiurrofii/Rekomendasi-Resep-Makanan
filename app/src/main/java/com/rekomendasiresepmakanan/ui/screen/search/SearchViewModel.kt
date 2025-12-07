package com.rekomendasiresepmakanan.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = RecipeRepository()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Recipe>>(emptyList())
    val searchResults: StateFlow<List<Recipe>> = _searchResults.asStateFlow()

    fun onSearchChange(query: String) {
        _searchQuery.value = query
        
        viewModelScope.launch {
            if (query.length >= 2) {
                _suggestions.value = repository.getSuggestions(query)
            } else {
                _suggestions.value = emptyList()
            }

            if (query.length >= 3) {
                 _searchResults.value = repository.searchRecipes(query)
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _suggestions.value = emptyList()
        _searchResults.value = emptyList()
    }

    fun selectSuggestion(text: String) {
        _searchQuery.value = text
        _suggestions.value = emptyList()
        viewModelScope.launch {
            _searchResults.value = repository.searchRecipes(text)
        }
    }
}
