package com.rekomendasiresepmakanan.data.repository

import com.rekomendasiresepmakanan.R
import com.rekomendasiresepmakanan.domain.model.Category
import com.rekomendasiresepmakanan.domain.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository {

    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        listOf(
            Category(1, "Nusantara", ""),
            Category(2, "Chinese", ""),
            Category(3, "Western", ""),
            Category(4, "Vegan", "")
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

    // Fungsi baru untuk mendapatkan detail resep
    suspend fun getRecipeById(id: Int): Recipe? = withContext(Dispatchers.IO) {
        getDummyRecipes().find { it.id == id }
    }

    private fun getDummyRecipes(): List<Recipe> {
        val descriptionText = "Rendang adalah hidangan tradisional khas suku Minangkabau di Sumatera Barat, Indonesia, yang telah mendunia karena rasanya yang kaya rempah dan proses pembuatannya yang unik. Berbeda dengan hidangan lain, rendang sebenarnya adalah nama untuk sebuah proses memasak, yaitu 'marandang', yang berarti mengeringkan atau memasak dalam waktu yang lama. Proses inilah yang menghasilkan tekstur kering dan cita rasa yang sangat khas."
        
        val commonIngredients = listOf("Daging Sapi", "Santan", "Cabai Merah", "Bawang Merah", "Bawang Putih", "Jahe", "Lengkuas", "Serai")
        val commonSteps = listOf("Siapkan bahan.", "Tumis bumbu halus.", "Masukkan daging.", "Tambahkan santan.", "Masak hingga kering.")

        return listOf(
            Recipe(1, "Rendang", "Nusantara", R.drawable.rendang1, descriptionText, commonIngredients, commonSteps),
            Recipe(2, "Fuyunghay", "Chinese", R.drawable.rendang1, descriptionText, commonIngredients, commonSteps),
            Recipe(3, "Bolognaise", "Western", R.drawable.rendang1, descriptionText, commonIngredients, commonSteps),
            Recipe(4, "Salad Buah", "Vegan", R.drawable.rendang1, descriptionText, commonIngredients, commonSteps),
            Recipe(5, "Nasi Pecel", "Nusantara", R.drawable.rendang1, descriptionText, commonIngredients, commonSteps),
            Recipe(6, "Capcay", "Chinese", R.drawable.rendang1, descriptionText, commonIngredients, commonSteps),
            Recipe(7, "Steak", "Western", R.drawable.rendang1, descriptionText, commonIngredients, commonSteps),
            Recipe(8, "Gado-gado", "Nusantara", R.drawable.rendang1, descriptionText, commonIngredients, commonSteps),
            Recipe(9, "Sate Ayam", "Nusantara", R.drawable.rendang1, descriptionText, commonIngredients, commonSteps),
            Recipe(10, "Pizza", "Western", R.drawable.rendang1, descriptionText, commonIngredients, commonSteps)
        )
    }
}
