package com.rekomendasiresepmakanan.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.ui.graphics.vector.ImageVector

// --- MODEL ---
data class Recipe(
    val id: Int,
    val name: String,
    val description: String,
    val icon: ImageVector = Icons.Filled.Fastfood 
)

// --- DATA SOURCE (Simulasi Database) ---
object RecipeRepository {
    fun getRecipes(): List<Recipe> {
        return listOf(
            Recipe(1, "Nasi Goreng Spesial", "Nasi goreng lezat dengan telur dan ayam suwir.", Icons.Filled.Restaurant),
            Recipe(2, "Sate Ayam Madura", "Sate ayam dengan bumbu kacang yang kental dan manis.", Icons.Filled.Fastfood),
            Recipe(3, "Pizza Rumahan", "Pizza dengan topping sosis dan keju mozzarella leleh.", Icons.Filled.LocalPizza),
            Recipe(4, "Soto Ayam Lamongan", "Soto ayam dengan kuah kuning yang segar dan koya.", Icons.Filled.SoupKitchen),
            Recipe(5, "Rendang Daging", "Daging sapi yang dimasak lama dengan rempah-rempah khas Padang.", Icons.Filled.Restaurant)
        )
    }
}
