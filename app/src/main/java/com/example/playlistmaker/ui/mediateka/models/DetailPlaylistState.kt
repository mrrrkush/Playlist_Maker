package com.example.playlistmaker.ui.mediateka.models

import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track

sealed interface DetailPlaylistState {

    data class Content(val playlist: Playlist, val trackList: List<Track>) : DetailPlaylistState

    data class Message(val text: String) : DetailPlaylistState

    data object PlaylistDeleted : DetailPlaylistState
}