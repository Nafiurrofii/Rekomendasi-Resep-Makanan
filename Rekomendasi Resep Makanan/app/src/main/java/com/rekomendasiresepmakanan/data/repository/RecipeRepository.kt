package com.rekomendasiresepmakanan.data.repository

import com.rekomendasiresepmakanan.R
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.domain.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

object RecipeRepository {

    private val categories = listOf(
        Category(1, "Nusantara"),
        Category(2, "Chinese"),
        Category(3, "Western"),
        Category(4, "Dessert"),
        Category(5, "Lainnya")
    )

    private val dummyRecipes = MutableStateFlow(getInitialDummyRecipes())
    val recipes: StateFlow<List<Recipe>> = dummyRecipes.asStateFlow()

    private val favoriteRecipeIds = MutableStateFlow<Set<Int>>(setOf())

    suspend fun getPopularRecipes(): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            // Fetch from API
            val response = com.rekomendasiresepmakanan.data.remote.RetrofitClient.instance.getRecipes()
            if (response.status == "success") {
                // Map local image resources
                val apiRecipes = response.data.map { recipe ->
                    val localRes = when {
                        recipe.title.contains("Rendang", true) -> R.drawable.rendang
                        recipe.title.contains("Fuyunghai", true) -> R.drawable.fuyunghai
                        recipe.title.contains("Goreng", true) -> R.drawable.nasigoreng
                        else -> null
                    }
                    recipe.copy(imageRes = localRes)
                }
                
                // Update cache
                dummyRecipes.value = apiRecipes
                return@withContext apiRecipes
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        dummyRecipes.value
    }

    suspend fun searchRecipes(query: String): List<Recipe> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()

        dummyRecipes.value
            .filter { recipe ->
                recipe.title.contains(query, ignoreCase = true) ||
                recipe.description.contains(query, ignoreCase = true) ||
                recipe.category?.name?.contains(query, ignoreCase = true) == true
            }
            .take(5)
    }

    suspend fun searchSuggestions(query: String): List<String> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()

        dummyRecipes.value
            .filter { it.title.contains(query, ignoreCase = true) }
            .map { it.title }
            .distinct()
            .take(5)
    }

    suspend fun getRecipeById(id: Int): Recipe? = withContext(Dispatchers.IO) {
        try {
            // Fetch from API
            val response = com.rekomendasiresepmakanan.data.remote.RetrofitClient.instance.getRecipeDetail(id)
            if (response.status == "success") {
                // Map local image resource
                val recipe = response.data
                val localRes = when {
                    recipe.title.contains("Rendang", true) -> R.drawable.rendang
                    recipe.title.contains("Fuyunghai", true) -> R.drawable.fuyunghai
                    recipe.title.contains("Goreng", true) -> R.drawable.nasigoreng
                    else -> null
                }
                return@withContext recipe.copy(imageRes = localRes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        // Fallback: search in cached data
        dummyRecipes.value.find { it.id == id }
    }

    suspend fun getRecipesByCategory(categoryName: String): List<Recipe> = withContext(Dispatchers.IO) {
        dummyRecipes.value.filter { it.category?.name == categoryName }
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
    
    // Fungsi baru untuk menambah resep ke backend
    suspend fun addRecipe(
        name: String, 
        categoryId: Int,
        description: String, 
        ingredients: String, 
        steps: String, 
        imageUrl: String
    ): Result<Recipe> = withContext(Dispatchers.IO) {
        try {
            // Prepare recipe object untuk dikirim ke API
            val newRecipe = Recipe(
                id = 0,
                title = name,
                categoryId = categoryId,
                category = null,
                image = imageUrl.ifBlank { "" },
                description = description,
                ingredients = ingredients.split("\n").filter { it.isNotBlank() },
                steps = steps.split("\n").filter { it.isNotBlank() }
            )

            // Kirim ke API
            val response = com.rekomendasiresepmakanan.data.remote.RetrofitClient.instance.addRecipe(newRecipe)
            
            if (response.status == "success") {
                // Update cache lokal
                val currentList = dummyRecipes.value.toMutableList()
                currentList.add(response.data)
                dummyRecipes.value = currentList
                
                return@withContext Result.success(response.data)
            } else {
                return@withContext Result.failure(Exception("Failed to add recipe"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.failure(e)
        }
    }

    suspend fun updateRecipe(id: Int, request: com.rekomendasiresepmakanan.data.remote.request.RecipeRequest): Result<Recipe> = withContext(Dispatchers.IO) {
        try {
            val response = com.rekomendasiresepmakanan.data.remote.RetrofitClient.instance.updateRecipe(id, request)
            if (response.status == "success") {
                 // Update cache
                 val currentList = dummyRecipes.value.toMutableList()
                 val index = currentList.indexOfFirst { it.id == id }
                 if (index != -1) {
                     currentList[index] = response.data
                     dummyRecipes.value = currentList
                 }
                 Result.success(response.data)
            } else {
                 Result.failure(Exception("Failed to update recipe"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteRecipe(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = com.rekomendasiresepmakanan.data.remote.RetrofitClient.instance.deleteRecipe(id)
            if (response.status == "success") {
                 // Update cache
                 val currentList = dummyRecipes.value.toMutableList()
                 currentList.removeAll { it.id == id }
                 dummyRecipes.value = currentList
                 Result.success(Unit)
            } else {
                 Result.failure(Exception("Failed to delete recipe"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getInitialDummyRecipes(): List<Recipe> {
        val descriptionText = "Rendang adalah hidangan tradisional khas suku Minangkabau..."
        val ingredientsRendang = listOf("Bahan utama:", "1. 2 liter santan...")
        val stepsRendang = listOf("1. Haluskan bumbu...", "2. Masak santan...")

        return listOf(
            Recipe(
                id = 1, 
                title = "Rendang", 
                categoryId = 1,
                category = Category(1, "Nusantara"),
                image = "", 
                description = "Deskripsi Rendang...", 
                ingredients = ingredientsRendang, 
                steps = stepsRendang, 
                imageRes = R.drawable.rendang
            ),
            Recipe(
                id = 2, 
                title = "Fuyunghay", 
                categoryId = 2,
                category = Category(2, "Chinese"),
                image = "", 
                description = "Deskripsi Fuyunghay...", 
                ingredients = ingredientsRendang, 
                steps = stepsRendang, 
                imageRes = R.drawable.fuyunghai
            ),
            Recipe(
                id = 3, 
                title = "Bolognaise", 
                categoryId = 3,
                category = Category(3, "Western"),
                image = "", 
                description = "Deskripsi Bolognaise...", 
                ingredients = ingredientsRendang, 
                steps = stepsRendang, 
                imageRes = R.drawable.nasigoreng
            )
        )
    }
}
