package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.audioplayer.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.db.playlist.PlaylistDbConvertor
import com.example.playlistmaker.data.db.track.TrackDbConvertor
import com.example.playlistmaker.data.mediateka.favourites.FavoritesTracksRepositoryImpl
import com.example.playlistmaker.data.mediateka.playlist.PlaylistRepositoryImpl
import com.example.playlistmaker.data.network.ITunesAPI
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.SearchRepositoryImpl
import com.example.playlistmaker.data.searchHistory.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.searchHistory.SearchHistoryStorage
import com.example.playlistmaker.data.searchHistory.SharedPrefsHistoryStorage
import com.example.playlistmaker.data.settings.ThemeManager
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerRepository
import com.example.playlistmaker.domain.api.mediateka.favourites.FavoritesTracksRepository
import com.example.playlistmaker.domain.api.mediateka.playlist.PlaylistRepository
import com.example.playlistmaker.domain.api.search.SearchRepository
import com.example.playlistmaker.domain.api.searchHistory.SearchHistoryRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single { MediaPlayer() }

    single<ITunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPI::class.java)
    }

    single<NetworkClient> { RetrofitNetworkClient(get()) }

    factory {
        PlaylistDbConvertor()
    }

    factory {
        TrackDbConvertor()
    }

    single { ThemeManager(get()) }
    single<AudioPlayerRepository> { AudioPlayerRepositoryImpl(get()) }
    single<SearchRepository> { SearchRepositoryImpl(networkClient = get(), storage = get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get(), get()) }
    single<SearchHistoryStorage> { SharedPrefsHistoryStorage(sharedPrefs = get()) }
    single<FavoritesTracksRepository> { FavoritesTracksRepositoryImpl(dataBase = get(), trackDbConvertor = get()) }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            dataBase = get(),
            playlistDbConvertor = get(),
            trackDbConvertor = get(),
            externalNavigator = get(),
            context = androidContext()
        )
    }
}