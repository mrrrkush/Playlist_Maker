package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.data.searchHistory.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.track.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.searchHistory.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.searchHistory.SearchHistoryRepository
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.api.track.TrackRepository
import com.example.playlistmaker.domain.impl.searchHistory.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.track.TrackInteractorImpl

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }
    private fun getSearchHistoryRepository(sharedPref: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPref)
    }
    fun provideSearchHistoryInteractor(sharedPref: SharedPreferences): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(sharedPref))
    }
    fun provideSettingsInteractor(app: App): SettingsInteractor {
        return SettingsInteractorImpl(app)
    }
}
