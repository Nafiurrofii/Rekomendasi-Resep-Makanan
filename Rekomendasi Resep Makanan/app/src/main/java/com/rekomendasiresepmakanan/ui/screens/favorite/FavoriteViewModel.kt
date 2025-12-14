package com.rekomendasiresepmakanan.ui.screens.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.local.entity.FavoriteEntity
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    
    // Aliran data daftar favorit dari Room DB
    // Menggunakan stateIn untuk mengubah Flow menjadi StateFlow agar lifecycle-aware di UI
    val favorites: StateFlow<List<FavoriteEntity>> = RecipeRepository.getAllFavorites(application)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
