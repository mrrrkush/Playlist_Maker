package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.model.track.Track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: List<Track>)
    }
}
