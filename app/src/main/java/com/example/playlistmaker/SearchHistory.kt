package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPref: SharedPreferences) {

    private val gson = Gson()
    private val historyKey = "search_history"

    fun saveTrack(track: Track) {
        val trackList = getHistory()
        trackList.removeAll { it.trackId == track.trackId }
        trackList.add(0, track)

        if (trackList.size > 10) {
            trackList.removeAt(trackList.size - 1)
        }
        saveHistory(trackList)
    }

    fun getHistory(): ArrayList<Track> {
        val jsonString = sharedPref.getString(historyKey, null)
        return if (jsonString != null) {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            gson.fromJson(jsonString, type) ?: ArrayList()
        } else {
            ArrayList()
        }
    }

    fun clearHistory() {
        sharedPref.edit().remove(historyKey).apply()
    }

    private fun saveHistory(trackList: ArrayList<Track>) {
        val jsonString = gson.toJson(trackList)
        sharedPref.edit().putString(historyKey, jsonString).apply()
    }
}



