package com.rekomendasiresepmakanan.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Category(
    val id: Int,
    val name: String,
    val image: String // URL to the image
)
