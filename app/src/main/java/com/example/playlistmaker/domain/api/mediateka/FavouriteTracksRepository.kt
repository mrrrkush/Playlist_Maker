package com.example.playlistmaker.domain.api.mediateka

import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {

    suspend fun getFavoritesTracks(): Flow<List<Track>>

    suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean>

    suspend fun addToFavorites(track: Track)

    suspend fun deleteFromFavorites(trackId: Int)
}