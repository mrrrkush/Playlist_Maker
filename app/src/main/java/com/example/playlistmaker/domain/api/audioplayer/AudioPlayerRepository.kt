package com.example.playlistmaker.domain.api.audioplayer

interface AudioPlayerRepository {
    fun setDataSource(url: String)
    fun prepare()
    fun start()
    fun pause()
    fun stop()
    fun release()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int
    fun setOnPreparedListener(listener: () -> Unit)
    fun setOnCompletionListener(listener: () -> Unit)
}
