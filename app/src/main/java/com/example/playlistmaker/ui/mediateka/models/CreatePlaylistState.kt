package com.example.playlistmaker.ui.mediateka.models

import com.example.playlistmaker.domain.model.playlist.Playlist

sealed interface CreatePlaylistState {

    data class SaveSuccess(val name: String): CreatePlaylistState

    data class EditInProgress(val isStarted: Boolean): CreatePlaylistState

    data class LoadPlaylist(val playlist: Playlist): CreatePlaylistState
}