package com.example.myapplication.data.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.db.playlist.Playlist
import com.example.myapplication.data.db.playlist.PlaylistDao
import com.example.myapplication.data.db.playlist.PlaylistTrack
import com.example.myapplication.data.db.quiz.QuizDao
import com.example.myapplication.data.db.quiz.Quiz
import com.example.myapplication.data.db.quiz.QuizQuestion

@Database(entities = [Playlist::class, PlaylistTrack::class, Quiz::class, QuizQuestion::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun quizDao(): QuizDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}