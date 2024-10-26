package com.example.playlistmaker.data.db.playlist

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface PlaylistDao {
    @Upsert
    suspend fun savePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId LIMIT 1")
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity

    @Query("SELECT * FROM playlist_table ORDER BY createTime DESC")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Update
    suspend fun updateTracksInPlaylist(playlistEntity: PlaylistEntity)

    @Query("DELETE FROM playlist_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)
}