package com.rekomendasiresepmakanan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rekomendasiresepmakanan.data.local.entity.RecipeImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeImageDao {
    @Query("SELECT imageData FROM recipe_images WHERE recipe_id = :recipeId")
    fun getImageFlow(recipeId: Int): Flow<ByteArray?>

    @Query("SELECT * FROM recipe_images WHERE recipe_id = :recipeId")
    suspend fun getImage(recipeId: Int): RecipeImageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: RecipeImageEntity)
}
