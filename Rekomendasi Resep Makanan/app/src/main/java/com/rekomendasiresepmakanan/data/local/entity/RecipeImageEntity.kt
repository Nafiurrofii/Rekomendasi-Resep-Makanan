package com.rekomendasiresepmakanan.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_images")
data class RecipeImageEntity(
    @PrimaryKey
    val recipeId: Int,
    
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val imageBlob: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecipeImageEntity

        if (recipeId != other.recipeId) return false
        if (!imageBlob.contentEquals(other.imageBlob)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = recipeId
        result = 31 * result + imageBlob.contentHashCode()
        return result
    }
}
