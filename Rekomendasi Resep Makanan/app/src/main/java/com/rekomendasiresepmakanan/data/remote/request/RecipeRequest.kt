package com.rekomendasiresepmakanan.data.remote.request

data class RecipeRequest(
    val nama_resep: String,
    val category_id: Int,
    val deskripsi: String,
    val bahan: List<String>,
    val langkah_langkah: List<String>,
    val gambar: String? = null
)
