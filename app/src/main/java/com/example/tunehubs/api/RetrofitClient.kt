package com.example.tunehubs.api

import com.example.tunehubs.utils.App
import com.example.tunehubtest.api.LastFmApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.io.File

object RetrofitClient {
    private const val BASE_URL = "https://ws.audioscrobbler.com/"

    private val cacheSize = 10L * 1024L * 1024L // 10MB
    private val cache = Cache(File(App.context.cacheDir, "http_cache"), cacheSize)

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                if (!App.isNetworkAvailable()) {
                    request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=86400")
                        .build()
                }
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    val apiService: LastFmApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LastFmApiService::class.java)
    }
}