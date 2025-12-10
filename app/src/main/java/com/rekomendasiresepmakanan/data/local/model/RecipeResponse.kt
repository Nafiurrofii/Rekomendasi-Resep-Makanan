package com.rekomendasiresepmakanan.data.model

import com.google.gson.annotations.SerializedName

// Ini untuk menangkap objek pembungkus JSON dari Laravel
data class ApiResponse(
    val status: String,
    val data: List<RecipeNetwork>
)

// Ini data resep aslinya
data class RecipeNetwork(
    val id: Int,
    val title: String,
    val description: String,
    @SerializedName("image_url") // Pastikan nama ini SAMA PERSIS dengan key JSON dari Laravel
    val imageUrl: String
)
