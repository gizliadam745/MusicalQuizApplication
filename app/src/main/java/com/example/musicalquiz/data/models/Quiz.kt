package com.example.musicalquiz.data.models
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val playlistId: Int,
    val questions: List<Long>
)