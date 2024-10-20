package com.example.playlistmaker.domain.api.audioplayer

import com.example.playlistmaker.ui.audioplayer.models.PlayerState

interface AudioPlayerRepository {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun reset()
    fun getPosition() : Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}
