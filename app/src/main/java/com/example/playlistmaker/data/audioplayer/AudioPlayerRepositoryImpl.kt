package com.example.playlistmaker.data.audioplayer

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerRepository

class AudioPlayerRepositoryImpl : AudioPlayerRepository {
    private val mediaPlayer = MediaPlayer()

    override fun setDataSource(url: String) {
        mediaPlayer.setDataSource(url)
    }

    override fun prepare() {
        mediaPlayer.prepareAsync()
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun stop() {
        mediaPlayer.stop()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setOnPreparedListener(listener: () -> Unit) {
        mediaPlayer.setOnPreparedListener { listener() }
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener { listener() }
    }
}