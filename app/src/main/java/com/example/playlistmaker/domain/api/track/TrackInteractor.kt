package com.example.playlistmaker.domain.api.track

import com.example.playlistmaker.domain.models.Track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: List<Track>)
    }
}
