package com.rekomendasiresepmakanan.data.util

/**
 * Sealed class untuk UI State management yang type-safe.
 * Digunakan untuk menangani 3 state: Loading, Success, dan Error.
 */
sealed class UiState<out T> {
    /**
     * State saat data sedang dimuat dari API
     */
    object Loading : UiState<Nothing>()
    
    /**
     * State saat data berhasil dimuat
     * @param data Data yang berhasil dimuat dari API
     */
    data class Success<T>(val data: T) : UiState<T>()
    
    /**
     * State saat terjadi error (network error, parsing error, dll)
     * @param message Pesan error yang akan ditampilkan ke user
     */
    data class Error(val message: String) : UiState<Nothing>()
}

