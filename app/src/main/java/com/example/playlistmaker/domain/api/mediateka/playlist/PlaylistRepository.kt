package com.example.playlistmaker.domain.api.mediateka.playlist

import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun savePlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlistId: Int)

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun getPlaylistById(playlistId: Int): Playlist

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist)

    fun getTracksFromPlaylist(tracksId: List<Int>): Flow<List<Track>>

    fun sharePlaylist(playlistDescription: String)
}