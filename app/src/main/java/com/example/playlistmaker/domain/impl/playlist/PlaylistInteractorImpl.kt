package com.example.playlistmaker.domain.impl.playlist

import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(id: Long) {
        playlistRepository.deletePlaylist(id)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun updatePlaylists(playlist: Playlist) {
        playlistRepository.updatePlaylists(playlist)
    }

    override suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean> =
        playlistRepository.addTrackToPlayList(track, playlist)

    override suspend fun saveImageToPrivateStorage(uri: String) {
        playlistRepository.saveImageToPrivateStorage(uri)
    }

    override suspend fun getPlaylistById(id: Long): Flow<Playlist> {
        return playlistRepository.getPlaylistById(id)
    }

    override suspend fun getTracksFromPlaylist(id: Long): Flow<List<Track>> {
        return playlistRepository.getTracksFromPlaylist(id)
    }

    override suspend fun saveCurrentPlaylistId(id: Long) {
        playlistRepository.saveCurrentPlaylistId(id)
    }

    override suspend fun getCurrentPlaylistId(): Long {
        return playlistRepository.getCurrentPlaylistId()
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist): Flow<Boolean> {
        return playlistRepository.deleteTrackFromPlaylist(track, playlist)
    }
}