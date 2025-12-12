package com.rekomendasiresepmakanan.data.model

import com.google.gson.annotations.SerializedName

// Ini untuk menangkap objek pembungkus JSON dari Laravel
data class ApiResponse<T>(
    val status: String,
    val data: T
)

// Ini data resep aslinya
data class RecipeNetwork(
    val id: Int,
    val title: String,
    val description: String,
    val origin: String, // Menambahkan field origin
    @SerializedName("image_url") // Pastikan nama ini SAMA PERSIS dengan key JSON dari Laravel
    val imageUrl: String,
    val ingredients: List<String>,
    val steps: List<String>
)
