package com.rekomendasiresepmakanan.ui.component

import android.graphics.BitmapFactory
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.rekomendasiresepmakanan.data.repository.RecipeRepository

@Composable
fun RecipeImage(
    recipeId: Int = 0,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    shape: Shape = RoundedCornerShape(12.dp),
    contentScale: ContentScale = ContentScale.Crop
) {
    // 1. Logic Room / Local
    if (recipeId != 0) {
        val context = LocalContext.current
        val repository = RecipeRepository
        
        // Observe Blob
        val imageBlob by repository.getRecipeImageFlow(context, recipeId).collectAsState(initial = null)
        
        // Trigger Download
        LaunchedEffect(recipeId, imageUrl) {
            if (imageBlob == null && !imageUrl.isNullOrBlank()) {
                 repository.downloadAndSaveImage(context, recipeId, imageUrl)
            }
        }
        
        // Render
        if (imageBlob != null) {
            // Decode Bitmap
             val bitmap by produceState<ImageBitmap?>(initialValue = null, imageBlob) {
                value = withContext(Dispatchers.Default) {
                    try {
                        BitmapFactory.decodeByteArray(imageBlob!!, 0, imageBlob!!.size)?.asImageBitmap()
                    } catch (e: Exception) { null }
                }
            }
            
            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = modifier.clip(shape)
                )
            } else {
                // Failed decode -> Fallback to Network
                NetworkImage(imageUrl, modifier, shape, contentDescription, contentScale)
            }
        } else {
             // Blob belum ada -> Fallback to Network (Load URL while downloading in bg)
             NetworkImage(imageUrl, modifier, shape, contentDescription, contentScale)
        }

    } else {
        // 2. Logic Network Only (Coil)
        NetworkImage(imageUrl, modifier, shape, contentDescription, contentScale)
    }
}

/**
 * Reusable Network Image dengan Coil + Error Handling yang Jelas
 */
@Composable
fun NetworkImage(
    imageUrl: String?,
    modifier: Modifier,
    shape: Shape,
    contentDescription: String?,
    contentScale: ContentScale
) {
    if (imageUrl.isNullOrEmpty()) {
        PlaceholderContainer(modifier, shape)
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
                ErrorContainer(modifier = Modifier.matchParentSize(), shape = shape)
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
            .background(Color.LightGray.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(0.4f), 
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
    }
}

@Composable
fun ErrorContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(Color.LightGray.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.BrokenImage,
            contentDescription = "Error loading image",
            tint = Color.Gray,
            modifier = Modifier.fillMaxSize(0.4f)
        )
    }
}

@Composable
fun PlaceholderContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(Color.LightGray.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Image,
            contentDescription = "No Image",
            tint = Color.Gray,
            modifier = Modifier.fillMaxSize(0.4f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeImageGridExamplePreview() {
    val dummyImages = listOf(
        "https://via.placeholder.com/150", 
        "", 
        "invalid-url", 
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
                    .aspectRatio(1f)
            ) {
               RecipeImage(
                   imageUrl = dummyImages[index],
                   modifier = Modifier.fillMaxSize()
               )
            }
        }
    }
}
