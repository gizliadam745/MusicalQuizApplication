package com.example.myapplication.mvvm.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.db.quiz.Quiz
import com.example.myapplication.data.db.quiz.QuizQuestion
import com.example.myapplication.data.db.quiz.QuizWithQuestions
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuizRepository(application)
    private val _quizzes = MutableLiveData<List<QuizWithQuestions>>()
    val quizzes: LiveData<List<QuizWithQuestions>> = _quizzes
    private val _quiz = MutableLiveData<QuizWithQuestions?>()
    val quiz: MutableLiveData<QuizWithQuestions?> = _quiz
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun insertQuiz(quiz: Quiz) {
        viewModelScope.launch {
            try {
                val quizId = repository.insertQuiz(quiz)
                getQuizWithQuestions(quizId)
            } catch (e: Exception) {
                _error.value = "Error when adding playlist: ${e.message}"
            }
        }
    }
    fun insertQuizQuestion(quizQuestion: QuizQuestion) {
        viewModelScope.launch {
            try {
                repository.insertQuizQuestion(quizQuestion)
            } catch (e: Exception) {
                _error.value = "Error when adding question to quiz: ${e.message}"
            }
        }
    }

    fun getAllQuizzes() {
        viewModelScope.launch {
            try {
                val allQuizzes = repository.getAllQuizzes()
                _quizzes.value = allQuizzes
            } catch (e: Exception) {
                _error.value = "Error when fetching quizzes: ${e.message}"
            }
        }
    }
    fun getQuizWithQuestions(quizId: Long) {
        viewModelScope.launch {
            try {
                val quiz = repository.getQuizWithQuestions(quizId)
                _quiz.value = quiz
            } catch (e: Exception) {
                _error.value = "Error when fetching quiz: ${e.message}"
            }
        }
    }
    fun deleteQuiz(quizId: Long){
        viewModelScope.launch {
            try {
                repository.deleteQuiz(quizId)
            } catch (e: Exception){
                _error.value = "Error when deleting playlist: ${e.message}"
            }
        }
    }
}