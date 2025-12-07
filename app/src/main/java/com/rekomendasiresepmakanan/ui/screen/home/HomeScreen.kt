package com.rekomendasiresepmakanan.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rekomendasiresepmakanan.R
import com.rekomendasiresepmakanan.ui.component.CategoryItem
import com.rekomendasiresepmakanan.ui.component.RecipeCard
import com.rekomendasiresepmakanan.ui.theme.RekomendasiResepMakananTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToCategories: () -> Unit, 
    onNavigateToCategoryDetail: (String) -> Unit,
    onNavigateToFavorite: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val popularRecipes by viewModel.popularRecipes.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFFE6E6FA)
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    "Menu",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Tambah Resep") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) }
                )
                // Item "Resep Tersimpan" dihapus
                NavigationDrawerItem(
                    label = { Text("Tentang App") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Info, contentDescription = null) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(onMenuClick = {
                    scope.launch { drawerState.open() }
                })
            },
            bottomBar = {
                HomeBottomBar(
                    onNavigateToSearch = onNavigateToSearch,
                    onNavigateToFavorite = onNavigateToFavorite
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Banner Section
                BannerSection()

                Spacer(modifier = Modifier.height(24.dp))

                // Category Section
                SectionHeader(title = "Kategori Resep", onSeeAllClick = onNavigateToCategories)
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onClick = { onNavigateToCategoryDetail(category.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Popular Recipes Section
                SectionHeader(title = "Resep Populer")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(popularRecipes) { recipe ->
                        Box(modifier = Modifier.clickable { onNavigateToDetail(recipe.id) }) {
                             RecipeCard(recipe = recipe)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    )
}

@Composable
fun BannerSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray) 
    ) {
        Image(
            painter = painterResource(id = R.drawable.rendang1),
            contentDescription = "Banner Masakan Nusantara",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
        )
        
        Text(
            text = "Masakan Nusantara",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "See all",
            modifier = Modifier.clickable(onClick = onSeeAllClick)
        )
    }
}

@Composable
fun HomeBottomBar(
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorite: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.DarkGray
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.DarkGray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToSearch,
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.DarkGray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToFavorite,
            icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Saved") },
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
fun HomeScreenPreview() {
    RekomendasiResepMakananTheme {
        HomeScreen(
            onNavigateToSearch = {},
            onNavigateToDetail = {},
            onNavigateToCategories = {},
            onNavigateToCategoryDetail = {},
            onNavigateToFavorite = {}
        )
    }
}
