package com.rekomendasiresepmakanan.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.R
import com.rekomendasiresepmakanan.domain.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = RecipeRepository

    // State untuk kategori (Dummy dulu karena belum ada API)
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    // State untuk menyimpan data resep dari API
    private val _popularRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val popularRecipes = _popularRecipes.asStateFlow()

    // State untuk loading (agar bisa menampilkan progress bar)
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // State untuk error message (jika gagal ambil data)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        loadCategories()
        fetchRecipes()
    }

    private fun loadCategories() {
        // Dummy Data Kategori
        _categories.value = listOf(
            Category(1, "Nusantara"),
            Category(2, "Chinese"),
            Category(3, "Western"),
            Category(4, "Dessert")
        )
    }

    fun fetchRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Reset error

            try {
                // Ambil data dari Repository (yang sudah handle API + Mapping)
                val recipes = repository.getPopularRecipes()
                _popularRecipes.value = recipes

            } catch (e: Exception) {
                // Jika error (misal server mati atau tidak ada internet)
                e.printStackTrace()
                _errorMessage.value = "Gagal memuat data: ${e.localizedMessage}"
            } finally {
                // Selesai loading (baik sukses maupun gagal)
                _isLoading.value = false
            }
        }
    }
}
