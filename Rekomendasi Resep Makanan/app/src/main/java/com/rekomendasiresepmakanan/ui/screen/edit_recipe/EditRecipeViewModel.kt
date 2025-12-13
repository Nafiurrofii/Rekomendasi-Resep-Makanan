package com.rekomendasiresepmakanan.ui.screen.edit_recipe

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.repository.RecipeRepository
import com.rekomendasiresepmakanan.domain.model.Recipe
import com.rekomendasiresepmakanan.domain.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditRecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<UiState<Recipe>>(UiState.Loading)
    val uiState: StateFlow<UiState<Recipe>> = _uiState.asStateFlow()

    private val _updateState = MutableStateFlow<UiState<Boolean>?>(null)
    val updateState: StateFlow<UiState<Boolean>?> = _updateState.asStateFlow()

    private val _deleteState = MutableStateFlow<UiState<Unit>?>(null)
    val deleteState: StateFlow<UiState<Unit>?> = _deleteState.asStateFlow()

    private val _categories = MutableStateFlow<List<com.rekomendasiresepmakanan.domain.model.Category>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
        viewModelScope.launch {
            _categories.value = RecipeRepository.getCategories()
        }
    }

    var newImageUri: Uri? = null
        private set
        
    var newImageUrlInput by mutableStateOf("")
        private set

    fun loadRecipe(id: Int) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
             val recipe = RecipeRepository.getRecipeById(getApplication<Application>().applicationContext, id)
             if (recipe != null) {
                 _uiState.value = UiState.Success(recipe)
                 // Jika gambar lama adalah URL eksternal, pre-fill input
                 if (recipe.image?.startsWith("http") == true) {
                     newImageUrlInput = recipe.image
                 }
             } else {
                 _uiState.value = UiState.Error("Failed to fetch recipe")
             }
        }
    }

    fun onImagePicked(uri: Uri?) {
        newImageUri = uri
        // Reset URL input if image picked
        if (uri != null) newImageUrlInput = ""
    }
    
    fun onImageUrlChanged(url: String) {
        newImageUrlInput = url
        // Reset URI if URL typed
        if (url.isNotEmpty()) newImageUri = null
    }

    fun updateRecipe(
        id: Int,
        title: String,
        categoryId: Int,
        description: String,
        ingredients: String,
        instructions: String,
        oldImageUrl: String?
    ) {
        _updateState.value = UiState.Loading
        viewModelScope.launch {
            val ingredientsList = ingredients.split("\n").filter { it.isNotBlank() }
            val stepsList = instructions.split("\n").filter { it.isNotBlank() }
            
            // Tentukan gambar final: URI > URL Input > Validasi
            // Repository akan handle: jika URI ada -> upload. Jika URL ada -> kirim string.
            // Jika user tidak ganti gambar (URI null, URL Input kosong), kita kirim oldImageUrl?
            // Repository logic: imageUrl: String?. 
            // Jika kita kirim oldImageUrl sebagai 'imageUrl', backend akan menimpanya.
            // Jika user menghapus input URL (kosong), dan tidak pilih gambar baru -> Harusnya gambar lama tetap ada?
            
            // Logic Sederhana:
            // Kirim newImageUrlInput sebagai imageUrl ke repo.
            // Jika newImageUrlInput kosong, tapi user tidak pilih gambar baru, kita kirim oldImageUrl?
            // Hati-hati: Jika user MENGHAPUS URL (ingin hapus gambar/ganti gambar), bagaimana?
            // Tapi fitur ini opsional. Anggap saja:
            
            val finalImageUrl = if (newImageUrlInput.isNotBlank()) newImageUrlInput else oldImageUrl

            val result = RecipeRepository.updateRecipe(
                context = getApplication<Application>().applicationContext,
                id = id,
                name = title,
                categoryId = categoryId,
                description = description,
                ingredients = ingredientsList,
                steps = stepsList,
                imageUri = newImageUri,
                imageUrl = finalImageUrl
            )

            if (result.isSuccess) {
                _updateState.value = UiState.Success(true)
            } else {
                _updateState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Update failed")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = null
    }
}
