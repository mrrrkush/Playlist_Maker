package com.example.playlistmaker.ui.audioplayer.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerInteractor

class AudioPlayerViewModelFactory(
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AudioPlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AudioPlayerViewModel(audioPlayerInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
