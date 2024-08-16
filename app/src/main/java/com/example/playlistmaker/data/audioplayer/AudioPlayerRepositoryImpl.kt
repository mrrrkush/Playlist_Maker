package com.example.playlistmaker.data.audioplayer

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerRepository
import java.io.IOException

class AudioPlayerRepositoryImpl(
    private var mediaPlayer: MediaPlayer
) : AudioPlayerRepository {

    private var isMediaPlayerInitialized: Boolean = false

    private fun getMediaPlayer(): MediaPlayer {
        if (!isMediaPlayerInitialized) {
            isMediaPlayerInitialized = true
            mediaPlayer = MediaPlayer()
        }
        return mediaPlayer
    }

    override fun setDataSource(url: String) {
        val player = getMediaPlayer()
        try {
            player.reset()
            player.setDataSource(url)
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
        if (isMediaPlayerInitialized) {
            mediaPlayer.release()
            isMediaPlayerInitialized = false
        }
    }

    override fun isPlaying(): Boolean {
        return if (isMediaPlayerInitialized) {
            getMediaPlayer().isPlaying
        } else {
            false
        }
    }

    override fun getCurrentPosition(): Int {
        return if (isMediaPlayerInitialized) {
            getMediaPlayer().currentPosition
        } else {
            0
        }
    }

    override fun setOnPreparedListener(listener: () -> Unit) {
        getMediaPlayer().setOnPreparedListener { listener() }
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        getMediaPlayer().setOnCompletionListener { listener() }
    }
}
