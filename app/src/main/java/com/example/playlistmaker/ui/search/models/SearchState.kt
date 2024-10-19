package com.example.playlistmaker.ui.search.models

import com.example.playlistmaker.domain.model.search.NetworkError
import com.example.playlistmaker.domain.model.track.Track

sealed interface SearchState {
    class SearchHistory(val tracks: List<Track>) : SearchState
    class SearchedTracks(val tracks: List<Track>) : SearchState
    class SearchError(val error: NetworkError) : SearchState
    object Loading : SearchState
}