package com.rekomendasiresepmakanan.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.domain.model.UiState
import com.rekomendasiresepmakanan.ui.screens.favorite.FavoriteScreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onNavigateToDetail: (Int) -> Unit,
    homeViewModel: HomeViewModel = viewModel(),
    // favoriteViewModel bisa di-instantiate di dalam jika menggunakan default factory
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItemIndex = remember { mutableIntStateOf(0) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                NavigationDrawerItem(label = { Text("Profil") }, selected = false, onClick = {})
                NavigationDrawerItem(label = { Text("Tambah Resep") }, selected = false, onClick = {})
                NavigationDrawerItem(label = { Text("Resep Tersimpan") }, selected = false, onClick = {})
                NavigationDrawerItem(label = { Text("Tentang Aplikasi") }, selected = false, onClick = {})
            }
        }
    ) {
        Scaffold(
            topBar = {
                // TopBar hanya muncul di Home (index 0) atau bisa disesuaikan
                if (selectedItemIndex.intValue == 0) {
                    HomeTopBar(onMenuClick = { scope.launch { drawerState.open() } })
                }
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = selectedItemIndex.intValue == 0,
                        onClick = { selectedItemIndex.intValue = 0 },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Beranda") },
                        label = { Text("Beranda") }
                    )
                    NavigationBarItem(
                        selected = selectedItemIndex.intValue == 1,
                        onClick = { selectedItemIndex.intValue = 1 },
                        icon = { Icon(Icons.Filled.Search, contentDescription = "Cari") },
                        label = { Text("Cari") }
                    )
                    NavigationBarItem(
                        selected = selectedItemIndex.intValue == 2,
                        onClick = { selectedItemIndex.intValue = 2 },
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorit") },
                        label = { Text("Favorit") }
                    )
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (selectedItemIndex.intValue) {
                    0 -> HomeContent(
                        onNavigateToDetail = onNavigateToDetail,
                        viewModel = homeViewModel
                    )
                    1 -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Fitur Pencarian Segera Hadir")
                    }
                    2 -> FavoriteScreen(
                        onNavigateToDetail = onNavigateToDetail
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    onNavigateToDetail: (Int) -> Unit,
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    
    // Refresh handler - simple implementation
    // For proper PullRefresh, need M3 PullToRefreshBox (experimental in newer libs) or Accompanist
    // Here we use a simple FAB/Button for refresh if error/offline
    
    Column(modifier = Modifier.fillMaxSize()) {
        
        // Offline Indicator
        AnimatedVisibility(visible = !isOnline) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(8.dp),
                   horizontalArrangement = Arrangement.Center,
                   verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.WifiOff, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mode Offline - Menampilkan data cache", style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        when (val state = uiState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = state.message, color = Color.Red)
                        Button(onClick = { viewModel.retry() }) {
                            Text("Coba Lagi")
                        }
                    }
                }
            }
            is UiState.Success -> {
                val recipes = state.data
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item { Spacer(Modifier.height(16.dp)) }
                    item { BannerSection() }
                    item { CategorySection() }
                    item { SectionHeader(title = "Semua Resep") }
                    
                    if (recipes.isEmpty()) {
                        item {
                            Text("Tidak ada resep", modifier = Modifier.padding(16.dp))
                        }
                    } else {
                        val chunkedRecipes = recipes.chunked(2)
                        items(chunkedRecipes) { rowRecipes ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                for (recipe in rowRecipes) {
                                    RecipeCardItem(
                                        id = recipe.id,
                                        title = recipe.title,
                                        imageUrl = recipe.image,
                                        origin = recipe.category?.name ?: "Umum",
                                        onClick = { onNavigateToDetail(recipe.id) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowRecipes.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                    item { Spacer(Modifier.height(16.dp)) }
                }
            }
        }
    }
}

private fun HomeViewModel.retry() {
    TODO("Not yet implemented")
}

// ... Rest of components (RecipeCardItem, HomeTopBar, etc) same as before ...
// For brevity, I will copy only necessary parts or rely on you to assume they are there.
// But to be safe (since overwrite), I must include everything.

@Composable
fun RecipeCardItem(
    id: Int,
    title: String,
    imageUrl: String?,
    origin: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.width(160.dp)
) {
    Card(
        modifier = modifier
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            com.rekomendasiresepmakanan.ui.component.RecipeImage(
                recipeId = id,
                imageUrl = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = origin, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(text = title, style = MaterialTheme.typography.titleMedium, maxLines = 2)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = { Text("Resep Makanan", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notifikasi")
            }
        }
    )
}

@Composable
fun BannerSection() {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = ColorPainter(Color.DarkGray), // Placeholder
                contentDescription = "Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text("Inspirasi Masak Harian", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CategorySection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader(title = "Kategori")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryItem(name = "Nusantara", color = Color(0xFFFFE0B2))
            CategoryItem(name = "Chinese", color = Color(0xFFC8E6C9))
            CategoryItem(name = "Western", color = Color(0xFFFFCCBC))
            CategoryItem(name = "Vegan", color = Color(0xFFB2DFDB))
        }
    }
}

@Composable
fun CategoryItem(name: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.RestaurantMenu, contentDescription = name, tint = Color.Black.copy(alpha = 0.7f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Icon(Icons.Filled.ChevronRight, contentDescription = "Lihat semua", tint = Color.Gray)
    }
}
