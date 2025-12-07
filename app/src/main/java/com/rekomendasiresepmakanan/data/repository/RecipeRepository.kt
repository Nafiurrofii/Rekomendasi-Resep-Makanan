package com.rekomendasiresepmakanan.data.repository

import com.rekomendasiresepmakanan.R
import com.rekomendasiresepmakanan.domain.model.Category
import com.rekomendasiresepmakanan.domain.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository {

    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        listOf(
            Category(1, "Nusantara", ""), // Image placeholder string, UI will likely ignore or use default if empty for now
            Category(2, "Chinese", ""),
            Category(3, "Western", ""),
            Category(4, "Dessert", ""),
            Category(5, "Vegan", ""),
            Category(6, "Jajanan", ""),
            Category(7, "Healthy", ""),
            Category(8, "Japanese", ""),
            Category(9, "Timur", ""),
            Category(10, "Turkish", ""),
            Category(11, "Korean", ""),
            Category(12, "Indian", ""),
            Category(13, "Italian", ""),
            Category(14, "Mexico", "")
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

    private fun getDummyRecipes(): List<Recipe> {
        val descriptionText = "Rendang adalah hidangan tradisional khas suku Minangkabau di Sumatera Barat, Indonesia, yang telah mendunia karena rasanya yang kaya rempah dan proses pembuatannya yang unik. Berbeda dengan hidangan lain, rendang sebenarnya adalah nama untuk sebuah proses memasak, yaitu 'marandang', yang berarti mengeringkan atau memasak dalam waktu yang lama. Proses inilah yang menghasilkan tekstur kering dan cita rasa yang sangat khas."
        
        val ingredientsRendang = listOf(
            "Bahan utama:",
            "1. 2 liter santan dari 2 butir kelapa tua",
            "2. 1 kg daging sapi (bagian sandung lamur atau paha, potong ukuran sedang)",
            "Bumbu halus:",
            "1. 12 butir bawang merah",
            "2. 5 butir kemiri, sangrai",
            "3. 6 siung bawang putih",
            "4. 3 cm jahe",
            "5. 4 cm lengkuas",
            "6. 3 cm kunyit",
            "7. 10 buah cabai merah besar (boleh campur cabai rawit sesuai selera)",
            "8. 1 sdt ketumbar",
            "9. 1 sdt jintan (disangrai sebentar)",
            "Bumbu pelengkap:",
            "1. 2 batang serai, memarkan",
            "2. 5 lembar daun jeruk",
            "3. 2 lembar daun salam",
            "4. 2 butir asam kandis (atau 1 sdm air asam jawa)",
            "5. Garam secukupnya",
            "6. Gula merah secukupnya"
        )

        val stepsRendang = listOf(
            "1. Haluskan bumbu:\nBlender atau ulek semua bahan bumbu halus hingga benar-benar lembut.",
            "2. Masak santan:\nMasukkan santan ke dalam wajan besar, tambahkan bumbu halus, serai, daun jeruk, daun salam, dan asam kandis. Aduk perlahan agar santan tidak pecah.",
            "3. Tambahkan daging:\nSetelah santan mulai panas dan bumbu tercampur rata, masukkan potongan daging sapi.",
            "4. Masak dengan api sedang:\nAduk sesekali agar santan tidak pecah. Setelah mendidih, kecilkan api.",
            "5. Masak hingga santan mengental:\nTeruskan memasak selama 2–3 jam hingga santan menyusut, berminyak, dan daging empuk. Aduk sesekali agar tidak gosong di dasar wajan.",
            "6. Koreksi rasa:\nTambahkan garam dan gula merah sesuai selera. Jika ingin rendang lebih kering, teruskan memasak hingga berwarna coklat tua dan benar-benar berminyak.",
            "7. Sajikan:\nAngkat dan sajikan rendang dengan nasi hangat."
        )

        // Creating variations for demo purposes
        return listOf(
            Recipe(1, "Rendang", "Nusantara", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(2, "Fuyunghay", "Chinese", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(3, "Bolognaise", "Western", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(4, "Salad Buah", "Vegan", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(5, "Nasi Pecel", "Nusantara", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(6, "Capcay", "Chinese", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(7, "Steak", "Western", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(8, "Gado-gado", "Nusantara", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(9, "Sate Ayam", "Nusantara", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(10, "Pizza", "Western", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(11, "Sushi", "Japanese", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(12, "Kimchi", "Korean", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(13, "Curry", "Indian", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(14, "Tacos", "Mexico", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang),
            Recipe(15, "Bakso", "Jajanan", R.drawable.rendang1, descriptionText, ingredientsRendang, stepsRendang)
        )
    }
}
