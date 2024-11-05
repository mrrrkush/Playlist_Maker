package com.example.playlistmaker.ui.mediateka.models

import com.example.playlistmaker.domain.model.playlist.Playlist

sealed class PlaylistsState {
    data class WasAdded(val playlistName: String) : PlaylistsState()

    data class AlreadyAdded(val playlistName: String) : PlaylistsState()

    data class ShowPlaylists(val playlists: List<Playlist>) : PlaylistsState()
}