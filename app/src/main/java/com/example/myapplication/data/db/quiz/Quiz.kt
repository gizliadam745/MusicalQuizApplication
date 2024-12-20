package com.example.myapplication.data.db.quiz

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val playlistId: Long,
    val mode: String // To store game mode
)