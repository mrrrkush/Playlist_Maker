package com.example.playlistmaker.data.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class ThemeManager(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val DARK_THEME_KEY = "darkTheme"
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME_KEY, darkThemeEnabled)
            .apply()
        updateTheme()
    }

    private fun updateTheme() {
        val darkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, false)
    }
}

