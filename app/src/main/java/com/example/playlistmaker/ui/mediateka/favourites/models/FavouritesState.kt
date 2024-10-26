package com.example.playlistmaker.ui.mediateka.favourites.models

import com.example.playlistmaker.domain.model.track.Track

sealed interface FavoritesState {
    data object Empty : FavoritesState
    data class Content(val favorites: List<Track>) : FavoritesState
}