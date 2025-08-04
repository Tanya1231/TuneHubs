package com.example.tunehubtest.models

// Модель для ответа top tracks
data class TopTracksResponse(
    val tracks: Tracks
)

// Контейнер для списка треков
data class Tracks(
    val track: List<Track>
)

// Детальный трек из списка
data class Track(
    val name: String,
    val artist: Artist,
    val url: String,
    val listeners: String,
    val image: List<Image>
)

// Артист
data class Artist(
    val name: String,
    val url: String
)

// Изображение
data class Image(
    val text: String, // URL картинки
    val size: String
)

// Детальный ответ по треку для получения информации и описания
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
    val text: String, // URL картинки
    val size: String
)

data class WikiInfo(
    val published: String,
    val summary: String,
    val content: String
)