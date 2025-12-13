package com.rekomendasiresepmakanan.ui.screen.add_recipe

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.rekomendasiresepmakanan.domain.model.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    viewModel: AddRecipeViewModel = viewModel(),
    navigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val uploadState = uiState.uploadState

    // Launcher untuk memilih gambar dari galeri
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImageSelected(uri)
    }

    // Observer untuk state upload (loading, success, error)
    LaunchedEffect(uploadState) {
        when (uploadState) {
            is UiState.Success -> {
                Toast.makeText(context, "Recipe added successfully!", Toast.LENGTH_SHORT).show()
                viewModel.resetUploadState() // Reset state
                navigateBack() // Kembali ke layar sebelumnya
            }
            is UiState.Error -> {
                if (uploadState.message.isNotEmpty()) { // Hanya tampilkan toast jika ada pesan error
                    Toast.makeText(context, "Error: ${uploadState.message}", Toast.LENGTH_LONG).show()
                }
                viewModel.resetUploadState() // Reset state agar user bisa mencoba lagi
            }
            is UiState.Loading -> {
                // State ini ditangani oleh loading indicator
            }
            null -> {
                // State idle, tidak melakukan apa-apa
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Recipe") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                // Image Picker
                // Image Input Section
                var isUrlMode by remember { mutableStateOf(false) }

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
                            value = uiState.imageUrl,
                            onValueChange = viewModel::onImageUrlChange,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Image URL") },
                            placeholder = { Text("https://example.com/image.jpg") },
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Preview Image URL
                        if (uiState.imageUrl.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Gray.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = uiState.imageUrl),
                                    contentDescription = "URL Preview",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    } else {
                        // Image Picker Mode
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                                .clickable { imagePickerLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.imageUri == null) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoCamera,
                                        contentDescription = "Upload Image",
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Click to select an image", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            } else {
                                Image(
                                    painter = rememberAsyncImagePainter(model = uiState.imageUri),
                                    contentDescription = "Selected Recipe Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Input Fields
                Text("Recipe Name", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Kategori Dropdown
                var expandedCategory by remember { mutableStateOf(false) }
                val categories by viewModel.categories.collectAsState()
                
                // Helper to find category name by ID (safe call)
                val selectedCategoryName = categories.find { it.id == uiState.categoryId }?.name ?: "Pilih Kategori"

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
                                    viewModel.onCategoryChange(category.id)
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text("Description", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChange,
                    modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 100.dp),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Bahan-bahan
                Text("Ingredients (one per line)", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.ingredients,
                    onValueChange = viewModel::onIngredientsChange,
                    modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 120.dp),
                    placeholder = { Text("Example:\n1 cup flour\n2 eggs, beaten\n1/2 cup sugar") },
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Langkah-langkah
                Text("Steps (one per line)", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.steps,
                    onValueChange = viewModel::onStepsChange,
                    modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 120.dp),
                    placeholder = { Text("Example:\n1. Mix dry ingredients.\n2. Add wet ingredients.\n3. Bake for 30 minutes.") },
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Submit
                Button(
                    onClick = viewModel::addRecipe,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = uploadState !is UiState.Loading // Disable tombol saat sedang loading
                ) {
                    Text("Add Recipe", style = MaterialTheme.typography.titleMedium)
                }
            }

            // Tampilkan loading indicator di tengah layar
            if (uploadState is UiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
