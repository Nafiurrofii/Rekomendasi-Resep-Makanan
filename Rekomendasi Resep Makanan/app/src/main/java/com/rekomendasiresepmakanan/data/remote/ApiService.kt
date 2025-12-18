package com.rekomendasiresepmakanan.data.remote

import com.rekomendasiresepmakanan.data.remote.response.RecipeResponse
import com.rekomendasiresepmakanan.data.remote.request.LoginRequest
import com.rekomendasiresepmakanan.data.remote.request.RegisterRequest
import com.rekomendasiresepmakanan.data.remote.response.AuthResponse
import com.rekomendasiresepmakanan.data.remote.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<AuthResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): BaseResponse<AuthResponse>

    // Mengambil semua resep
    @GET("recipes")
    suspend fun getRecipes(): BaseResponse<List<RecipeResponse>>

    // Mengambil semua kategori
    @GET("categories")
    suspend fun getCategories(): BaseResponse<List<com.rekomendasiresepmakanan.domain.model.Category>>

    // Mengambil detail resep berdasarkan ID
    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: Int): BaseResponse<RecipeResponse>

    /**
     * Menambah resep baru dengan gambar (Multipart)
     * Menggunakan @Part eksplisit untuk menghindari masalah encoding map
     */
    @Multipart
    @POST("recipes")
    suspend fun addRecipe(
        @Part("nama_resep") name: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part("deskripsi") description: RequestBody,
        @Part("bahan") ingredients: RequestBody,
        @Part("langkah_langkah") steps: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("image_url") imageUrl: RequestBody?
    ): BaseResponse<RecipeResponse>

    /**
     * Mengupdate resep dengan atau tanpa gambar baru
     */
    @Multipart
    @POST("recipes/{id}")
    suspend fun updateRecipe(
        @Path("id") recipeId: Int,
        @Part("nama_resep") name: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part("deskripsi") description: RequestBody,
        @Part("bahan") ingredients: RequestBody,
        @Part("langkah_langkah") steps: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("image_url") imageUrl: RequestBody?
    ): BaseResponse<RecipeResponse>

    /**
     * Menghapus resep
     */
    @DELETE("recipes/{id}")
    suspend fun deleteRecipe(@Path("id") recipeId: Int): BaseResponse<Unit>

    @POST("logout")
    suspend fun logout(): BaseResponse<Unit>
}
