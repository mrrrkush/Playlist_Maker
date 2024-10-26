package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.playlist.PlaylistDbConvertor
import com.example.playlistmaker.data.db.track.TrackDbConvertor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory {
        PlaylistDbConvertor()
    }

    factory {
        TrackDbConvertor()
    }
}