package com.rekomendasiresepmakanan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rekomendasiresepmakanan.domain.model.Recipe

@Entity(tableName = "recipes_cache")
data class RecipeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val categoryId: Int,
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val image: String,
    val updatedAt: Long = System.currentTimeMillis(),
    val cachedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Recipe {
        return Recipe(
            id = id,
            title = title,
            categoryId = categoryId,
            category = null, // Category obj not stored flat here usually
            description = description,
            ingredients = ingredients,
            steps = steps,
            image = image
        )
    }
}

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
