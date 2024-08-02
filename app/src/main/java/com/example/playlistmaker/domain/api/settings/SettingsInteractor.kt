package com.example.playlistmaker.domain.api.settings

interface SettingsInteractor {
    var isDarkTheme: Boolean
    fun switchTheme(isDark: Boolean)
}