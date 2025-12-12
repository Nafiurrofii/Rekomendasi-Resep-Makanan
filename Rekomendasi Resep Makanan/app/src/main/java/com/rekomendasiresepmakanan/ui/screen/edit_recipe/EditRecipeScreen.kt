package com.rekomendasiresepmakanan.ui.screen.edit_recipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rekomendasiresepmakanan.domain.model.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(
    recipeId: Int,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit, // For delete success
    viewModel: EditRecipeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val updateState by viewModel.updateState.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()

    // Form States
    var title by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf(1) } // Default Nusantara
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    // Init data load
    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    // Prefill form when data loaded
    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            val recipe = (uiState as UiState.Success).data
            title = recipe.title
            categoryId = recipe.categoryId
            description = recipe.description
            ingredients = recipe.ingredients.joinToString("\n")
            instructions = recipe.steps.joinToString("\n")
            imageUrl = recipe.image
        }
    }

    // Handle update success
    LaunchedEffect(updateState) {
        if (updateState is UiState.Success) {
            onNavigateBack()
            viewModel.resetUpdateState()
        }
    }

    // Handle delete success
    LaunchedEffect(deleteState) {
        if (deleteState is UiState.Success) {
            onNavigateHome()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Resep") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
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
                is UiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Title
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Nama Resep") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Kategori
                        var expandedCategory by remember { mutableStateOf(false) }
                        val categories = listOf("Nusantara", "Chinese", "Western", "Dessert", "Lainnya")
                        
                        fun getCategoryName(id: Int): String {
                            return when(id) {
                                1 -> "Nusantara"
                                2 -> "Chinese"
                                3 -> "Western"
                                4 -> "Dessert"
                                else -> "Lainnya"
                            }
                        }

                        fun getCategoryIdByName(name: String): Int {
                            return when(name) {
                                "Nusantara" -> 1
                                "Chinese" -> 2
                                "Western" -> 3
                                "Dessert" -> 4
                                else -> 5
                            }
                        }

                        ExposedDropdownMenuBox(
                            expanded = expandedCategory,
                            onExpandedChange = { expandedCategory = !expandedCategory },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = getCategoryName(categoryId),
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
                                categories.forEach { categoryName ->
                                    DropdownMenuItem(
                                        text = { Text(categoryName) },
                                        onClick = {
                                            categoryId = getCategoryIdByName(categoryName)
                                            expandedCategory = false
                                        }
                                    )
                                }
                            }
                        }

                        // Description
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Deskripsi Singkat") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        // Ingredients
                        OutlinedTextField(
                            value = ingredients,
                            onValueChange = { ingredients = it },
                            label = { Text("Bahan-bahan (pisahkan baris)") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 5
                        )

                        // Instructions
                        OutlinedTextField(
                            value = instructions,
                            onValueChange = { instructions = it },
                            label = { Text("Langkah-langkah (pisahkan baris)") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 5
                        )
                        
                        // Gambar (Image Picker Style)
                        val imagePickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                            contract = androidx.activity.result.contract.ActivityResultContracts.GetContent(),
                            onResult = { uri ->
                                viewModel.onImagePicked(uri) // Update ViewModel
                                // Update local preview state if needed, but ViewModel holds the truth
                            }
                        )

                        // Logic display value: New URI filename > Old URL > Empty
                        val displayImageValue = viewModel.newImageUri?.lastPathSegment ?: imageUrl
                        
                        OutlinedTextField(
                            value = displayImageValue,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Gambar") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { imagePickerLauncher.launch("image/*") },
                            placeholder = { Text("Pilih Gambar") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Link,
                                    contentDescription = "Upload Gambar",
                                    modifier = Modifier.clickable { imagePickerLauncher.launch("image/*") }
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Update Button
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
                        
                        // Error message from update
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
