package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesAPI {
    @GET("/search?entity=song")
    fun search(
        @Query("term") text: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song"

    ): Call<TrackSearchResponse>
}

