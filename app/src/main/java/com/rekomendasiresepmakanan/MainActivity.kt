package com.rekomendasiresepmakanan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.rekomendasiresepmakanan.navigation.AppNavHost
import com.rekomendasiresepmakanan.ui.theme.RekomendasiResepMakananTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RekomendasiResepMakananTheme {
                val navController = rememberNavController()
                // Menggunakan AppNavHost yang baru (yang support Home & Search)
                AppNavHost(navController = navController)
            }
        }
    }
}
