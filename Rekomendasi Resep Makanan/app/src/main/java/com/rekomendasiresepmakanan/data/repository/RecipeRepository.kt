package com.rekomendasiresepmakanan.data.repository

import android.content.Context
import android.net.Uri
import com.rekomendasiresepmakanan.data.local.AppDatabase
import com.rekomendasiresepmakanan.data.mapper.toEntity
import com.rekomendasiresepmakanan.data.mapper.toFavoriteEntity
import com.rekomendasiresepmakanan.data.remote.RetrofitClient
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.domain.model.Resource
import com.rekomendasiresepmakanan.domain.model.UiState
import com.rekomendasiresepmakanan.util.NetworkConnectivityHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import com.rekomendasiresepmakanan.data.local.entity.RecipeImageEntity
import kotlinx.coroutines.withContext

object RecipeRepository {

    private val apiService = RetrofitClient.apiService

    /**
     * Get all recipes dengan HYBRID logic:
     * 1. Emit data dari Cache (Room) terlebih dahulu (Instant UI)
     * 2. Jika Online, fetch dari API
     * 3. Jika API sukses, update Cache (Room) -> UI update otomatis via Flow
     * 4. Jika API gagal/offline, UI tetap menampilkan data Cache
     */
    fun getRecipes(context: Context): Flow<Resource<List<Recipe>>> = flow {
        val database = AppDatabase.getDatabase(context)
        val recipeDao = database.recipeDao()

        // 1. Emit Cache First
        val cachedRecipes = recipeDao.getAllRecipes().first()
        if (cachedRecipes.isNotEmpty()) {
            emit(Resource.Success(cachedRecipes.map { it.toDomain() }))
        } else {
            emit(Resource.Loading()) // Loading only if cache is empty
        }

        // 2. Fetch API if Online
        if (NetworkConnectivityHelper.isOnline(context)) {
            try {
                val response = apiService.getRecipes()
                if (response.status == "success") {
                    val recipes = response.data ?: emptyList()

                    // 3. Update Cache
                    if (recipes.isNotEmpty()) {
                        recipeDao.insertAll(recipes.map { it.toEntity() })
                        emit(Resource.Success(recipes.map { it.toDomain() }))
                    }
                } else {
                    if (cachedRecipes.isEmpty()) {
                        emit(Resource.Error(response.message ?: "Failed to fetch data"))
                    }
                }
            } catch (e: Exception) {
                if (cachedRecipes.isEmpty()) {
                    emit(Resource.Error(e.message ?: "Network error"))
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    // Legacy support for other parts of app (masih return UiState tapi non-hybrid)
    suspend fun getRecipeById(context: Context, id: Int): Recipe? {
        // Coba ambil dari Network dulu (Fresh Data)
        try {
            val response = apiService.getRecipeById(id)
            if (response.status == "success" && response.data != null) {
                // Simpan ke Cache
                val recipe = response.data.toDomain()
                val database = AppDatabase.getDatabase(context)
                database.recipeDao().insert(response.data.toEntity())
                return recipe
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Jika Network gagal/offline, ambil dari Cache
        val database = AppDatabase.getDatabase(context)
        val cachedEntity = database.recipeDao().getRecipeById(id)
        return cachedEntity?.toDomain()
    }

    /**
     * Add Recipe - Online Only (Sync logic could be added)
     */
    /**
     * Add Recipe - Online Only (Sync logic could be added)
     * Updated to use Explicit Parts to fix 422 Error
     */
    fun addRecipe(
        context: Context,
        name: String,
        categoryId: Int,
        description: String,
        ingredients: List<String>,
        steps: List<String>,
        imageUri: Uri?,
        imageUrl: String?
    ): Flow<UiState<Recipe>> = flow {
        emit(UiState.Loading)
        
        if (!NetworkConnectivityHelper.isOnline(context)) {
            emit(UiState.Error("No internet connection"))
            return@flow
        }

        try {
            val imagePart = if (imageUri != null) {
                val file = uriToFile(imageUri, context)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", file.name, requestFile)
            } else {
                null
            }

            val imageUrlBody = if (!imageUrl.isNullOrBlank()) {
                createPartFromString(imageUrl)
            } else {
                null
            }

            // Konversi data ke RequestBody (text/plain)
            val nameBody = createPartFromString(name)
            val categoryIdBody = createPartFromString(categoryId.toString())
            val descriptionBody = createPartFromString(description)
            
            // Konversi List ke JSON String
            val ingredientsJson = com.google.gson.Gson().toJson(ingredients)
            val stepsJson = com.google.gson.Gson().toJson(steps)
            
            val ingredientsBody = createPartFromString(ingredientsJson)
            val stepsBody = createPartFromString(stepsJson)

            // Panggil API dengan parameter eksplisit
            val response = apiService.addRecipe(
                name = nameBody,
                categoryId = categoryIdBody,
                description = descriptionBody,
                ingredients = ingredientsBody,
                steps = stepsBody,
                image = imagePart,
                imageUrl = imageUrlBody
            )

            response.data?.let {
                // Update Cache Room agar UI langsung refresh
                val database = AppDatabase.getDatabase(context)
                database.recipeDao().insertAll(listOf(it.toEntity()))
                
                emit(UiState.Success(it.toDomain()))
            } ?: emit(UiState.Error("Data is empty"))

        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "An unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateRecipe(
        context: Context,
        id: Int,
        name: String,
        categoryId: Int,
        description: String,
        ingredients: List<String>,
        steps: List<String>,
        imageUri: Uri?,
        imageUrl: String?
    ): Result<Boolean> {
        if (!NetworkConnectivityHelper.isOnline(context)) {
            return Result.failure(Exception("No internet connection"))
        }

        return try {
            val nameBody = createPartFromString(name)
            val categoryIdBody = createPartFromString(categoryId.toString())
            val descriptionBody = createPartFromString(description)
            
            val ingredientsJson = com.google.gson.Gson().toJson(ingredients)
            val stepsJson = com.google.gson.Gson().toJson(steps)
            
            val ingredientsBody = createPartFromString(ingredientsJson)
            val stepsBody = createPartFromString(stepsJson)

            val imagePart = if (imageUri != null) {
                val file = uriToFile(imageUri, context)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", file.name, requestFile)
            } else {
                null
            }

            val imageUrlBody = if (!imageUrl.isNullOrBlank()) {
                createPartFromString(imageUrl)
            } else {
                null
            }

            val response = apiService.updateRecipe(
                recipeId = id,
                name = nameBody,
                categoryId = categoryIdBody,
                description = descriptionBody,
                ingredients = ingredientsBody,
                steps = stepsBody,
                image = imagePart,
                imageUrl = imageUrlBody
            )

            if (response.status == "success") {
                response.data?.let { updatedRecipe ->
                     // Update Cache Room agar UI langsung refresh (termasuk Home Screen)
                     val database = AppDatabase.getDatabase(context)
                     database.recipeDao().insertAll(listOf(updatedRecipe.toEntity()))
                }
                Result.success(true)
            } else {
                Result.failure(Exception(response.message ?: "Failed to update"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteRecipe(context: Context, id: Int): Result<Unit> {
        if (!NetworkConnectivityHelper.isOnline(context)) {
            return Result.failure(Exception("No internet connection"))
        }
        
        return try {
            val response = apiService.deleteRecipe(id)
            if (response.status == "success") {
                // Delete from cache too
                val database = AppDatabase.getDatabase(context)
                database.recipeDao().deleteById(id)
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Failed to delete"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // --- FAVORITE FEATURES ---
    
    suspend fun toggleFavorite(context: Context, recipe: Recipe) {
        val database = AppDatabase.getDatabase(context)
        val dao = database.favoriteDao()
        val existing = dao.getFavoriteByRecipeId(recipe.id)
        if (existing != null) {
            dao.deleteFavoriteByRecipeId(recipe.id)
        } else {
            dao.insertFavorite(recipe.toFavoriteEntity())
        }
    }
    
    fun isFavorite(context: Context, recipeId: Int): Flow<Boolean> {
        val database = AppDatabase.getDatabase(context)
        return database.favoriteDao().isFavorite(recipeId)
    }
    
    fun getAllFavorites(context: Context) = AppDatabase.getDatabase(context).favoriteDao().getAllFavorites()

    // --- LOCAL IMAGE FEATURES ---

    fun getRecipeImageFlow(context: Context, recipeId: Int): Flow<ByteArray?> {
        val database = AppDatabase.getDatabase(context)
        return database.recipeImageDao().getImageFlow(recipeId)
    }

    suspend fun downloadAndSaveImage(context: Context, recipeId: Int, imageUrl: String) {
        val database = AppDatabase.getDatabase(context)
        // Cek jika sudah ada
        val existing = database.recipeImageDao().getImage(recipeId)
        if (existing != null) return

        if (imageUrl.isBlank()) return

        try {
            // Download as byte array with User-Agent to avoid blocks
            val bytes = withContext(Dispatchers.IO) {
                try {
                    val url = URL(imageUrl)
                    val connection = url.openConnection() as java.net.HttpURLConnection
                    connection.doInput = true
                    connection.connectTimeout = 15000 // 15 seconds
                    connection.readTimeout = 15000
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Android) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Mobile Safari/537.36")
                    
                    connection.inputStream.use { it.readBytes() }
                } catch (e: Exception) {
                    byteArrayOf()
                }
            }
            
            if (bytes.isNotEmpty()) {
                database.recipeImageDao().insert(RecipeImageEntity(recipeId, bytes))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- UTILS ---

    private fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        tempFile.deleteOnExit()
        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }
    
    // Helper function for viewmodels that need direct list
    suspend fun getRecipesDirectly(context: Context): List<Recipe> {
         val database = AppDatabase.getDatabase(context)
         val cached = database.recipeDao().getAllRecipes().first()
         return cached.map { it.toDomain() }
    }

    // --- RESTORED METHODS ---

    suspend fun getPopularRecipes(): List<Recipe> {
        return try {
            val response = apiService.getRecipes()
            if (response.status == "success") {
                val recipes = response.data?.map { it.toDomain() } ?: emptyList()
                recipes // Menampilkan semua data, tidak dibatasi 5 lagi
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getRecipesByCategory(categoryName: String): List<Recipe> {
        return try {
            val response = apiService.getRecipes()
            if (response.status == "success") {
                 val allRecipes = response.data?.map { it.toDomain() } ?: emptyList()
                 val categoryId = when(categoryName) {
                     "Nusantara" -> 1
                     "Chinese" -> 2
                     "Western" -> 3
                     "Dessert" -> 4
                     "Lainnya" -> 5
                     else -> -1
                 }
                 
                 if (categoryId != -1) {
                     allRecipes.filter { it.categoryId == categoryId }
                 } else {
                     emptyList()
                 }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun getCategories(): List<com.rekomendasiresepmakanan.domain.model.Category> {
        return try {
            val response = apiService.getCategories()
            if (response.status == "success") {
                response.data ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
