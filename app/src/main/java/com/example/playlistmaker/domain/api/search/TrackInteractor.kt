package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTrack(expression: String): Flow<List<Track>>
}
