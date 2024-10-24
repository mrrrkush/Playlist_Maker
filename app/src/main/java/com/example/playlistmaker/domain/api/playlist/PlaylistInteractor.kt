package com.example.playlistmaker.domain.api.playlist

import android.net.Uri
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(id: Int)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun updatePlaylists(playlist: Playlist)

    suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean>

    suspend fun saveImageToPrivateStorage(uri: Uri)
}