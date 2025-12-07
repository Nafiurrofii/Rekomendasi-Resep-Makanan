package com.rekomendasiresepmakanan.ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rekomendasiresepmakanan.ui.component.CustomSearchBar
import com.rekomendasiresepmakanan.ui.component.RecipeGridItem
import com.rekomendasiresepmakanan.ui.theme.RekomendasiResepMakananTheme

@Composable
fun SearchScreen(
    onNavigateHome: () -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    val query by viewModel.searchQuery.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    Scaffold(
        bottomBar = {
            SearchBottomBar(onNavigateHome = onNavigateHome)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Search Bar with fixed position logic
                Box(modifier = Modifier.zIndex(1f)) {
                    Column {
                        CustomSearchBar(
                            query = query,
                            onQueryChange = viewModel::onSearchChange,
                            onClear = viewModel::clearSearch
                        )
                        
                        // Suggestion Dropdown
                        AnimatedVisibility(visible = suggestions.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp)
                                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE6E6FA) // Ungu pastel
                                )
                            ) {
                                Column {
                                    suggestions.forEach { suggestion ->
                                        Text(
                                            text = suggestion,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { viewModel.selectSuggestion(suggestion) }
                                                .padding(16.dp),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Divider(color = Color.White.copy(alpha = 0.5f))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Results Grid
                if (searchResults.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(searchResults) { recipe ->
                            RecipeGridItem(recipe = recipe)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBottomBar(onNavigateHome: () -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black // Make sure content is Black
    ) {
        NavigationBarItem(
            selected = false,
            onClick = onNavigateHome,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            // Removed label
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.DarkGray, // Darker gray for unselected
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = true,
            onClick = {}, // Already on search
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            // Removed label
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.DarkGray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Saved") },
            // Removed label
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
fun SearchScreenPreview() {
    RekomendasiResepMakananTheme {
        SearchScreen(onNavigateHome = {})
    }
}
