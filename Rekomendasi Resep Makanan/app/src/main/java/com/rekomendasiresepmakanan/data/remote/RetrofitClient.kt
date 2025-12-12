package com.rekomendasiresepmakanan.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // ============================================================================
    // PENTING: Konfigurasi BASE_URL untuk berbagai environment
    // ============================================================================

        // UNTUK EMULATOR ANDROID STUDIO:
//     private const val BASE_URL = "http://10.0.2.2:8000/"
    
    // UNTUK HP FISIK:
    // 1. Cek IP laptop dengan command: ipconfig (Windows) atau ifconfig (Mac/Linux)
    // 2. Jalankan Laravel: php artisan serve --host=0.0.0.0 --port=8000
    // 3. GANTI IP di bawah dengan IP laptop Anda
    // 4. HP dan Laptop HARUS di WiFi yang sama!
    
    private const val BASE_URL = "http://192.168.50.81:8000/"  // ← GANTI dengan IP laptop Anda!

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
