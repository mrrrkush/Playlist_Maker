package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.SettingsInteractor

class SettingsInteractorImpl(private val app: App) : SettingsInteractor {
    override var isDarkTheme: Boolean
        get() = app.darkTheme
        set(value) { app.switchTheme(value) }

    override fun switchTheme(isDark: Boolean) = app.switchTheme(isDark)
}