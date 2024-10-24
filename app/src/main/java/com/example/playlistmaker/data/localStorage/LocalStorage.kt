package com.example.playlistmaker.data.localStorage

interface LocalStorage {
    suspend fun saveImageToPrivateStorage(uri: String)

    fun saveCurrentPlaylistId(id: Long)

    fun getCurrentPlaylistId(): Long
}