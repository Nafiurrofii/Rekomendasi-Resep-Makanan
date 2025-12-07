package com.rekomendasiresepmakanan.ui.screen.category

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rekomendasiresepmakanan.ui.component.RecipeGridItem
import com.rekomendasiresepmakanan.ui.theme.RekomendasiResepMakananTheme
import com.rekomendasiresepmakanan.viewmodel.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    categoryName: String,
    onBackClick: () -> Unit,
    onRecipeClick: (Int) -> Unit,
    viewModel: CategoryViewModel = viewModel()
) {
    val recipes by viewModel.getRecipeByCategory(categoryName).collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = categoryName,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (recipes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Text(
                    text = "Belum ada resep untuk kategori ini",
                    modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(recipes) { recipe ->
                    Box(modifier = Modifier.clickable { onRecipeClick(recipe.id) }) {
                        RecipeGridItem(recipe = recipe)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryDetailScreenPreview() {
    RekomendasiResepMakananTheme {
        CategoryDetailScreen(
            categoryName = "Nusantara",
            onBackClick = {},
            onRecipeClick = {}
        )
    }
}
