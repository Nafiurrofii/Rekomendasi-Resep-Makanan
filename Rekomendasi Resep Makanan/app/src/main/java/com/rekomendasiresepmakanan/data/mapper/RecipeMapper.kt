package com.rekomendasiresepmakanan.data.mapper

import com.rekomendasiresepmakanan.data.local.entity.FavoriteEntity
import com.rekomendasiresepmakanan.data.local.entity.RecipeEntity
import com.rekomendasiresepmakanan.data.remote.response.RecipeResponse
import com.rekomendasiresepmakanan.domain.model.Recipe

fun Recipe.toFavoriteEntity(): FavoriteEntity {
    return FavoriteEntity(
        recipeId = this.id,
        name = this.title,
        image = this.image,
        imageRes = this.imageRes
    )
}

fun RecipeResponse.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = this.id,
        title = this.title ?: "",
        categoryId = this.categoryId ?: 0,
        description = this.description ?: "",
        ingredients = this.ingredients ?: emptyList(),
        steps = this.steps ?: emptyList(),
        image = this.imageUrl ?: "",
        updatedAt = System.currentTimeMillis(),
        cachedAt = System.currentTimeMillis()
    )
}
