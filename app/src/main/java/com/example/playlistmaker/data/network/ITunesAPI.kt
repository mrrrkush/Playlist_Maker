package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song")
    suspend fun search(
        @Query("term") text: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song"
    ): TrackSearchResponse
}

