package com.example.tunehub.repository

import com.example.tunehubs.api.RetrofitClient
import com.example.tunehubs.models.TrackInfoResponse
import com.example.tunehubtest.models.TopTracksResponse
import com.google.gson.Gson
import java.io.IOException

class MusicRepository {
    private val apiService = RetrofitClient.apiService
    private val okHttpClient = RetrofitClient.okHttpClient

    suspend fun getTopTracks(limit: Int = 10): Result<TopTracksResponse> {
        return try {
            val response = apiService.getTopTracks(limit)

            if (response.isSuccessful) {
                response.body()?.let {
                    return Result.success(it)
                } ?: return Result.failure(IOException("Ответ пустой"))
            }

            // Обработка кеша при ошибке
            val request = response.raw().request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=86400")
                .build()

            val cachedResponse = okHttpClient.newCall(request).execute()

            if (cachedResponse.isSuccessful) {
                val cachedBodyString = cachedResponse.body?.string()
                val cachedBody = Gson().fromJson(cachedBodyString, TopTracksResponse::class.java)
                Result.success(cachedBody)
            } else {
                Result.failure(IOException("Нет данных в кеше и сеть недоступна"))
            }
        } catch (e: Exception) {
            // В случае исключения возвращаем ошибку
            Result.failure(e)
        }
    }

    suspend fun getTrackInfo(artist: String, track: String): Result<TrackInfoResponse> {
        return try {
            val response = apiService.getTrackInfo(artist, track)

            if (response.isSuccessful) {
                response.body()?.let {
                    return Result.success(it)
                } ?: return Result.failure(IOException("Ответ пустой"))
            }

            // Обработка кеша при ошибке
            val request = response.raw().request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=86400")
                .build()

            val cachedResponse = okHttpClient.newCall(request).execute()

            if (cachedResponse.isSuccessful) {
                val cachedBodyString = cachedResponse.body?.string()
                val cachedBody = Gson().fromJson(cachedBodyString, TrackInfoResponse::class.java)
                Result.success(cachedBody)
            } else {
                Result.failure(IOException("Нет данных в кеше и сеть недоступна"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun testApiPerformance(iterations: Int): String {
        var totalTime = 0L
        for (i in 1..iterations) {
            val start = System.currentTimeMillis()
            getTopTracks(10)
            val end = System.currentTimeMillis()
            totalTime += (end - start)
        }
        val averageTime = totalTime / iterations
        return "Среднее время: ${averageTime} мс"
    }
}