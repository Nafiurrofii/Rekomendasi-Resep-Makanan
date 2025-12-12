package com.rekomendasiresepmakanan.ui.screen.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.local.AppDatabase
import com.rekomendasiresepmakanan.data.local.entity.FavoriteEntity
import com.rekomendasiresepmakanan.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel untuk FavoriteScreen.
 * Mengelola daftar favorit dan operasi hapus favorit.
 */
class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteRepository: FavoriteRepository

    // StateFlow untuk daftar favorit (reactive)
    val favorites: StateFlow<List<FavoriteEntity>>

    init {
        val database = AppDatabase.getDatabase(application)
        favoriteRepository = FavoriteRepository(database.favoriteDao())
        
        // Convert Flow dari repository ke StateFlow dengan initial value empty list
        favorites = favoriteRepository.getAllFavorites()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    /**
     * Menghapus resep dari favorit berdasarkan recipeId.
     */
    fun removeFavorite(recipeId: Int) {
        viewModelScope.launch {
            favoriteRepository.removeFavorite(recipeId)
        }
    }
}
