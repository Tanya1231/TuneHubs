package com.example.tunehubs.models

import com.google.gson.annotations.SerializedName

data class TrackInfoResponse(
    val track: TrackDetailInfo
)

data class TrackDetailInfo(
    val name: String,
    val artist: ArtistInfo,
    val album: AlbumInfo?,
    val wiki: WikiInfo?
)

data class ArtistInfo(
    val name: String,
    val url: String
)

data class AlbumInfo(
    val title: String,
    val artist: String,
    val url: String,
    val image: List<ImageInfo>
)

data class ImageInfo(
    @SerializedName("#text") val text: String,
    val size: String
)

data class WikiInfo(
    val published: String,
    val summary: String,
    val content: String
)