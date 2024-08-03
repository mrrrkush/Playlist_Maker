package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.model.track.Track

interface TrackRepository {
    fun searchTrack(expression: String): List<Track>
}