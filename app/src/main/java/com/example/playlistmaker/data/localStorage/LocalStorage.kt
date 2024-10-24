package com.example.playlistmaker.data.localStorage

import android.net.Uri

interface LocalStorage {
    suspend fun saveImageToPrivateStorage(uri: Uri)
}