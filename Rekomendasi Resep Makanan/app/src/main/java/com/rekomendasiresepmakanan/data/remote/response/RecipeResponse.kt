package com.rekomendasiresepmakanan.data.remote.response

import com.google.gson.annotations.SerializedName
import com.rekomendasiresepmakanan.domain.model.Recipe

data class RecipeResponse(
    val id: Int,
    @SerializedName("nama_resep")
    val title: String?,
    @SerializedName("deskripsi")
    val description: String?,
    @SerializedName("origin")
    val origin: String? = null,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("bahan")
    val ingredients: List<String>?,
    @SerializedName("langkah_langkah")
    val steps: List<String>?,
    @SerializedName("category_id")
    val categoryId: Int?
) {
    fun toDomain(): Recipe {
        return Recipe(
            id = id,
            title = title ?: "",
            categoryId = categoryId ?: 0,
            category = null,
            description = description ?: "",
            ingredients = ingredients ?: emptyList(),
            steps = steps ?: emptyList(),
            image = imageUrl ?: ""
        )
    }
}
