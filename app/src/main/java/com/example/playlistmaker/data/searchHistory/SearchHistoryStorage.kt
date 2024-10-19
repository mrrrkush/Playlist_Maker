package com.example.playlistmaker.data.searchHistory

import com.example.playlistmaker.domain.model.track.Track

interface SearchHistoryStorage {
    fun saveHistory(tracks: List<Track>)
    fun getHistory(): List<Track>
    fun clearHistory()
}