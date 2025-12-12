package com.rekomendasiresepmakanan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity Room untuk menyimpan data resep favorit secara lokal (offline).
 * Table name: favorites
 */
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val recipeId: Int,        // ID resep dari backend/repository
    val name: String,         // Nama resep
    val image: String,        // URL gambar (bisa kosong)
    val imageRes: Int? = null // Resource ID gambar lokal (nullable)
)
