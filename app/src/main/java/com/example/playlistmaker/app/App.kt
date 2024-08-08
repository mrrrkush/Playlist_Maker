package com.example.playlistmaker.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.audioplayer.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.TrackRepositoryImpl
import com.example.playlistmaker.data.searchHistory.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerRepository
import com.example.playlistmaker.domain.api.search.TrackInteractor
import com.example.playlistmaker.domain.api.search.TrackRepository
import com.example.playlistmaker.domain.api.searchHistory.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.searchHistory.SearchHistoryRepository
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import com.example.playlistmaker.domain.api.sharing.SharingInteractor
import com.example.playlistmaker.domain.impl.audioplayer.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.search.TrackInteractorImpl
import com.example.playlistmaker.domain.impl.searchHistory.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.sharing.SharingInteractorImpl

class App : Application() {

    private lateinit var sharedPreferences: SharedPreferences
    private val audioPlayerRepository: AudioPlayerRepository by lazy {
        AudioPlayerRepositoryImpl()
    }
    private val audioPlayerInteractor: AudioPlayerInteractor by lazy {
        AudioPlayerInteractorImpl(audioPlayerRepository)
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        updateTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean("darkTheme", darkThemeEnabled)
            .apply()
        updateTheme()
    }

    private fun updateTheme() {
        val darkTheme = sharedPreferences.getBoolean("darkTheme", false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean("darkTheme", false)
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(this)
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(ExternalNavigator(this))
    }
    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(provideTrackRepository())
    }

    private fun provideTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository())
    }

    private fun provideSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPreferences)
    }
    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return audioPlayerInteractor
    }
}