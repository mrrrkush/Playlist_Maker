package com.example.playlistmaker.domain.api.mediateka.favourites

import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesTracksRepository {
    suspend fun insertTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun checkTrackById(trackId: Long): Flow<Boolean>

    fun getFavoritesTracks(): Flow<List<Track>>
}