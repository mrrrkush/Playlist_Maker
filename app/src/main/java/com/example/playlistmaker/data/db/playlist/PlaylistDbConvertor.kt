package com.example.playlistmaker.data.db.playlist

import android.net.Uri
import com.example.playlistmaker.domain.model.playlist.Playlist

class PlaylistDbConvertor {
    fun mapPlaylistToEntity(playlist: Playlist): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                id = id,
                name = name,
                description = description ?: "",
                coverUri = coverUri.toString(),
                tracksId = tracksId.joinToString(","),
                numberOfTracks = numberOfTracks,
                playlistDuration = playlistDuration,
                createTime = System.currentTimeMillis()
            )
        }
    }

    fun mapEntityListToPlaylists(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map {
            mapEntityToPlaylist(it)
        }
    }

    fun mapEntityToPlaylist(playlistEntity: PlaylistEntity): Playlist {
        return with(playlistEntity) {
            Playlist(
                id = id,
                name = name,
                description = description,
                coverUri = Uri.parse(coverUri),
                tracksId = mapStringToListInt(tracksId),
                playlistDuration = playlistDuration,
                numberOfTracks = numberOfTracks
            )
        }
    }

    fun mapStringToListInt(tracksId: String): List<Int> {
        return if (tracksId.isEmpty()) {
            emptyList()
        } else {
            tracksId.split(",").map { it.toInt() }
        }
    }
}