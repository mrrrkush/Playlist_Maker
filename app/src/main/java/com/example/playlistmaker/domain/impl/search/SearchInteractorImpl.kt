package com.example.playlistmaker.domain.impl.search

import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.api.search.SearchRepository
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(val repository: SearchRepository): SearchInteractor {

    override fun searchTracks(expression: String) : Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun getHistory(): List<Track> {
        return repository.getHistory().toList()
    }

    override fun saveHistory(tracks: List<Track>) {
        repository.saveHistory(tracks)
    }

}

