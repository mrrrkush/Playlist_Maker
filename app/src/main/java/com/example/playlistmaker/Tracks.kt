package com.example.playlistmaker

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)
data class SearchResponse(val resultCount: Int, val results: List<Track>)

