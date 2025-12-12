package com.rekomendasiresepmakanan.data.local.dao

import androidx.room.*
import com.rekomendasiresepmakanan.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) untuk operasi CRUD pada tabel favorites.
 */
@Dao
interface FavoriteDao {
    
    /**
     * Mengambil semua resep favorit sebagai Flow.
     * Flow akan otomatis update UI ketika data berubah.
     */
    @Query("SELECT * FROM favorites ORDER BY id DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>
    
    /**
     * Mengecek apakah resep dengan recipeId tertentu sudah difavoritkan.
     * Return null jika belum ada.
     */
    @Query("SELECT * FROM favorites WHERE recipeId = :recipeId LIMIT 1")
    suspend fun getFavoriteByRecipeId(recipeId: Int): FavoriteEntity?
    
    /**
     * Menambahkan resep ke favorit.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)
    
    /**
     * Menghapus resep dari favorit berdasarkan recipeId.
     */
    @Query("DELETE FROM favorites WHERE recipeId = :recipeId")
    suspend fun deleteFavoriteByRecipeId(recipeId: Int)
    
    /**
     * Menghapus semua favorit (untuk testing/debugging).
     */
    @Query("DELETE FROM favorites")
    suspend fun deleteAllFavorites()
}
