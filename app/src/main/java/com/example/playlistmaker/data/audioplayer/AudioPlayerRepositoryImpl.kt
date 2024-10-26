package com.example.playlistmaker.data.audioplayer

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerRepository
import com.example.playlistmaker.ui.audioplayer.models.PlayerState

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {

    private var playerState: PlayerState = PlayerState.Default

    override fun createPlayer(trackUrl: String, completion: () -> Unit) {
        with(mediaPlayer) {
            try {
                setDataSource(trackUrl)
                prepareAsync()
            } catch (e: Exception) {
                playerState = PlayerState.Default
            }
            setOnPreparedListener {
                playerState = PlayerState.Prepared
                completion()
            }
            setOnCompletionListener {
                playerState = PlayerState.Prepared
                completion()
            }
        }
    }

    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.Playing(elapsedTime())
    }

    override fun pause() {
        when (playerState) {
            is PlayerState.Default -> {}
            is PlayerState.Prepared -> {}
            else -> {
                mediaPlayer.pause()
                playerState = PlayerState.Paused(elapsedTime())
            }
        }
    }

    override fun release() {
        if (playerState != PlayerState.Default) {
            mediaPlayer.stop()
            mediaPlayer.release()
            playerState = PlayerState.Default
        }
    }

    override fun playerState(): PlayerState {
        return when (playerState) {
            is PlayerState.Default -> {
                PlayerState.Default
            }

            is PlayerState.Prepared -> {
                PlayerState.Prepared
            }

            is PlayerState.Playing -> {
                PlayerState.Playing(elapsedTime())
            }

            is PlayerState.Paused -> {
                PlayerState.Paused(elapsedTime())
            }
        }
    }

    private fun elapsedTime(): Int {
        return mediaPlayer.currentPosition
    }
}
