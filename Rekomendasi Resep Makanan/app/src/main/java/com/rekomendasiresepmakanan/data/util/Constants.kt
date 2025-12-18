package com.rekomendasiresepmakanan.data.util

import android.os.Build

object Constants {
    // Gunakan IP Address dari laptop Anda (misal: 192.168.1.7). Jangan gunakan localhost.
    private const val REAL_DEVICE_BASE_URL = "http://192.168.50.81:8000/api/"
    private const val EMULATOR_BASE_URL = "http://10.0.2.2:8000/api/"

    val BASE_URL: String
        get() = if (isEmulator()) EMULATOR_BASE_URL else REAL_DEVICE_BASE_URL

    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT)
    }
}

