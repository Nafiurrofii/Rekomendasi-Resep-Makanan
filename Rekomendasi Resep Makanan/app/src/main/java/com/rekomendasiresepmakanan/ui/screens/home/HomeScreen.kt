package com.rekomendasiresepmakanan.ui.screens.home

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
import coil.compose.AsyncImage // Pastikan import ini ada untuk memuat gambar
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onNavigateToDetail: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    // Observe UI State
    val uiState by viewModel.uiState.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItemIndex = remember { mutableStateOf(0) }

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
                HomeTopBar(onMenuClick = { scope.launch { drawerState.open() } })
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = selectedItemIndex.value == 0,
                        onClick = { selectedItemIndex.value = 0 },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Beranda") },
                        label = { Text("Beranda") }
                    )
                    NavigationBarItem(
                        selected = selectedItemIndex.value == 1,
                        onClick = { selectedItemIndex.value = 1 },
                        icon = { Icon(Icons.Filled.Search, contentDescription = "Cari") },
                        label = { Text("Cari") }
                    )
                    NavigationBarItem(
                        selected = selectedItemIndex.value == 2,
                        onClick = { selectedItemIndex.value = 2 },
                        icon = { Icon(Icons.Filled.FavoriteBorder, contentDescription = "Favorit") },
                        label = { Text("Favorit") }
                    )
                }
            }
        ) { paddingValues ->

            // Handle berbagai UI State
            when (val state = uiState) {
                is com.rekomendasiresepmakanan.domain.model.UiState.Loading -> {
                    // Loading State - Loading indicator di tengah
                    Box( modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator()
                            Text(
                                "Memuat resep...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }

                is com.rekomendasiresepmakanan.domain.model.UiState.Error -> {
                    // Error State - Pesan error + tombol retry
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Red.copy(alpha = 0.7f)
                            )
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.DarkGray,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Button(
                                onClick = { viewModel.retry() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black
                                )
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Coba Lagi")
                            }
                        }
                    }
                }

                is com.rekomendasiresepmakanan.domain.model.UiState.Success -> {
                    // Success State - Tampilkan data
                    val recipes = state.data
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        item { BannerSection() }
                        item { CategorySection() }
                        item { SectionHeader(title = "Resep Populer") }

                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(recipes) { recipe ->
                                    RecipeCardItem(
                                        title = recipe.title,
                                        imageUrl = recipe.image,  // Gunakan URL dari database
                                        origin = recipe.category?.name ?: "Lainnya",
                                        onClick = { onNavigateToDetail(recipe.id) }
                                    )
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }
            }
        }
    }
}

// Komponen Kartu Resep Sederhana (dibuat lokal di sini agar tidak error import)
@Composable
fun RecipeCardItem(
    title: String,
    imageUrl: String?,  // Changed from imageRes to imageUrl
    origin: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // Load image dari database URL menggunakan AsyncImage
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(Color.Gray.copy(alpha = 0.3f)),
                error = ColorPainter(Color.Gray.copy(alpha = 0.5f))
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
        title = {},
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
            .height(200.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Placeholder Banner
            androidx.compose.foundation.Image(
                painter = ColorPainter(Color.DarkGray),
                contentDescription = "Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text("Masakan Nusantara", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CategorySection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader(title = "Kategori Resep")
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
