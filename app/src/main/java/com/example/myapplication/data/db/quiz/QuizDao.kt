package com.example.myapplication.data.db.quiz

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface QuizDao {
    @Insert
    suspend fun insertQuiz(quiz: Quiz): Long

    @Insert
    suspend fun insertQuizQuestion(quizQuestion: QuizQuestion)

    @Transaction
    @Query("SELECT * FROM quizzes")
    suspend fun getAllQuizzes(): List<QuizWithQuestions>

    @Transaction
    @Query("SELECT * FROM quizzes WHERE id = :quizId")
    suspend fun getQuizWithQuestions(quizId: Long): QuizWithQuestions?

    @Query("DELETE FROM quizzes WHERE id = :quizId")
    suspend fun deleteQuiz(quizId: Long)
    @Query("DELETE FROM quiz_questions WHERE quizId = :quizId")
    suspend fun deleteQuizQuestions(quizId: Long)

}