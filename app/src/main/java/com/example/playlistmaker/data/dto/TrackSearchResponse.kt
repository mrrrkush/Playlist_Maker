package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

data class TrackSearchResponse(val resultCount: Int, val results: List<TrackDto>): Response()