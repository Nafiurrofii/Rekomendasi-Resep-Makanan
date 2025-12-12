package com.rekomendasiresepmakanan.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

/**
 * Komponen untuk menampilkan gambar resep.
 * Metadata:
 * - Jika imageUrl null/kosong -> Loading (CircularProgressIndicator)
 * - Jika loading -> Loading
 * - Jika error -> Loading
 * - Style: Container abu-abu muda, Rounded 12dp, Loading di tengah.
 */
@Composable
fun RecipeImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    shape: Shape = RoundedCornerShape(12.dp), // Rounded 12dp default
    contentScale: ContentScale = ContentScale.Crop
) {
    if (imageUrl.isNullOrEmpty()) {
        LoadingContainer(modifier = modifier, shape = shape)
    } else {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            modifier = modifier.clip(shape),
            contentScale = contentScale,
            loading = {
                LoadingContainer(modifier = Modifier.matchParentSize(), shape = shape)
            },
            error = {
                // Jika gagal load, tetap tampilkan loading (sesuai request)
                LoadingContainer(modifier = Modifier.matchParentSize(), shape = shape)
            }
        )
    }
}

@Composable
fun LoadingContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(Color.LightGray.copy(alpha = 0.3f)), // Background abu-abu muda
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(0.3f), // Ukuran indicator proporsional
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
    }
}

// --- Contoh Penggunaan (Preview & Example) ---

@Preview(showBackground = true)
@Composable
fun RecipeImageGridExamplePreview() {
    // Dummy Data
    val dummyImages = listOf(
        "https://via.placeholder.com/150", 
        "", // Kosong -> Loading
        "invalid-url", // Error -> Loading
        null, // Null -> Loading
        "https://via.placeholder.com/150/0000FF"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(dummyImages.size) { index ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .aspectRatio(1f) // Aspect ratio kotak
            ) {
               RecipeImage(
                   imageUrl = dummyImages[index],
                   modifier = Modifier.fillMaxSize()
               )
            }
        }
    }
}
