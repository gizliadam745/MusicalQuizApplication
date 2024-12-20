package com.example.myapplication.data.db.quiz

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class QuizWithQuestions(
    @Embedded val quiz: Quiz,
    @Relation(
        parentColumn = "id",
        entityColumn = "quizId",
        associateBy = Junction(QuizQuestion::class)
    )
    val questions: List<QuizQuestion>
)