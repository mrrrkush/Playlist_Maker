package com.example.playlistmaker.domain.api

interface SettingsInteractor {
    var isDarkTheme: Boolean
    fun switchTheme(isDark: Boolean)
}