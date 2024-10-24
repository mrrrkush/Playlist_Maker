package com.example.playlistmaker.domain.model.playlist

import java.io.Serializable

data class Playlist(
    var id: Long = 0,
    var title: String,
    val description: String,
    val imageUri: String? = null,
    var trackList: String,
    var size: Int,
): Serializable