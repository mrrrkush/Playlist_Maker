package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, viewModelModule)
        }
        val settingsInteractor: SettingsInteractor = getKoin().get()
        AppCompatDelegate.setDefaultNightMode(
            if (settingsInteractor.isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}