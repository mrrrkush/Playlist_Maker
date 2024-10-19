package com.example.playlistmaker.di

import com.example.playlistmaker.db.TrackDbConverter
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.mediateka.view_model.FavouriteTrackViewModel
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { AudioPlayerViewModel(favouriteTracksInteractor = get(), playerInteractor = get()) }
    viewModel { FavouriteTrackViewModel(favouriteTracksInteractor = get()) }
    viewModel { PlaylistsViewModel() }
}