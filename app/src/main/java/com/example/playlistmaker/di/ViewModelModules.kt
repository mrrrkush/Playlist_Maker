package com.example.playlistmaker.di

import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.mediateka.view_model.BasePlaylistViewModel
import com.example.playlistmaker.ui.mediateka.view_model.FavouriteTrackViewModel
import com.example.playlistmaker.ui.mediateka.view_model.OpenPlaylistViewModel
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { AudioPlayerViewModel(favouriteTracksInteractor = get(), playerInteractor = get(), playlistInteractor = get()) }
    viewModel { FavouriteTrackViewModel(favouriteTracksInteractor = get()) }
    viewModel { BasePlaylistViewModel(playlistInteractor = get()) }
    viewModel { PlaylistViewModel(playlistInteractor = get()) }
    viewModel { OpenPlaylistViewModel(playlistInteractor = get(), sharingInteractor = get()) }
}