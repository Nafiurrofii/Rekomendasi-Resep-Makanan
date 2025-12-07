package com.rekomendasiresepmakanan.ui.screen.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
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

@Composable
fun FavoriteScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: FavoriteViewModel = viewModel()
) {
    val favoriteRecipes by viewModel.favoriteRecipes.collectAsState()

    Scaffold(
        topBar = {
            Text(
                text = "Masakan Favorit",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
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
        if (favoriteRecipes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada masakan favorit")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(paddingValues)
            ) {
                items(favoriteRecipes) { recipe ->
                    Box(modifier = Modifier.clickable { onNavigateToDetail(recipe.id) }) {
                        RecipeGridItem(recipe = recipe)
                    }
                }
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
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorite") }, // Icon hati penuh
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
