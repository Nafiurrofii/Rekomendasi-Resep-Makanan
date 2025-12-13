package com.rekomendasiresepmakanan.ui.screen.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.R
import com.rekomendasiresepmakanan.domain.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.rekomendasiresepmakanan.domain.model.Resource

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipeRepository
    private val context = application.applicationContext

    // State untuk kategori
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    // State untuk menyimpan data resep dari API
    private val _popularRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val popularRecipes = _popularRecipes.asStateFlow()

    // State untuk loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // State untuk error message
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        loadCategories()
        fetchRecipes()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            // Ambil kategori dari API (Database)
            val result = repository.getCategories()
            if (result.isNotEmpty()) {
                _categories.value = result
            } else {
                // Fallback jika API kosong/gagal (untuk sementara hardcode jika DB kosong)
                 _categories.value = listOf(
                    Category(1, "Nusantara"),
                    Category(2, "Chinese"),
                    Category(3, "Western"),
                    Category(4, "Dessert"),
                    Category(5, "Lainnya")
                )
            }
        }
    }

    fun fetchRecipes() {
        viewModelScope.launch {
            // Gunakan getRecipes(context) yang memiliki fitur Fallback Cache (DB) -> API -> DB Update
            repository.getRecipes(context).collect { resource ->
                when(resource) {
                    is Resource.Loading -> {
                         _isLoading.value = true
                    }
                    is Resource.Success -> {
                        _isLoading.value = false
                        _popularRecipes.value = resource.data ?: emptyList()
                        _errorMessage.value = null
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        // Jika Cache ada isinya, Error tidak dipancarkan oleh repository (sudah dihandle),
                        // tapi jika cache kosong dan API gagal, baru error muncul.
                        _errorMessage.value = resource.message
                    }
                }
            }
        }
    }
}
