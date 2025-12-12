package com.rekomendasiresepmakanan.data.repository

import com.rekomendasiresepmakanan.data.local.dao.FavoriteDao
import com.rekomendasiresepmakanan.data.local.entity.FavoriteEntity
import com.rekomendasiresepmakanan.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

/**
 * Repository untuk mengelola operasi favorit.
 * Bertindak sebagai single source of truth untuk data favorit.
 */
class FavoriteRepository(private val favoriteDao: FavoriteDao) {
    
    /**
     * Mengambil semua favorit sebagai Flow.
     * UI akan otomatis update ketika ada perubahan.
     */
    fun getAllFavorites(): Flow<List<FavoriteEntity>> {
        return favoriteDao.getAllFavorites()
    }
    
    /**
     * Cek apakah resep dengan recipeId tertentu sudah difavoritkan.
     */
    suspend fun isFavorite(recipeId: Int): Boolean {
        return favoriteDao.getFavoriteByRecipeId(recipeId) != null
    }
    
    /**
     * Menambahkan resep ke favorit.
     */
    suspend fun addFavorite(recipe: Recipe) {
        val favorite = FavoriteEntity(
            recipeId = recipe.id,
            name = recipe.title,
            image = recipe.image,
            imageRes = recipe.imageRes
        )
        favoriteDao.insertFavorite(favorite)
    }
    
    /**
     * Menghapus resep dari favorit berdasarkan recipeId.
     */
    suspend fun removeFavorite(recipeId: Int) {
        favoriteDao.deleteFavoriteByRecipeId(recipeId)
    }
    
    /**
     * Toggle status favorit: tambah jika belum ada, hapus jika sudah ada.
     */
    suspend fun toggleFavorite(recipe: Recipe) {
        if (isFavorite(recipe.id)) {
            removeFavorite(recipe.id)
        } else {
            addFavorite(recipe)
        }
    }
}
