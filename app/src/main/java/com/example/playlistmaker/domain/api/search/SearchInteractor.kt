package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
    fun getHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
}
