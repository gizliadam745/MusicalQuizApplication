package com.example.musicalquiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicalquiz.data.models.Quiz
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: Quiz)

    @Query("SELECT * FROM quizzes")
    fun getAllQuizzes(): Flow<List<Quiz>>
    @Query("SELECT * FROM quizzes WHERE id = :quizId")
    fun getQuizById(quizId:Int): Flow<Quiz>
}