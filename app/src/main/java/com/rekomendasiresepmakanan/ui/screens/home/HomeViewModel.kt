package com.rekomendasiresepmakanan.ui.screens.home // Pastikan package ini benar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekomendasiresepmakanan.data.model.RecipeNetwork
import com.rekomendasiresepmakanan.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // 1. State untuk menampung data Resep dari Laravel
    private val _recipes = MutableStateFlow<List<RecipeNetwork>>(emptyList())
    val recipes = _recipes.asStateFlow()

    // 2. State untuk Loading (agar UI bisa menampilkan spinner)
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // 3. State untuk Error Message (jika koneksi gagal)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Init block dipanggil otomatis saat ViewModel dibuat
    init {
        fetchRecipes()
    }

    // Fungsi untuk mengambil data dari API
    fun fetchRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Reset error

            try {
                // Panggil endpoint API via RetrofitClient
                // Pastikan RetrofitClient Anda base URL-nya http://10.0.2.2:8000/
                val response = RetrofitClient.instance.getRecipes()

                // Masukkan data (response.data) ke dalam state _recipes
                _recipes.value = response.data

                // Debugging Log (Cek di Logcat jika masih gagal)
                android.util.Log.d("API_SUCCESS", "Berhasil dapat ${response.data.size} resep")

            } catch (e: Exception) {
                e.printStackTrace()
                // Simpan pesan error agar bisa ditampilkan di layar
                _errorMessage.value = "Gagal: ${e.message}"
                android.util.Log.e("API_ERROR", "Error fetch data: ${e.message}")
            } finally {
                // Matikan loading baik sukses maupun gagal
                _isLoading.value = false
            }
        }
    }
}
