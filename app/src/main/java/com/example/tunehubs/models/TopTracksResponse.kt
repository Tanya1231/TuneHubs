package com.example.tunehubtest.models

// Модель для ответа top tracks
data class TopTracksResponse(
    val tracks: Tracks
)

data class Tracks(
    val track: List<Track>
)

data class Track(
    val name: String,
    val artist: Artist,
    val url: String,
    val listeners: String,
    val image: List<Image>
)

data class Artist(
    val name: String,
    val url: String
)

data class Image(
    val text: String,
    val size: String
)