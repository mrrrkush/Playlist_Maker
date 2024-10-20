package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.audioplayer.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.mediateka.FavouriteTracksRepositoryImpl
import com.example.playlistmaker.data.network.ITunesAPI
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.SearchRepositoryImpl
import com.example.playlistmaker.data.searchHistory.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.searchHistory.SearchHistoryStorage
import com.example.playlistmaker.data.searchHistory.SharedPrefsHistoryStorage
import com.example.playlistmaker.data.settings.ThemeManager
import com.example.playlistmaker.data.db.DbConverter
import com.example.playlistmaker.data.localStorage.LocalStorage
import com.example.playlistmaker.data.localStorage.LocalStorageImpl
import com.example.playlistmaker.data.playlist.PlaylistRepositoryImpl
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerRepository
import com.example.playlistmaker.domain.api.mediateka.FavouriteTracksRepository
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
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
        DbConverter()
    }

    single { ThemeManager(get()) }
    single<AudioPlayerRepository> { AudioPlayerRepositoryImpl(get()) }
    single<SearchRepository> { SearchRepositoryImpl(networkClient = get(), storage = get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get(), get()) }
    single<SearchHistoryStorage> { SharedPrefsHistoryStorage(sharedPrefs = get()) }
    single<FavouriteTracksRepository> { FavouriteTracksRepositoryImpl(appDatabase = get(), trackDbConvertor = get()) }
    single<PlaylistRepository> { PlaylistRepositoryImpl(appDatabase = get(), dbConvertor = get(), localStorage = get()) }
    single<LocalStorage> { LocalStorageImpl(context = get()) }
}