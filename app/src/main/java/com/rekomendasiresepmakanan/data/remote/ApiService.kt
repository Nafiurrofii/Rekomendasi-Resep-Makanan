package com.rekomendasiresepmakanan.data.remote

import com.rekomendasiresepmakanan.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // Mengambil SEMUA resep dari endpoint Laravel: /api/recipes
    @GET("api/recipes")
    suspend fun getRecipes(): ApiResponse

    // (Opsional) Mengambil DETAIL resep berdasarkan ID: /api/recipes/{id}
    @GET("api/recipes/{id}")
    suspend fun getRecipeDetail(@Path("id") id: Int): ApiResponse
}
