package com.rekomendasiresepmakanan.ui.screen.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rekomendasiresepmakanan.R

// Data class sederhana untuk Bahan
data class Ingredient(
    val name: String,
    val imageRes: Int
)

// Dummy data bahan untuk Rendang
// Semua gambar bahan sekarang menggunakan rendang1 (sebagai placeholder dari gambar yang ada)
val dummyIngredients = listOf(
    Ingredient("Daging Sapi", R.drawable.rendang1), 
    Ingredient("Santan", R.drawable.rendang1),
    Ingredient("Cabai Merah", R.drawable.rendang1), 
    Ingredient("Bawang Merah", R.drawable.rendang1),
    Ingredient("Bawang Putih", R.drawable.rendang1),
    Ingredient("Jahe", R.drawable.rendang1),
    Ingredient("Lengkuas", R.drawable.rendang1),
    Ingredient("Serai", R.drawable.rendang1)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    itemId: Int = 1, // Default ID for preview
    onBackClick: () -> Unit = {}
) {
    var isFavorite by remember { mutableStateOf(false) }
    var isIngredientsExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Rendang",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFavorite) Color(0xFFF08080) else Color.Black, 
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (isFavorite) "Hapus favorit" else "Tambah favorit",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Gambar Utama Full Width
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp) // Tinggi disesuaikan
                    .background(Color.LightGray)
            ) {
                // Menggunakan gambar lokal rendang1.jpeg
                Image(
                    painter = painterResource(id = R.drawable.rendang1),
                    contentDescription = "Rendang",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Deskripsi
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Rendang adalah hidangan tradisional khas suku Minangkabau di Sumatera Barat, Indonesia, yang telah mendunia karena rasanya yang kaya rempah dan proses pembuatannya yang unik. Berbeda dengan hidangan lain, rendang sebenarnya adalah nama untuk sebuah proses memasak, yaitu 'marandang', yang berarti mengeringkan atau memasak dalam waktu yang lama. Proses inilah yang menghasilkan tekstur kering dan cita rasa yang sangat khas.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    lineHeight = 22.sp,
                    color = Color.DarkGray
                )
            }

            HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))

            // Bagian Expandable Items (Bahan)
            DetailListItem(
                title = "BAHAN",
                subtitle = "List Bahan bahan",
                isExpanded = isIngredientsExpanded,
                onClick = { isIngredientsExpanded = !isIngredientsExpanded }
            )

            AnimatedVisibility(visible = isIngredientsExpanded) {
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                    dummyIngredients.chunked(4).forEach { rowIngredients ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            rowIngredients.forEach { ingredient ->
                                IngredientItem(ingredient = ingredient, modifier = Modifier.weight(1f))
                            }
                            repeat(4 - rowIngredients.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))

            DetailListItem(
                title = "RECOOK",
                subtitle = "Cara Pembuatan",
                onClick = { /* Handle click */ }
            )
            
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun DetailListItem(
    title: String,
    subtitle: String,
    isExpanded: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.width(80.dp)
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Icon(
            imageVector = if (isExpanded) Icons.Default.ExpandMore else Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun IngredientItem(
    ingredient: Ingredient,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5)), // Background abu muda
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = ingredient.imageRes),
                contentDescription = ingredient.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = ingredient.name,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            lineHeight = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen()
}
