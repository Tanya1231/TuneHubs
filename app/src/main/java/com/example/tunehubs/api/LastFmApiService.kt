package com.example.tunehubtest.api

import com.example.tunehubs.models.TrackInfoResponse
import com.example.tunehubtest.models.TopTracksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApiService {
    @GET("2.0/?method=chart.gettoptracks&api_key=962cc9583cc6238050031519fd21a7ae&format=json")
    suspend fun getTopTracks(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1
    ): Response<TopTracksResponse>

    @GET("2.0/?method=track.getinfo&api_key=962cc9583cc6238050031519fd21a7ae&format=json")
    suspend fun getTrackInfo(
        @Query("artist") artist: String,
        @Query("track") track: String
    ): Response<TrackInfoResponse>
}