package com.example.playlistmaker.domain.api.searchHistory

import com.example.playlistmaker.domain.model.track.Track

interface SearchHistoryInteractor {
    fun saveTrack(track: Track)
    fun getHistory(): List<Track>
    fun clearHistory()
}