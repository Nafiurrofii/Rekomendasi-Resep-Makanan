package com.rekomendasiresepmakanan.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rekomendasiresepmakanan.data.local.dao.FavoriteDao
import com.rekomendasiresepmakanan.data.local.entity.FavoriteEntity

/**
 * Room Database untuk aplikasi.
 * Version 1: Tabel favorites untuk menyimpan resep favorit.
 */
@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun favoriteDao(): FavoriteDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        /**
         * Singleton pattern untuk mencegah multiple instance database.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rekomendasi_resep_database"
                )
                    .fallbackToDestructiveMigration() // Hapus data lama jika schema berubah
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
