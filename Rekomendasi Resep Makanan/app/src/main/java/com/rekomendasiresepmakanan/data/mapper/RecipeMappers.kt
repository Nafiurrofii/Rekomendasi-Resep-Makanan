package com.rekomendasiresepmakanan.data.mapper

import com.google.gson.Gson
import com.rekomendasiresepmakanan.data.local.entity.RecipeEntity
import com.rekomendasiresepmakanan.data.local.entity.FavoriteEntity
import com.rekomendasiresepmakanan.data.local.model.RecipeResponse
import com.rekomendasiresepmakanan.domain.model.Recipe

/**
 * Extension functions untuk konversi antara layer:
 * RecipeResponse (API) <-> RecipeEntity (Room) <-> Recipe (Domain)
 */

// RecipeResponse to RecipeEntity
fun RecipeResponse.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        title = title,
        categoryId = categoryId ?: 0,
        description = description,
        ingredients = ingredients ?: emptyList(),
        steps = steps ?: emptyList(),
        image = imageUrl ?: "",
        updatedAt = System.currentTimeMillis(),
        cachedAt = System.currentTimeMillis()
    )
}

// Recipe to FavoriteEntity
fun Recipe.toFavoriteEntity(): FavoriteEntity {
    return FavoriteEntity(
        recipeId = id,
        name = title,
        image = image,
        imageRes = null
    )
}
