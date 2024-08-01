package com.example.playlistmaker.domain.impl.audioplayer

import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerRepository

class AudioPlayerInteractorImpl(private val audioPlayer: AudioPlayerRepository) :
    AudioPlayerInteractor {

    override fun prepare(url: String) {
        audioPlayer.setDataSource(url)
        audioPlayer.prepare()
    }

    override fun play() {
        audioPlayer.start()
    }

    override fun pause() {
        audioPlayer.pause()
    }

    override fun stop() {
        audioPlayer.stop()
    }

    override fun release() {
        audioPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return audioPlayer.isPlaying()
    }

    override fun getCurrentPosition(): Int {
        return audioPlayer.getCurrentPosition()
    }

    override fun setOnPreparedListener(listener: () -> Unit) {
        audioPlayer.setOnPreparedListener(listener)
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        audioPlayer.setOnCompletionListener(listener)
    }
}