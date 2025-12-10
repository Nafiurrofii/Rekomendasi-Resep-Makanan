package com.rekomendasiresepmakanan.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // PENTING: Ganti URL ini sesuai kebutuhan
    // 1. Jika pakai Emulator Android Studio: gunakan "http://10.0.2.2:8000/"
    // 2. Jika pakai HP Fisik: gunakan IP Laptop (contoh: "http://192.168.1.10:8000/")
    // Pastikan diakhiri dengan garis miring "/"
    private const val BASE_URL = "http://10.0.2.2:8000/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
