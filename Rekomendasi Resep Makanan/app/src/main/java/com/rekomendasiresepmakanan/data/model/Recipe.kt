package com.rekomendasiresepmakanan.data.model

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class Recipe(
    val id: Int,
    @SerializedName("nama_resep")
    val title: String,
    @SerializedName("category_id")
    val categoryId: Int,
    val category: Category?,
    @SerializedName("deskripsi")
    val description: String,
    @SerializedName("bahan")
    val ingredients: List<String>,
    @SerializedName("langkah_langkah")
    val steps: List<String>,
    @SerializedName("gambar")
    val image: String,
    val imageRes: Int? = null
)

