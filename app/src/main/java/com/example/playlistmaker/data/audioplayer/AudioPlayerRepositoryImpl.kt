package com.example.playlistmaker.data.audioplayer

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerRepository
import java.io.IOException

class AudioPlayerRepositoryImpl : AudioPlayerRepository {
    private var mediaPlayer: MediaPlayer? = MediaPlayer()

    private fun getMediaPlayer(): MediaPlayer {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        return mediaPlayer!!
    }

    override fun setDataSource(url: String) {
        val mp = getMediaPlayer()
        try {
            mp.reset()
            mp.setDataSource(url)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun prepare() {
        getMediaPlayer().prepareAsync()
    }

    override fun start() {
        getMediaPlayer().start()
    }

    override fun pause() {
        getMediaPlayer().pause()
    }

    override fun stop() {
        getMediaPlayer().stop()
    }

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun isPlaying(): Boolean {
        return getMediaPlayer().isPlaying
    }

    override fun getCurrentPosition(): Int {
        return getMediaPlayer().currentPosition
    }

    override fun setOnPreparedListener(listener: () -> Unit) {
        getMediaPlayer().setOnPreparedListener { listener() }
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        getMediaPlayer().setOnCompletionListener { listener() }
    }
}
