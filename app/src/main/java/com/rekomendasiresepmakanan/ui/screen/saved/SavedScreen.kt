package com.rekomendasiresepmakanan.ui.screen.saved

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rekomendasiresepmakanan.ui.component.RecipeGridItem
import com.rekomendasiresepmakanan.ui.theme.RekomendasiResepMakananTheme
import com.rekomendasiresepmakanan.viewmodel.FavoriteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    onNavigateToDetail: (Int) -> Unit,
    onBackClick: () -> Unit, // Ditambahkan
    viewModel: FavoriteViewModel = viewModel()
) {
    val savedRecipes by viewModel.favoriteRecipes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Resep Tersimpan",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (savedRecipes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada resep yang disimpan")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(paddingValues)
            ) {
                items(savedRecipes) { recipe ->
                    Box(modifier = Modifier.clickable { onNavigateToDetail(recipe.id) }) {
                        RecipeGridItem(recipe = recipe)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SavedScreenPreview() {
    RekomendasiResepMakananTheme {
        SavedScreen(
            onNavigateToDetail = {},
            onBackClick = {}
        )
    }
}
