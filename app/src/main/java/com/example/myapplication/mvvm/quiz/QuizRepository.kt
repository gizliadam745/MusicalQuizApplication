package com.example.myapplication.mvvm.quiz

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.myapplication.data.db.AppDatabase
import com.example.myapplication.data.db.quiz.Quiz
import com.example.myapplication.data.db.quiz.QuizQuestion

class QuizRepository(context: Context) {
    private val quizDao = AppDatabase.getDatabase(context).quizDao()

    suspend fun insertQuiz(quiz: Quiz): Long{
        return withContext(Dispatchers.IO){
            quizDao.insertQuiz(quiz)
        }
    }
    suspend fun insertQuizQuestion(quizQuestion: QuizQuestion){
        withContext(Dispatchers.IO){
            quizDao.insertQuizQuestion(quizQuestion)
        }
    }
    suspend fun getAllQuizzes() = withContext(Dispatchers.IO){
        quizDao.getAllQuizzes()
    }
    suspend fun getQuizWithQuestions(quizId: Long) = withContext(Dispatchers.IO){
        quizDao.getQuizWithQuestions(quizId)
    }
    suspend fun deleteQuiz(quizId: Long) {
        withContext(Dispatchers.IO){
            quizDao.deleteQuiz(quizId)
            quizDao.deleteQuizQuestions(quizId)
        }
    }
}