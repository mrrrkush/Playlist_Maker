package com.example.playlistmaker.data.mediateka

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.DbConverter
import com.example.playlistmaker.data.db.track.TrackEntity
import com.example.playlistmaker.domain.api.mediateka.FavouriteTracksRepository
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: DbConverter,
): FavouriteTracksRepository {

    override suspend fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun isFavoriteTrack(trackId: Long): Flow<Boolean> = flow {
        val isFavorite = appDatabase.trackDao().isFavoriteTrack(trackId)
        emit(isFavorite)
    }

    override suspend fun addToFavorites(track: Track) {
        appDatabase.trackDao().addToFavorites(trackDbConvertor.mapFromTrackToTrackEntity(track))
    }

    override suspend fun deleteFromFavorites(trackId: Long) {
        appDatabase.trackDao().deleteFromFavorites(trackId)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.mapFromTrackEntityToTrack(track) }
    }
}