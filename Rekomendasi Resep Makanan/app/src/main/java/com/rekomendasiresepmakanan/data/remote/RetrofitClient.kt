package com.rekomendasiresepmakanan.data.remote

import com.rekomendasiresepmakanan.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // IP Address Laptop (Wi-Fi)
    private const val BASE_URL = "http://192.168.50.81:8000/api/"

    val apiService: ApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                requestBuilder.header("Accept", "application/json") // Penting: Minta JSON ke Laravel
                val token = com.rekomendasiresepmakanan.data.repository.AuthRepository.currentUser.value?.token
                if (!token.isNullOrBlank()) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }
            .connectTimeout(60, TimeUnit.SECONDS)  // Timeout untuk koneksi ke server
            .readTimeout(60, TimeUnit.SECONDS)     // Timeout untuk membaca response
            .writeTimeout(60, TimeUnit.SECONDS)    // Timeout untuk menulis request (upload file)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
