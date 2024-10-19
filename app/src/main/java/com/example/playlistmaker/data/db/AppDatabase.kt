package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 2,
    entities = [
        TrackEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun TrackDao(): TrackDao
}

