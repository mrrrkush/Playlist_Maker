package com.example.playlistmaker.domain.impl.settings

import com.example.playlistmaker.data.settings.ThemeManager
import com.example.playlistmaker.domain.api.settings.SettingsInteractor

class SettingsInteractorImpl(private val themeManager: ThemeManager) : SettingsInteractor {

    override var isDarkTheme: Boolean
        get() = themeManager.isDarkTheme()
        set(value) { themeManager.switchTheme(value) }

    override fun switchTheme(isDark: Boolean) = themeManager.switchTheme(isDark)
}