package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
    fun getHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
}