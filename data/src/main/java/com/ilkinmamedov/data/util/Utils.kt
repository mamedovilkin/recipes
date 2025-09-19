package com.ilkinmamedov.data.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(1, TimeUnit.SECONDS)
    .readTimeout(1, TimeUnit.SECONDS)
    .writeTimeout(1, TimeUnit.SECONDS)
    .build()

suspend fun isInternetAvailable(): Boolean = withContext(Dispatchers.IO) {
    try {
        val request = Request.Builder()
            .url("https://www.gstatic.com/generate_204")
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            response.isSuccessful
        }
    } catch (_: IOException) {
        false
    }
}