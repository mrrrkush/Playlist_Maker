package com.example.playlistmaker.domain.impl.search

import com.example.playlistmaker.domain.api.search.TrackInteractor
import com.example.playlistmaker.domain.api.search.TrackRepository
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {
    private val executor: ExecutorService = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            val foundTracks = repository.searchTrack(expression)
            consumer.consume(foundTracks)
        }
    }
}

