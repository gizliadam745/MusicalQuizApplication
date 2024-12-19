package com.example.musicalquiz.data

import com.example.musicalquiz.data.Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.musicalquiz.data.models.Album
import com.example.musicalquiz.data.models.Playlist
import com.example.musicalquiz.data.models.Quiz
import com.example.musicalquiz.data.models.Track
import com.example.musicalquiz.data.models.Artist

@Database(entities = [Track::class, Playlist::class, Quiz::class, Album::class, Artist::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun quizDao(): QuizDao
}