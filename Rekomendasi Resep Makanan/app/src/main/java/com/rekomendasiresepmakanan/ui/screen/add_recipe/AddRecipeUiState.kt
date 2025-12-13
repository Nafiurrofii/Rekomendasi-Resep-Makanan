package com.rekomendasiresepmakanan.ui.screen.add_recipe

import android.net.Uri
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.domain.model.UiState

data class AddRecipeUiState(
    val name: String = "",
    val categoryId: Int = 1, // Default ke Nusantara
    val description: String = "",
    val ingredients: String = "",
    val steps: String = "",
    val imageUri: Uri? = null,
    val imageUrl: String = "",
    val uploadState: UiState<Recipe>? = null
)
