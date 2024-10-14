package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.search.TrackInteractor
import com.example.playlistmaker.domain.api.searchHistory.SearchHistoryInteractor
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchHistory = MutableLiveData<List<Track>>()
    val searchHistory: LiveData<List<Track>> = _searchHistory

    private var searchJob: Job? = null

    init {
        loadSearchHistory()
    }

    fun searchTrack(expression: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _isLoading.value = true
            trackInteractor.searchTrack(expression)
                .debounce(2000)
                .collect { foundTracks ->
                    _isLoading.postValue(false)
                    _tracks.postValue(foundTracks)
                    if (foundTracks.isNotEmpty()) {
                        searchHistoryInteractor.saveTrack(foundTracks.first())
                    }
                }
        }
    }

    private fun loadSearchHistory() {
        _searchHistory.value = searchHistoryInteractor.getHistory()
    }

    fun clearSearchHistory() {
        searchHistoryInteractor.clearHistory()
        _searchHistory.value = emptyList()
    }
}