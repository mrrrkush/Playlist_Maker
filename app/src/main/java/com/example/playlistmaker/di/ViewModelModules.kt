package com.example.playlistmaker.di

import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.mediateka.favourites.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.mediateka.playlist.view_model.CreatePlaylistViewModel
import com.example.playlistmaker.ui.mediateka.playlist.view_model.DetailPlaylistViewModel
import com.example.playlistmaker.ui.mediateka.playlist.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { (track: Track) ->
        AudioPlayerViewModel(
            track = track,
            playerInteractor = get(),
            favoritesTracksInteractor = get(),
            playlistInteractor = get()
        )
    }
    viewModel { FavoritesViewModel(favoritesTracksInteractor = get()) }
    viewModel { PlaylistViewModel(playlistInteractor = get()) }
    viewModel { CreatePlaylistViewModel(playlistInteractor = get()) }
    viewModel { DetailPlaylistViewModel(playlistInteractor = get()) }
}