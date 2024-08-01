package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.SettingsInteractor
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl

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
