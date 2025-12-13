package com.rekomendasiresepmakanan.ui.screen.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.rekomendasiresepmakanan.ui.component.RecipeImage
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rekomendasiresepmakanan.R
import com.rekomendasiresepmakanan.data.local.entity.FavoriteEntity
import com.rekomendasiresepmakanan.ui.screens.favorite.FavoriteViewModel
import com.rekomendasiresepmakanan.ui.theme.RekomendasiResepMakananTheme

/**
 * FavoriteScreen - Menampilkan daftar resep favorit dalam grid 2 kolom.
 * Menggunakan Room Database untuk menyimpan favorit secara lokal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: FavoriteViewModel = viewModel()
) {
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Resep Favorit",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            FavoriteBottomBar(
                onNavigateToHome = onNavigateToHome,
                onNavigateToSearch = onNavigateToSearch
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        
        if (favorites.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Gray.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "Belum ada resep favorit",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Tambahkan resep favorit dari halaman detail",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Grid dengan 2 kolom
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(favorites) { favorite ->
                    FavoriteCard(
                        favorite = favorite,
                        onClick = { onNavigateToDetail(favorite.recipeId) }
                    )
                }
            }
        }
    }
}

/**
 * Card untuk menampilkan satu item favorit.
 */
@Composable
fun FavoriteCard(
    favorite: FavoriteEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Gambar
            // Gambar
            RecipeImage(
                imageUrl = favorite.image,
                contentDescription = favorite.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            )
            
            // Nama resep
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = favorite.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun FavoriteBottomBar(
    onNavigateToHome: () -> Unit,
    onNavigateToSearch: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.DarkGray
    ) {
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToHome,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToSearch,
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorite") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.DarkGray,
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteScreenPreview() {
    RekomendasiResepMakananTheme {
        FavoriteScreen(
            onNavigateToHome = {},
            onNavigateToSearch = {},
            onNavigateToDetail = {}
        )
    }
}
