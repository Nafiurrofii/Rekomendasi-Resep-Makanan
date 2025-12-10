package com.rekomendasiresepmakanan.ui.screen.add_recipe

import android.net.Uri

data class AddRecipeUiState(
    val name: String = "",
    val ingredients: String = "",
    val steps: String = "",
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRecipeAdded: Boolean = false
)
