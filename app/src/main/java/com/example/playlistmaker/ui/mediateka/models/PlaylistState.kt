package com.example.playlistmaker.ui.mediateka.models

import com.example.playlistmaker.domain.model.playlist.Playlist

sealed interface PlaylistState {
    data object Empty : PlaylistState
    data class Content(val playlists: List<Playlist>) : PlaylistState
}