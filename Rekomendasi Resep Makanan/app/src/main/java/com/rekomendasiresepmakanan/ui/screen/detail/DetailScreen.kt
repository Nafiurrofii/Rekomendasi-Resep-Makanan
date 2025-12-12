package com.rekomendasiresepmakanan.ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.RectangleShape
import com.rekomendasiresepmakanan.ui.component.RecipeImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.rekomendasiresepmakanan.R
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.domain.model.Category
import com.rekomendasiresepmakanan.ui.theme.RekomendasiResepMakananTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    onNavigateToIngredients: (Int) -> Unit = {},
    onNavigateToSteps: (Int) -> Unit = {},
    onNavigateToEdit: (Int) -> Unit = {}, // New callback
    onDeleteSuccess: () -> Unit = {}, // New callback
    viewModel: RecipeDetailViewModel = viewModel()
) {
    val recipe by viewModel.recipeDetail.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Handle delete result
    LaunchedEffect(deleteState) {
        if (deleteState == true) {
            onDeleteSuccess()
            viewModel.resetDeleteState()
        }
    }

    if (showDeleteDialog) {
        com.rekomendasiresepmakanan.ui.component.DeleteConfirmationDialog(
            onConfirm = {
                viewModel.deleteRecipe()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                NavigationDrawerItem(
                    label = { Text("Edit Resep") },
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        recipe?.let { onNavigateToEdit(it.id) }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Hapus Resep") },
                    icon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        showDeleteDialog = true
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedTextColor = Color.Red,
                        unselectedIconColor = Color.Red
                    )
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = recipe?.title ?: "Loading...",
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
                    actions = {
                        // Menu button to open drawer
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            bottomBar = {
                Button(
                    onClick = viewModel::toggleFavorite,
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
            recipe?.let { rec ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Gambar Utama Full Width
                    // Support for URL from database
                     RecipeImage(
                        imageUrl = rec.image,
                        contentDescription = rec.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        shape = RectangleShape
                     )

                    // Deskripsi
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = rec.description,
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
                        onClick = { onNavigateToIngredients(rec.id) }
                    )

                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))

                    DetailListItem(
                        title = "RECOOK",
                        subtitle = "Cara Pembuatan",
                        onClick = { onNavigateToSteps(rec.id) }
                    )
                    
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun DetailListItem(
    title: String,
    subtitle: String,
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
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    RekomendasiResepMakananTheme {
        val recipe = Recipe(
            id = 1, 
            title = "Rendang", 
            categoryId = 1, 
            category = Category(1, "Nusantara"), 
            image = "https://via.placeholder.com/150", 
            description = "Deskripsi contoh", 
            ingredients = listOf("Bahan 1", "Bahan 2"), 
            steps = listOf("Langkah 1", "Langkah 2")
        )
        val isFavorite = false
        Scaffold(
            // Perbaikan: topAppBarColors() di sini juga
            topBar = { 
                CenterAlignedTopAppBar(
                    title = { Text(recipe.title) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                ) 
            },
            bottomBar = { Button(onClick = {}) { Text(if (isFavorite) "Hapus" else "Tambah") } }
        ) {
            Column(Modifier.padding(it)) {
                Text(recipe.description)
            }
        }
    }
}