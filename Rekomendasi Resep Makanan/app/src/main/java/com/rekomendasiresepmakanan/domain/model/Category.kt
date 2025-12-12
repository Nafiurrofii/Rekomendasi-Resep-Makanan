package com.rekomendasiresepmakanan.domain.model

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class Category(
    val id: Int,
    @SerializedName("nama_kategori")
    val name: String
)
