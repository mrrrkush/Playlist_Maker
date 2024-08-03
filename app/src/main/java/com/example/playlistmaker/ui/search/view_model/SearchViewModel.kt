package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.search.TrackInteractor
import com.example.playlistmaker.domain.api.searchHistory.SearchHistoryInteractor
import com.example.playlistmaker.domain.model.track.Track

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

    init {
        loadSearchHistory()
    }

    fun searchTrack(expression: String) {
        _isLoading.value = true
        trackInteractor.searchTrack(expression, object : TrackInteractor.TrackConsumer {
            override fun consume(foundTrack: List<Track>) {
                _isLoading.postValue(false)
                _tracks.postValue(foundTrack)
                if (foundTrack.isNotEmpty()) {
                    searchHistoryInteractor.saveTrack(foundTrack.first())
                }
            }
        })
    }

    private fun loadSearchHistory() {
        _searchHistory.value = searchHistoryInteractor.getHistory()
    }

    fun clearSearchHistory() {
        searchHistoryInteractor.clearHistory()
        _searchHistory.value = emptyList()
    }
}