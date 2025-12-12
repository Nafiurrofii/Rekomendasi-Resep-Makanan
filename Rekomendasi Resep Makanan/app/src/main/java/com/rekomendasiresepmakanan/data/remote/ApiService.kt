package com.rekomendasiresepmakanan.data.remote

import com.rekomendasiresepmakanan.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Body
import com.rekomendasiresepmakanan.domain.model.Recipe

interface ApiService {

    // Mengambil SEMUA resep dari endpoint Laravel: /api/recipes
    @GET("api/recipes")
    suspend fun getRecipes(): ApiResponse<List<Recipe>>

    // (Opsional) Mengambil DETAIL resep berdasarkan ID: /api/recipes/{id}
    @GET("api/recipes/{id}")
    suspend fun getRecipeDetail(@Path("id") id: Int): ApiResponse<Recipe>

    // Menambahkan resep baru
    @POST("api/recipes")
    suspend fun addRecipe(@Body recipe: Recipe): ApiResponse<Recipe>

    // Update resep
    @retrofit2.http.PUT("api/recipes/{id}")
    suspend fun updateRecipe(
        @retrofit2.http.Path("id") id: Int,
        @retrofit2.http.Body recipe: com.rekomendasiresepmakanan.data.remote.request.RecipeRequest
    ): ApiResponse<Recipe>

    // Delete resep
    @retrofit2.http.DELETE("api/recipes/{id}")
    suspend fun deleteRecipe(@retrofit2.http.Path("id") id: Int): com.rekomendasiresepmakanan.data.remote.response.BaseResponse
}
