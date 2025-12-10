package com.rekomendasiresepmakanan.data.repository

import com.rekomendasiresepmakanan.R
import com.rekomendasiresepmakanan.domain.model.Category
import com.rekomendasiresepmakanan.domain.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class RecipeRepository {

    private val favoriteRecipeIds = MutableStateFlow(setOf(1, 5))
    private val dummyRecipes = MutableStateFlow(getInitialDummyRecipes())

    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        listOf(
            Category(1, "Nusantara", ""),
            Category(2, "Chinese", ""),
            Category(3, "Western", ""),
            Category(4, "Dessert", "")
        )
    }

    suspend fun getPopularRecipes(): List<Recipe> = withContext(Dispatchers.IO) {
        dummyRecipes.value
    }

    suspend fun searchRecipes(query: String): List<Recipe> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        dummyRecipes.value.filter {
            it.title.contains(query, ignoreCase = true) || 
            it.category.contains(query, ignoreCase = true)
        }
    }
    
    suspend fun getSuggestions(query: String): List<String> = withContext(Dispatchers.IO) {
        if (query.length < 2) return@withContext emptyList()
        dummyRecipes.value
            .filter { it.title.contains(query, ignoreCase = true) }
            .map { it.title }
            .distinct()
            .take(5)
    }

    suspend fun getRecipeById(id: Int): Recipe? = withContext(Dispatchers.IO) {
        dummyRecipes.value.find { it.id == id }
    }

    suspend fun getRecipesByCategory(categoryName: String): List<Recipe> = withContext(Dispatchers.IO) {
        dummyRecipes.value.filter { 
            it.category.equals(categoryName, ignoreCase = true) 
        }
    }

    suspend fun getFavoriteRecipes(): List<Recipe> = withContext(Dispatchers.IO) {
        val favoriteIds = favoriteRecipeIds.value
        dummyRecipes.value.filter { it.id in favoriteIds }
    }

    fun isFavorite(id: Int): Boolean {
        return id in favoriteRecipeIds.value
    }

    suspend fun toggleFavoriteStatus(id: Int) = withContext(Dispatchers.Default) {
        val currentFavorites = favoriteRecipeIds.value.toMutableSet()
        if (id in currentFavorites) {
            currentFavorites.remove(id)
        } else {
            currentFavorites.add(id)
        }
        favoriteRecipeIds.value = currentFavorites
    }
    
    // Fungsi baru untuk menambah resep
    suspend fun addRecipe(name: String, ingredients: String, steps: String, imageUrl: String) = withContext(Dispatchers.IO) {
        val currentList = dummyRecipes.value.toMutableList()
        val newId = (currentList.maxOfOrNull { it.id } ?: 0) + 1
        currentList.add(
            Recipe(
                id = newId,
                title = name,
                category = "Lainnya", // Kategori default
                image = R.drawable.rendang1, // Gambar default, karena imageUrl string, tapi model kita Int
                description = "Deskripsi untuk $name",
                ingredients = ingredients.split("\n"),
                steps = steps.split("\n")
            )
        )
        dummyRecipes.value = currentList
    }

    private fun getInitialDummyRecipes(): List<Recipe> {
        val descriptionText = "Rendang adalah hidangan tradisional khas suku Minangkabau..."
        val ingredientsRendang = listOf("Bahan utama:", "1. 2 liter santan...")
        val stepsRendang = listOf("1. Haluskan bumbu...", "2. Masak santan...")

        return listOf(
            Recipe(1, "Rendang", "Nusantara", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(2, "Fuyunghay", "Chinese", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(3, "Bolognaise", "Western", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang)
        )
    }
}
