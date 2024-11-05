package com.example.playlistmaker.data.db.track

import com.example.playlistmaker.data.db.playlist.PlaylistTrackEntity
import com.example.playlistmaker.domain.model.track.Track

class TrackDbConvertor {
    fun mapTrackToEntity(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            addTime = System.currentTimeMillis()
        )
    }

    fun mapEntityToTrack(entity: TrackEntity): Track {
        return Track(
            entity.trackId,
            entity.trackName,
            entity.artistName,
            entity.trackTimeMillis,
            entity.artworkUrl100,
            entity.collectionName,
            entity.releaseDate,
            entity.primaryGenreName,
            entity.country,
            entity.previewUrl
        )
    }

    fun mapTrackToPlaylistTrackEntity(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            track.trackId.toInt(),
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            addTime = System.currentTimeMillis()
        )
    }

    fun mapPlaylistTrackEntityToTrack(trackEntity: PlaylistTrackEntity): Track {
        return Track(
            trackEntity.trackId.toLong(),
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTimeMillis,
            trackEntity.artworkUrl100,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl,
        )
    }
}