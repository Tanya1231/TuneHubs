package com.example.tunehubs.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tunehubs.enty.FavoriteTrackEntity

@Dao
interface FavoriteTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteTrack: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks")
    suspend fun getAllFavorites(): List<FavoriteTrackEntity>

    @Query("DELETE FROM favorite_tracks WHERE name = :trackName")
    suspend fun deleteFavorite(trackName: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_tracks WHERE name = :trackName)")
    suspend fun isFavorite(trackName: String): Boolean
}