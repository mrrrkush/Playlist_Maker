package com.example.playlistmaker.domain.impl.audioplayer

import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerRepository
import com.example.playlistmaker.ui.audioplayer.models.PlayerState

class AudioPlayerInteractorImpl(private val repository: AudioPlayerRepository): AudioPlayerInteractor {
    override fun preparePlayer(url: String) {
        repository.preparePlayer(url)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun reset() {
        repository.reset()
    }

    override fun getPosition(): Long = repository.getPosition()

    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        repository.setOnStateChangeListener(callback)
    }
}