package com.rekomendasiresepmakanan.util

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object ImageDownloader {
    // Mutex untuk memastikan hanya 1 download berjalan dalam satu waktu (Serial)
    private val mutex = Mutex()

    suspend fun downloadImage(context: Context, imageUrl: String): File? {
        return withContext(Dispatchers.IO) {
            // Logika Caching Sederhana: Cek apakah file sudah ada di cache
            val filename = imageUrl.hashCode().toString() + ".jpg"
            val file = File(context.cacheDir, filename)
            
            if (file.exists() && file.length() > 0) {
                println("IMG_DL: Cache hit for $imageUrl")
                return@withContext file
            }

            // Jika belum ada, download dengan antrean (Mutex)
            mutex.withLock {
                // Cek lagi (siapa tahu sudah didownload thread lain saat antre)
                if (file.exists() && file.length() > 0) return@withLock file

                println("IMG_DL: Downloading $imageUrl")
                try {
                    val url = URL(imageUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.connectTimeout = 5000
                    connection.readTimeout = 5000
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("Connection", "close") // Force close
                    connection.connect()

                    if (connection.responseCode == 200) {
                        val inputStream = connection.inputStream
                        val outputStream = FileOutputStream(file)
                        
                        val buffer = ByteArray(1024)
                        var len: Int
                        while (inputStream.read(buffer).also { len = it } > 0) {
                            outputStream.write(buffer, 0, len)
                        }
                        
                        outputStream.close()
                        inputStream.close()
                        println("IMG_DL: Success $imageUrl")
                        return@withLock file
                    } else {
                        println("IMG_DL: Failed ${connection.responseCode} for $imageUrl")
                    }
                    connection.disconnect()
                } catch (e: Exception) {
                    println("IMG_DL: Error $imageUrl: ${e.message}")
                    e.printStackTrace()
                }
                return@withLock null
            }
        }
    }
}
