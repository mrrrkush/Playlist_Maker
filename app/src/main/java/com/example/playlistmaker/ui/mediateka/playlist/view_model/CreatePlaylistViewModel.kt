package com.example.playlistmaker.ui.mediateka.playlist.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.mediateka.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.ui.mediateka.models.CreatePlaylistState
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private var playlist: Playlist? = null

    private val _state = MutableLiveData<CreatePlaylistState>()
    val state: LiveData<CreatePlaylistState>
        get() = _state

    fun createPlaylist(name: String, description: String?, coverUri: Uri?) {
        viewModelScope.launch {
            if (playlist != null) {
                playlistInteractor.savePlaylist(
                    Playlist(
                        id = playlist?.id ?: 0,
                        name = name,
                        description = description,
                        coverUri = coverUri,
                        tracksId = playlist?.tracksId ?: emptyList(),
                        playlistDuration = playlist?.playlistDuration ?: 0
                    )
                )
            } else {
                playlistInteractor.savePlaylist(
                    Playlist(
                        id = 0,
                        name = name,
                        description = description,
                        coverUri = coverUri,
                        tracksId = emptyList(),
                        playlistDuration = 0
                    )
                )
            }
            _state.postValue(CreatePlaylistState.SaveSuccess(name))
        }
    }

    fun getPlaylistById(playlistId: Int) {
        viewModelScope.launch {
            playlist = playlistInteractor.getPlaylistById(playlistId)
            playlist.let {
                _state.postValue(CreatePlaylistState.LoadPlaylist(it!!))
            }
        }
    }

    fun checkBeforeCloseScreen(uri: Uri?, name: String, description: String) {
        val isEditingStarted = uri != null || name.isNotEmpty() || description.isNotEmpty()
        _state.value = CreatePlaylistState.EditInProgress(isEditingStarted)
    }
}