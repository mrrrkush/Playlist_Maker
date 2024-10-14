package com.example.playlistmaker.domain.impl.search

import com.example.playlistmaker.domain.api.search.TrackInteractor
import com.example.playlistmaker.domain.api.search.TrackRepository
import com.example.playlistmaker.domain.model.track.Track
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override fun searchTrack(expression: String): Flow<List<Track>> {
        return repository.searchTrack(expression)
    }
}

