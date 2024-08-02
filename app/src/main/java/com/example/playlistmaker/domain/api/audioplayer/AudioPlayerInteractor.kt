package com.example.playlistmaker.domain.api.audioplayer

interface AudioPlayerInteractor {
    fun prepare(url: String)
    fun play()
    fun pause()
    fun stop()
    fun release()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int
    fun setOnPreparedListener(listener: () -> Unit)
    fun setOnCompletionListener(listener: () -> Unit)
}