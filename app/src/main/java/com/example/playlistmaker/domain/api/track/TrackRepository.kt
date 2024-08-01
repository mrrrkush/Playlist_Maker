package com.example.playlistmaker.domain.api.track

import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
    fun searchTrack(expression: String): List<Track>
}