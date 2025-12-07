package com.rekomendasiresepmakanan.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Recipe(
    val id: Int,
    val title: String,
    val category: String,
    val image: Int, // Using Int for drawable resource based on previous context
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>
)
