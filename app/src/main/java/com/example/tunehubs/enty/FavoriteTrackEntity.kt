package com.example.tunehubs.enty

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class FavoriteTrackEntity(
    @PrimaryKey val name: String,
    val artistName: String,
    val imageUrl: String,
    val description: String
)