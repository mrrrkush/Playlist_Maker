package com.example.playlistmaker.domain.impl.mediateka.favourites

import com.example.playlistmaker.domain.api.mediateka.favourites.FavoritesTracksInteractor
import com.example.playlistmaker.domain.api.mediateka.favourites.FavoritesTracksRepository
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

class FavoritesTracksInteractorImpl(
    val repository: FavoritesTracksRepository
) : FavoritesTracksInteractor {
    override suspend fun insertTrack(track: Track) {
        repository.insertTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        repository.deleteTrack(track)
    }

    override fun checkTrackById(trackId: Long): Flow<Boolean> {
        return repository.checkTrackById(trackId)
    }

    override fun getFavoritesTracks(): Flow<List<Track>> {
        return repository.getFavoritesTracks()
    }
}