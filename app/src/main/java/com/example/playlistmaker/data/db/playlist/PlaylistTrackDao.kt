package com.example.playlistmaker.data.db.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM tracks_from_playlists WHERE trackId = :trackId limit 1")
    suspend fun getTrackById(trackId: Long): PlaylistTrackEntity

    @Query("SELECT * FROM tracks_from_playlists ORDER BY addTime DESC")
    suspend fun getAllTracks(): List<PlaylistTrackEntity>

    @Delete
    suspend fun deleteTrack(track: PlaylistTrackEntity)
}