package com.example.playlistmaker.data.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class ThemeManager(private val sharedPreferences: SharedPreferences) {

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
}
