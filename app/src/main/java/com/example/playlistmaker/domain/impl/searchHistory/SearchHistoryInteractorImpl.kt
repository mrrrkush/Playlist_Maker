package com.example.playlistmaker.domain.impl.searchHistory

import com.example.playlistmaker.domain.api.searchHistory.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.searchHistory.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun saveTrack(track: Track) = searchHistoryRepository.saveTrack(track)
    override fun getHistory(): List<Track> = searchHistoryRepository.getHistory()
    override fun clearHistory() = searchHistoryRepository.clearHistory()
}