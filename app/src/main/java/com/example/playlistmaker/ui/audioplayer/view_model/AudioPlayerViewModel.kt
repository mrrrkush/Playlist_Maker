package com.example.playlistmaker.ui.audioplayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private val _playerState = MutableLiveData(PlayerState.DEFAULT)
    val playerState: LiveData<PlayerState> = _playerState

    private val _currentPosition = MutableLiveData(0)
    val currentPosition: LiveData<Int> = _currentPosition

    private var updateProgressJob: Job? = null

    init {
        audioPlayerInteractor.setOnPreparedListener {
            _playerState.postValue(PlayerState.PREPARED)
        }

        audioPlayerInteractor.setOnCompletionListener {
            _playerState.postValue(PlayerState.PREPARED)
            updateProgressJob?.cancel()
            resetProgress()
        }
    }

    fun prepare(url: String) {
        audioPlayerInteractor.prepare(url)
    }

    fun play() {
        if (playerState.value == PlayerState.PREPARED || playerState.value == PlayerState.PAUSED) {
            audioPlayerInteractor.play()
            _playerState.postValue(PlayerState.PLAYING)
            startProgressUpdater()
        }
    }

    fun pause() {
        if (audioPlayerInteractor.isPlaying()) {
            audioPlayerInteractor.pause()
            _playerState.postValue(PlayerState.PAUSED)
            updateProgressJob?.cancel()
        }
    }

    fun stop() {
        audioPlayerInteractor.stop()
        _playerState.postValue(PlayerState.PREPARED)
        updateProgressJob?.cancel()
        resetProgress()
    }

    fun release() {
        audioPlayerInteractor.release()
        updateProgressJob?.cancel()
    }

    private fun startProgressUpdater() {
        updateProgressJob?.cancel()
        updateProgressJob = viewModelScope.launch(Dispatchers.Main) {
            while (playerState.value == PlayerState.PLAYING) {
                _currentPosition.postValue(audioPlayerInteractor.getCurrentPosition())
                delay(300L)
            }
        }
    }

    private fun resetProgress() {
        _currentPosition.postValue(0)
    }
}

enum class PlayerState {
    DEFAULT,
    PREPARED,
    PLAYING,
    PAUSED
}
