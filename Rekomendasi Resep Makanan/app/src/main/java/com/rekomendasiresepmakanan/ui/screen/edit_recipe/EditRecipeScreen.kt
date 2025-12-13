package com.rekomendasiresepmakanan.ui.screen.edit_recipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rekomendasiresepmakanan.domain.model.UiState
import com.rekomendasiresepmakanan.domain.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(
    recipeId: Int,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: EditRecipeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val updateState by viewModel.updateState.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()

    // Form States
    var title by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf(1) }
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success<*>) {
            val recipe = (uiState as UiState.Success<Recipe>).data
            title = recipe.title
            categoryId = recipe.categoryId
            description = recipe.description
            ingredients = recipe.ingredients.joinToString("\n")
            instructions = recipe.steps.joinToString("\n")
            imageUrl = recipe.image
        }
    }

    LaunchedEffect(updateState) {
        if (updateState is UiState.Success<*>) {
            onNavigateBack()
            viewModel.resetUpdateState()
        }
    }

    LaunchedEffect(deleteState) {
        if (deleteState is UiState.Success<*>) {
            onNavigateHome()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Resep") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Error -> {
                    Text(
                        text = (uiState as UiState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Success<*> -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Nama Resep") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        var expandedCategory by remember { mutableStateOf(false) }
                        val categories by viewModel.categories.collectAsState()
                        
                        val selectedCategoryName = categories.find { it.id == categoryId }?.name ?: "Pilih Kategori"

                        ExposedDropdownMenuBox(
                            expanded = expandedCategory,
                            onExpandedChange = { expandedCategory = !expandedCategory },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedCategoryName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Kategori") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                                modifier = Modifier.fillMaxWidth().menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedCategory,
                                onDismissRequest = { expandedCategory = false }
                            ) {
                                categories.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category.name) },
                                        onClick = {
                                            categoryId = category.id
                                            expandedCategory = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Deskripsi Singkat") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        OutlinedTextField(
                            value = ingredients,
                            onValueChange = { ingredients = it },
                            label = { Text("Bahan-bahan (pisahkan baris)") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 5
                        )

                        OutlinedTextField(
                            value = instructions,
                            onValueChange = { instructions = it },
                            label = { Text("Langkah-langkah (pisahkan baris)") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 5
                        )
                        
                        val imagePickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                            contract = androidx.activity.result.contract.ActivityResultContracts.GetContent(),
                            onResult = { uri ->
                                viewModel.onImagePicked(uri)
                            }
                        )

                        // Image Logic
                        var isUrlMode by remember { mutableStateOf(viewModel.newImageUrlInput.isNotEmpty()) }

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Gambar Resep", fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(onClick = { isUrlMode = !isUrlMode }) {
                                    Text(if (isUrlMode) "Switch to Upload" else "Switch to Link")
                                }
                            }

                            if (isUrlMode) {
                                OutlinedTextField(
                                    value = viewModel.newImageUrlInput,
                                    onValueChange = viewModel::onImageUrlChanged,
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text("Image URL") },
                                    placeholder = { Text("https://example.com/image.jpg") },
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                // Preview Link/Old URL
                                val previewUrl = if (viewModel.newImageUrlInput.isNotBlank()) viewModel.newImageUrlInput else imageUrl
                                if (!previewUrl.isNullOrBlank()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.Gray.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        androidx.compose.foundation.Image(
                                            painter = coil.compose.rememberAsyncImagePainter(model = previewUrl),
                                            contentDescription = "URL Preview",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                        )
                                    }
                                }
                            } else {
                                // Image Picker Mode
                                val displayUri = viewModel.newImageUri ?: (if (imageUrl?.startsWith("http") == false) imageUrl else null)
                                
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable { imagePickerLauncher.launch("image/*") },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (displayUri == null && viewModel.newImageUri == null && imageUrl.isNullOrBlank()) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(Icons.Default.Link, contentDescription = null) // Fallback icon
                                            Text("Select Image")
                                        }
                                    } else {
                                        // Prioritize New URI -> Old Image URL
                                        val model = viewModel.newImageUri ?: imageUrl
                                        androidx.compose.foundation.Image(
                                            painter = coil.compose.rememberAsyncImagePainter(model = model),
                                            contentDescription = "Preview",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.updateRecipe(
                                    recipeId, title, categoryId, description, ingredients, instructions, imageUrl
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = updateState !is UiState.Loading
                        ) {
                            if (updateState is UiState.Loading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Update Resep")
                            }
                        }
                        
                        if (updateState is UiState.Error) {
                            Text(
                                text = (updateState as UiState.Error).message,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
