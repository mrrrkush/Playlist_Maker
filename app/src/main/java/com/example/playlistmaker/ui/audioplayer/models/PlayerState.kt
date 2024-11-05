package com.example.playlistmaker.ui.audioplayer.models

sealed class PlayerState(
    val isPlayButtonEnabled: Boolean,
    val buttonIsPlay: Boolean,
    val progress: Int
) {
    data object Default : PlayerState(false, true, 0)
    data object Prepared : PlayerState(true, true, 0)
    class Playing(progress: Int) : PlayerState(true, false, progress)
    class Paused(progress: Int) : PlayerState(true, true, progress)
}