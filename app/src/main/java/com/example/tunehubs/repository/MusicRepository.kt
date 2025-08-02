package com.example.tunehub.repository

import com.example.tunehubs.api.RetrofitClient
import com.example.tunehubs.models.TrackInfoResponse
import com.example.tunehubtest.models.TopTracksResponse
import java.io.IOException

class MusicRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getTopTracks(limit: Int = 10): Result<TopTracksResponse> {
        return try {
            val response = apiService.getTopTracks(limit = limit)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(IOException("Response body is null"))
            } else {
                Result.failure(IOException("API call failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTrackInfo(artist: String, track: String): Result<TrackInfoResponse> {
        return try {
            val response = apiService.getTrackInfo(artist = artist, track = track)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(IOException("Response body is null"))
            } else {
                Result.failure(IOException("API call failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun testApiPerformance(iterations: Int): String {
        val startTime = System.currentTimeMillis()
        var successCount = 0
        var failCount = 0

        repeat(iterations) {
            try {
                val response = apiService.getTopTracks()
                if (response.isSuccessful) {
                    successCount++
                } else {
                    failCount++
                }
            } catch (e: Exception) {
                failCount++
            }
        }

        val totalTime = System.currentTimeMillis() - startTime
        val avgTime = totalTime / iterations.toFloat()

        return """
            Performance Test Results:
            ------------------------
            Iterations: $iterations
            Successful requests: $successCount
            Failed requests: $failCount
            Total time: ${totalTime}ms
            Average time per request: ${avgTime}ms
        """.trimIndent()
    }
}