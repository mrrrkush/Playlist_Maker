package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse

class RetrofitNetworkClient(private val apiService: ITunesAPI) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
            try {
                val response: TrackSearchResponse = apiService.search(dto.expression)
                TrackSearchResponse(
                    resultCount = response.resultCount,
                    results = response.results
                ).apply {
                    resultCode = 200
                }
            } catch (e: Exception) {
                Response().apply { resultCode = 500 }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}


