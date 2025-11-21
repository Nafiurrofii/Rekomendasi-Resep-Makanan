package com.rekomendasiresepmakanan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rekomendasiresepmakanan.ui.HomeScreen
import com.rekomendasiresepmakanan.ui.theme.RekomendasiResepMakananTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RekomendasiResepMakananTheme {
                // Memanggil View Utama (HomeScreen)
                HomeScreen()
            }
        }
    }
}
