package com.rekomendasiresepmakanan.data.repository

import com.rekomendasiresepmakanan.R
import com.rekomendasiresepmakanan.domain.model.Category
import com.rekomendasiresepmakanan.domain.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class RecipeRepository {

    // Simulasi database favorit lokal
    private val favoriteRecipeIds = MutableStateFlow(setOf(1, 5)) // Rendang dan Nasi Pecel sebagai favorit default

    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        // ... (kode kategori tetap sama)
        listOf(
            Category(1, "Nusantara", ""),
            Category(2, "Chinese", ""),
            Category(3, "Western", ""),
            Category(4, "Dessert", "")
        )
    }

    suspend fun getPopularRecipes(): List<Recipe> = withContext(Dispatchers.IO) {
        getDummyRecipes()
    }

    suspend fun searchRecipes(query: String): List<Recipe> = withContext(Dispatchers.IO) {
        val allRecipes = getDummyRecipes()
        if (query.isBlank()) return@withContext emptyList()
        
        allRecipes.filter {
            it.title.contains(query, ignoreCase = true) || 
            it.category.contains(query, ignoreCase = true)
        }
    }
    
    suspend fun getSuggestions(query: String): List<String> = withContext(Dispatchers.IO) {
        if (query.length < 2) return@withContext emptyList()
        val allRecipes = getDummyRecipes()
        allRecipes
            .filter { it.title.contains(query, ignoreCase = true) }
            .map { it.title }
            .distinct()
            .take(5)
    }

    suspend fun getRecipeById(id: Int): Recipe? = withContext(Dispatchers.IO) {
        getDummyRecipes().find { it.id == id }
    }

    suspend fun getRecipesByCategory(categoryName: String): List<Recipe> = withContext(Dispatchers.IO) {
        getDummyRecipes().filter { 
            it.category.equals(categoryName, ignoreCase = true) 
        }
    }

    // Fungsi baru untuk mendapatkan resep favorit
    suspend fun getFavoriteRecipes(): List<Recipe> = withContext(Dispatchers.IO) {
        val favoriteIds = favoriteRecipeIds.value
        getDummyRecipes().filter { it.id in favoriteIds }
    }

    // Fungsi baru untuk cek status favorit
    fun isFavorite(id: Int): Boolean {
        return id in favoriteRecipeIds.value
    }

    // Fungsi baru untuk toggle status favorit
    suspend fun toggleFavoriteStatus(id: Int) = withContext(Dispatchers.Default) {
        val currentFavorites = favoriteRecipeIds.value.toMutableSet()
        if (id in currentFavorites) {
            currentFavorites.remove(id)
        } else {
            currentFavorites.add(id)
        }
        favoriteRecipeIds.value = currentFavorites
    }

    private fun getDummyRecipes(): List<Recipe> {
        val descriptionText = "Rendang adalah hidangan tradisional khas suku Minangkabau..."
        val ingredientsRendang = listOf("Bahan utama:", "1. 2 liter santan...")
        val stepsRendang = listOf("1. Haluskan bumbu...", "2. Masak santan...")

        return listOf(
            Recipe(1, "Rendang", "Nusantara", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(2, "Fuyunghay", "Chinese", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(3, "Bolognaise", "Western", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(4, "Salad Buah", "Vegan", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(5, "Nasi Pecel", "Nusantara", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(6, "Capcay", "Chinese", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang)
            // ... resep lainnya
        )
    }
}
