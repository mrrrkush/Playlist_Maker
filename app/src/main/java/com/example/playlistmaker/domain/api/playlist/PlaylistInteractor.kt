package com.example.playlistmaker.domain.api.playlist

import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(id: Long)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun updatePlaylists(playlist: Playlist)

    suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean>

    suspend fun saveImageToPrivateStorage(uri: String)

    suspend fun getPlaylistById(id: Long): Flow<Playlist>

    suspend fun getTracksFromPlaylist(id: Long): Flow<List<Track>>

    suspend fun saveCurrentPlaylistId(id: Long)

    suspend fun getCurrentPlaylistId(): Long

    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist): Flow<Boolean>
}