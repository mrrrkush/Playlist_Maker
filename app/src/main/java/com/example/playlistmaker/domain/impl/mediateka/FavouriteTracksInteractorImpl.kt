package com.example.playlistmaker.domain.impl.mediateka

import com.example.playlistmaker.domain.api.mediateka.FavouriteTracksInteractor
import com.example.playlistmaker.domain.api.mediateka.FavouriteTracksRepository
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(
    private val favouriteTracksRepository: FavouriteTracksRepository
) : FavouriteTracksInteractor {

    override suspend fun getFavoritesTracks(): Flow<List<Track>> {
        return favouriteTracksRepository.getFavoritesTracks()
    }

    override suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean> {
        return favouriteTracksRepository.isFavoriteTrack(trackId)
    }

    override suspend fun addToFavorites(track: Track) {
        favouriteTracksRepository.addToFavorites(track)
    }

    override suspend fun deleteFromFavorites(trackId: Int) {
        favouriteTracksRepository.deleteFromFavorites(trackId)
    }
}