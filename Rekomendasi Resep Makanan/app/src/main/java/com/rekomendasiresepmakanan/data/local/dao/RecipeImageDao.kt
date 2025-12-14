package com.rekomendasiresepmakanan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rekomendasiresepmakanan.data.local.entity.RecipeImageEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeImageDao {
    @Query("SELECT imageBlob FROM recipe_images WHERE recipeId = :recipeId")
    fun getImageFlow(recipeId: Int): Flow<ByteArray?>

    @Query("SELECT imageBlob FROM recipe_images WHERE recipeId = :recipeId")
    suspend fun getImage(recipeId: Int): ByteArray?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: RecipeImageEntity)

    @Query("DELETE FROM recipe_images WHERE recipeId = :recipeId")
    suspend fun deleteByRecipeId(recipeId: Int)
}
