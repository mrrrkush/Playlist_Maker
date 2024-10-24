package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.model.playlist.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BasePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    ) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(playlist)
        }
    }

    fun saveToLocalStorage(uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.saveImageToPrivateStorage(uri)
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylists(playlist)
        }
    }
}