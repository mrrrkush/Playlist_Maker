package com.example.playlistmaker.domain.model.playlist

import android.net.Uri

data class Playlist(
    val id: Long = 0,
    val title: String,
    val description: String,
    val imageUri: Uri? = null,
    var trackList: String,
    var size: Int,
)