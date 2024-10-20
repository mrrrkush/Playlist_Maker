package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.ui.mediateka.models.PlaylistsScreenState
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val _stateLiveData = MutableLiveData<PlaylistsScreenState>()
    val stateLiveData: LiveData<PlaylistsScreenState> = _stateLiveData

    init {
        _stateLiveData.postValue(PlaylistsScreenState.Empty)
    }

    fun fillData() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                processResult(it)
            }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _stateLiveData.postValue(PlaylistsScreenState.Empty)
        } else {
            _stateLiveData.postValue(PlaylistsScreenState.Filled(playlists))
        }
    }
}