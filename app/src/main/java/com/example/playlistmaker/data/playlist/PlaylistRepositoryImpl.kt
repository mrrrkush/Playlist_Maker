package com.example.playlistmaker.data.playlist

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.DbConverter
import com.example.playlistmaker.data.db.playlist.PlaylistEntity
import com.example.playlistmaker.data.localStorage.LocalStorage
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class   PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: DbConverter,
    private val localStorage: LocalStorage,
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao()
            .addPlaylist(dbConvertor.mapFromPlaylistToPlaylistEntity(playlist))
    }

    override suspend fun deletePlaylist(id: Long) {
        appDatabase.playlistDao().deletePlaylist(id)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun updatePlaylists(playlist: Playlist) {
        appDatabase.playlistDao()
            .updatePlaylist(dbConvertor.mapFromPlaylistToPlaylistEntity(playlist))
    }

    override suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean>  =
        flow {
            val gson = GsonBuilder().create()
            val arrayTrackType = object : TypeToken<ArrayList<Track>>() {}.type

            val playlistTracks =
                gson.fromJson(playlist.trackList, arrayTrackType) ?: arrayListOf<Track>()

            var isInPlaylist = false

            playlistTracks.forEach {
                if (it.trackId == track.trackId) {
                    isInPlaylist = true
                }
            }

            if (!isInPlaylist) {
                playlistTracks.add(track)
                playlist.trackList = gson.toJson(playlistTracks)

                playlist.size = playlist.size + 1
                updatePlaylists(playlist)

                emit(true)
            } else {
                emit(false)
            }
        }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist): Flow<Boolean> = flow {
        val gson = GsonBuilder().create()
        val arrayTrackType = object : TypeToken<ArrayList<Track>>() {}.type

        val playlistTracks =
            gson.fromJson(playlist.trackList, arrayTrackType) ?: arrayListOf<Track>()

        var isInPlaylist = false

        playlistTracks.forEach {
            if (it.trackId == track.trackId) {
                isInPlaylist = true
            }
        }

        if (isInPlaylist) {
            playlistTracks.remove(track)
            playlist.trackList = gson.toJson(playlistTracks)

            playlist.size = playlist.size - 1
            updatePlaylists(playlist)

            emit(true)
        } else {
            emit(false)
        }
    }

    override suspend fun saveImageToPrivateStorage(uri: String) {
        localStorage.saveImageToPrivateStorage(uri)
    }

    override suspend fun getPlaylistById(id: Long): Flow<Playlist> = flow {
        val playlistEntity = appDatabase.playlistDao().getPlaylistById(id)
        if (playlistEntity != null) {
            emit(dbConvertor.mapFromPlaylistEntityToPlaylist(playlistEntity))
        } else {
            emit(Playlist(id = 0, title = "Unknown", description = "No description", imageUri = "", trackList = "", size = 0)) // или выбросить исключение
        }
    }

    override suspend fun getTracksFromPlaylist(id: Long): Flow<List<Track>> = flow {
        val gson = GsonBuilder().create()
        val listTrackType = object : TypeToken<MutableList<Track>>() {}.type

        val tracksString = appDatabase.playlistDao().getTracksFromPlaylist(id)
        val tracks = gson.fromJson(tracksString, listTrackType) ?: mutableListOf<Track>()

        emit(tracks)
    }

    override suspend fun saveCurrentPlaylistId(id: Long) {
        localStorage.saveCurrentPlaylistId(id)
    }

    override suspend fun getCurrentPlaylistId(): Long {
        return localStorage.getCurrentPlaylistId()
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlists ->
            dbConvertor.mapFromPlaylistEntityToPlaylist(
                playlists
            )
        }
    }
}