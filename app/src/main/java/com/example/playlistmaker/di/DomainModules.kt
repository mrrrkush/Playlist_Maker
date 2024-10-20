package com.example.playlistmaker.di

import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.mediateka.FavouriteTracksInteractor
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.api.searchHistory.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import com.example.playlistmaker.domain.api.sharing.SharingInteractor
import com.example.playlistmaker.domain.impl.audioplayer.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.mediateka.FavouriteTracksInteractorImpl
import com.example.playlistmaker.domain.impl.playlist.PlaylistInteractorImpl
import com.example.playlistmaker.domain.impl.search.SearchInteractorImpl
import com.example.playlistmaker.domain.impl.searchHistory.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.sharing.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainModule = module {
    single { ExternalNavigator(androidContext()) }

    factory<AudioPlayerInteractor> { AudioPlayerInteractorImpl(get()) }
    factory<SettingsInteractor> { SettingsInteractorImpl(get()) }
    factory<SharingInteractor> { SharingInteractorImpl(get()) }
    single<SearchInteractor> { SearchInteractorImpl(repository = get()) }
    factory<SearchHistoryInteractor> { SearchHistoryInteractorImpl(get()) }
    single<FavouriteTracksInteractor> { FavouriteTracksInteractorImpl(favouriteTracksRepository = get()) }
    single<PlaylistInteractor> { PlaylistInteractorImpl(playlistRepository = get()) }
}