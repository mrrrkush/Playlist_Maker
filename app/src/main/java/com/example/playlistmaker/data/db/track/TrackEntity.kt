package com.example.playlistmaker.data.db.track

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.data.db.track.TrackEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class TrackEntity(
    @PrimaryKey
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl60: String,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?,
    val favouriteAddedTimestamp: Long
) {
    companion object {
        const val TABLE_NAME = "track_table"
    }
}