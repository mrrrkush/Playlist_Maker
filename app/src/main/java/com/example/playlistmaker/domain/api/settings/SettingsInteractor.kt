package com.example.playlistmaker.domain.api.settings

interface SettingsInteractor {
    val isDarkTheme: Boolean
    fun switchTheme(isDark: Boolean)
}
