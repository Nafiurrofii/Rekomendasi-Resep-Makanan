package com.rekomendasiresepmakanan.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.model.RecipeNetwork
import com.rekomendasiresepmakanan.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // State untuk menyimpan data resep dari API
    private val _recipes = MutableStateFlow<List<RecipeNetwork>>(emptyList())
    val recipes = _recipes.asStateFlow()

    // State untuk loading (agar bisa menampilkan progress bar)
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // State untuk error message (jika gagal ambil data)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        fetchRecipes()
    }

    fun fetchRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Reset error

            try {
                // Panggil API Laravel
                val response = RetrofitClient.instance.getRecipes()

                // Masukkan data dari respon API ke state
                _recipes.value = response.data

            } catch (e: Exception) {
                // Jika error (misal server mati atau tidak ada internet)
                e.printStackTrace()
                _errorMessage.value = "Gagal memuat data: ${e.localizedMessage}"
            } finally {
                // Selesai loading (baik sukses maupun gagal)
                _isLoading.value = false
            }
        }
    }
}
