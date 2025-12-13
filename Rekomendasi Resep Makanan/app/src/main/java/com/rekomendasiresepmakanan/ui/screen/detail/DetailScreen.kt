package com.rekomendasiresepmakanan.ui.screen.detail

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.domain.model.UiState
import com.rekomendasiresepmakanan.ui.component.RecipeImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    recipeId: Int,
    onBackClick: () -> Unit,
    onNavigateToIngredients: (Int) -> Unit = {},
    onNavigateToSteps: (Int) -> Unit = {},
    onNavigateToEdit: (Int) -> Unit = {},
    onDeleteSuccess: () -> Unit = {},
    viewModel: RecipeDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()

    val recipe = (uiState as? UiState.Success)?.data

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Memanggil data resep saat pertama kali dibuka
    LaunchedEffect(recipeId) {
        viewModel.getRecipeDetail(recipeId)
    }

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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = recipe?.title ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1
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
                    // Tombol Edit & Delete dipindah ke TopBar agar lebih accessible daripada Drawer
                    IconButton(onClick = { recipe?.let { onNavigateToEdit(it.id) } }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            // Tombol Favorite Fix di bawah
            Button(
                onClick = { viewModel.toggleFavorite() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFavorite) Color(0xFFE53935) else Color.Black, 
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isFavorite) "Hapus dari Favorit" else "Simpan ke Favorit",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (uiState is UiState.Loading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState is UiState.Error) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = (uiState as UiState.Error).message, color = Color.Red)
                    Button(onClick = { viewModel.getRecipeDetail(recipeId) }) { Text("Coba Lagi") }
                }
            }
        } else {
            recipe?.let { rec ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Gambar Utama
                    RecipeImage(
                        recipeId = rec.id,
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
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify,
                            lineHeight = 24.sp,
                            color = Color.DarkGray
                        )
                    }

                    HorizontalDivider(thickness = 8.dp, color = Color.Gray.copy(alpha = 0.05f))

                    // Bagian Expandable Items (Bahan)
                    DetailListItem(
                        title = "BAHAN",
                        subtitle = "${rec.ingredients.size} bahan",
                        onClick = { onNavigateToIngredients(rec.id) }
                    )

                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.3f))

                    DetailListItem(
                        title = "CARA MASAK",
                        subtitle = "${rec.steps.size} langkah",
                        onClick = { onNavigateToSteps(rec.id) }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
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
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
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
