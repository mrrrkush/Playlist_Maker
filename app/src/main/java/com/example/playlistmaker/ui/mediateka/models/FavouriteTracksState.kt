package com.example.playlistmaker.ui.mediateka.models

import com.example.playlistmaker.domain.model.track.Track

sealed interface FavouriteTracksState {

    data class Content(
        val tracks: List<Track>
    ) : FavouriteTracksState

    object Empty : FavouriteTracksState
}