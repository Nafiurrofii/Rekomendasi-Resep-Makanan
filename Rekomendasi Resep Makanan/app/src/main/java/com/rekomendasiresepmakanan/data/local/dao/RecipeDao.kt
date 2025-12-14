package com.rekomendasiresepmakanan.data.local.dao

import androidx.room.*
import com.rekomendasiresepmakanan.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    /**
     * Get all cached recipes as Flow (reactive)
     */
    @Query("SELECT * FROM recipes_cache ORDER BY updatedAt DESC")
    fun getAllRecipes(): Flow<List<RecipeEntity>>
    
    /**
     * Get specific recipe by ID
     */
    @Query("SELECT * FROM recipes_cache WHERE id = :id")
    suspend fun getRecipeById(id: Int): RecipeEntity?
    
    /**
     * Insert multiple recipes (for bulk cache update)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<RecipeEntity>)
    
    /**
     * Insert single recipe
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: RecipeEntity)
    
    /**
     * Update recipe
     */
    @Update
    suspend fun update(recipe: RecipeEntity)
    
    /**
     * Delete recipe by ID
     */
    @Query("DELETE FROM recipes_cache WHERE id = :id")
    suspend fun deleteById(id: Int)
    
    /**
     * Clear all cached recipes
     */
    @Query("DELETE FROM recipes_cache")
    suspend fun clearAll()
    
    /**
     * Get count of cached recipes
     */
    @Query("SELECT COUNT(*) FROM recipes_cache")
    suspend fun getCount(): Int
    
    /**
     * Get recipes cached before certain timestamp (for invalidation)
     */
    @Query("SELECT * FROM recipes_cache WHERE cachedAt < :timestamp")
    suspend fun getOldRecipes(timestamp: Long): List<RecipeEntity>
    
    /**
     * Delete old cached recipes
     */
    @Query("DELETE FROM recipes_cache WHERE cachedAt < :timestamp")
    suspend fun deleteOldRecipes(timestamp: Long)
}
