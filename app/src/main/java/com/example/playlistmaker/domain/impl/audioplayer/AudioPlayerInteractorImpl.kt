package com.example.playlistmaker.domain.impl.audioplayer

import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerRepository
import com.example.playlistmaker.ui.audioplayer.models.PlayerState

class AudioPlayerInteractorImpl(private val repository: AudioPlayerRepository): AudioPlayerInteractor {
    override fun createPlayer(trackUrl: String, completion: () -> Unit) {
        repository.createPlayer(trackUrl, completion)
    }

    override fun play() {
        repository.play()
    }

    override fun pause() {
        repository.pause()
    }

    override fun release() {
        repository.release()
    }

    override fun getPlayerState(): PlayerState {
        return repository.playerState()
    }
}