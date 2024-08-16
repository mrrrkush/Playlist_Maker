package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.Response as RetrofitResponse

class RetrofitNetworkClient(private val apiService: ITunesAPI) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
            try {
                val call: Call<TrackSearchResponse> = apiService.search(dto.expression)
                val response: RetrofitResponse<TrackSearchResponse> = call.execute()
                val body = response.body()

                val result = TrackSearchResponse(
                    resultCount = body?.resultCount ?: 0,
                    results = body?.results ?: emptyList()
                ).apply {
                    resultCode = response.code()
                }

                result
            } catch (e: Exception) {
                Response().apply { resultCode = 500 }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}


