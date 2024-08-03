package com.example.playlistmaker.domain.model.sharing

data class EmailData(
    val recipient: String,
    val subject: String,
    val body: String
)