package com.rekomendasiresepmakanan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "recipe_images")
data class RecipeImageEntity(
    @PrimaryKey
    @ColumnInfo(name = "recipe_id")
    val recipeId: Int,
    
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val imageData: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecipeImageEntity

        if (recipeId != other.recipeId) return false
        if (!imageData.contentEquals(other.imageData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = recipeId
        result = 31 * result + imageData.contentHashCode()
        return result
    }
}
