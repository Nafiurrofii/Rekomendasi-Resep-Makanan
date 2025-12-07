package com.rekomendasiresepmakanan.ui.screen.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rekomendasiresepmakanan.ui.theme.RekomendasiResepMakananTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = "Tentang App",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Nama Aplikasi
            Text(
                text = "Rekomendasi Resep Makanan",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center // Ditambahkan untuk menengahkan teks
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Versi Aplikasi
            Text(
                text = "Versi Aplikasi: 1.0.0",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Deskripsi Aplikasi
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Aplikasi Resep Nusantara adalah aplikasi yang menyediakan berbagai macam resep masakan, mulai dari hidangan tradisional Indonesia hingga masakan internasional.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Fitur utama:",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Pencarian resep\n" +
                           "• Kategori resep\n" +
                           "• Menyimpan resep favorit\n" +
                           "• Daftar bahan & langkah memasak",
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Aplikasi ini dibuat menggunakan Jetpack Compose dan arsitektur MVVM modern.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            // Spacer untuk mendorong "Dibuat oleh" ke bawah jika konten sedikit
            Spacer(modifier = Modifier.weight(1f))
            
            // Informasi Pembuat
            Text(
                text = "Dibuat oleh: Kelompok ..",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    RekomendasiResepMakananTheme {
        AboutScreen(onBackClick = {})
    }
}
