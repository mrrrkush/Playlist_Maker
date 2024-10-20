package com.example.playlistmaker.ui.mediateka.models

import com.example.playlistmaker.domain.model.playlist.Playlist

sealed class PlaylistsScreenState(playlists: List<Playlist>?) {
    object Empty : PlaylistsScreenState(null)
    class Filled(val playlists: List<Playlist>) : PlaylistsScreenState(playlists)
}