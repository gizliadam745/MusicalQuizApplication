package com.example.musicalquiz.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.musicalquiz.data.AppDatabase
import com.example.musicalquiz.data.models.Quiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "musical_quiz_db"
    ).build()
    private val quizDao = db.quizDao()

    fun getAllQuizzes(): Flow<List<Quiz>> {
        return quizDao.getAllQuizzes()
    }
    fun getQuizById(quizId: Int): Flow<Quiz> {
        return quizDao.getQuizById(quizId)
    }
    fun insertQuiz(quiz: Quiz) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                quizDao.insertQuiz(quiz)
            }
        }
    }
}