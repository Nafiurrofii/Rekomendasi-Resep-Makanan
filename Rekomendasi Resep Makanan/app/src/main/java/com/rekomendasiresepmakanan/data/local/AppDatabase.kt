package com.rekomendasiresepmakanan.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rekomendasiresepmakanan.data.local.dao.FavoriteDao
import com.rekomendasiresepmakanan.data.local.dao.RecipeDao
import com.rekomendasiresepmakanan.data.local.dao.RecipeImageDao
import com.rekomendasiresepmakanan.data.local.entity.RecipeImageEntity
import com.rekomendasiresepmakanan.data.local.entity.RecipeEntity
import com.rekomendasiresepmakanan.data.local.entity.FavoriteEntity

@Database(
    entities = [RecipeEntity::class, FavoriteEntity::class, RecipeImageEntity::class],
    version = 3, 
    exportSchema = false
)
@androidx.room.TypeConverters(com.rekomendasiresepmakanan.data.local.entity.Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun recipeDao(): RecipeDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun recipeImageDao(): RecipeImageDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipe_database"
                )
                .fallbackToDestructiveMigration() // Drop & recreate on version change
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}
