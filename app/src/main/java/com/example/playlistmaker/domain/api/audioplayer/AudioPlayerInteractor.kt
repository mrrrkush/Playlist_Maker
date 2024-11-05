package com.example.playlistmaker.domain.api.audioplayer

import com.example.playlistmaker.ui.audioplayer.models.PlayerState

interface AudioPlayerInteractor {
    fun createPlayer(trackUrl: String, completion: () -> Unit)

    fun play()

    fun pause()

    fun release()

    fun getPlayerState(): PlayerState
}