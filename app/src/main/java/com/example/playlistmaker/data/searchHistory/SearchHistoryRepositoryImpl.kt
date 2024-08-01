package com.example.playlistmaker.data.searchHistory

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.searchHistory.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(private val sharedPref: SharedPreferences) :
    SearchHistoryRepository {
    private val gson = Gson()
    private val historyKey = "search_history"

    override fun saveTrack(track: Track) {
        val trackList = getHistory().toMutableList()
        trackList.removeAll { it.trackId == track.trackId }
        trackList.add(0, track)

        if (trackList.size > 10) {
            trackList.removeAt(trackList.size - 1)
        }
        saveHistory(trackList)
    }

    override fun getHistory(): List<Track> {
        val jsonString = sharedPref.getString(historyKey, null)
        return if (jsonString != null) {
            val type = object : TypeToken<List<Track>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyList()
        } else {
            emptyList()
        }
    }

    override fun clearHistory() {
        sharedPref.edit().remove(historyKey).apply()
    }

    private fun saveHistory(trackList: List<Track>) {
        val jsonString = gson.toJson(trackList)
        sharedPref.edit().putString(historyKey, jsonString).apply()
    }
}