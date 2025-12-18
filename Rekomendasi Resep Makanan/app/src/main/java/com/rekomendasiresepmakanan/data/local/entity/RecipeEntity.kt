package com.rekomendasiresepmakanan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.google.gson.Gson

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val image: String?,
    val categoryId: Int,
    val updatedAt: Long = System.currentTimeMillis(),
    val cachedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Recipe {
        return Recipe(
            id = id,
            title = title,
            categoryId = categoryId,
            category = null,
            description = description,
            ingredients = ingredients,
            steps = steps,
            image = image ?: ""
        )
    }
}

class Converters {
    @androidx.room.TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @androidx.room.TypeConverter
    fun toStringList(value: String): List<String> {
        return try {
            Gson().fromJson(value, Array<String>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
