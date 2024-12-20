package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentQuizDetailsBinding
import com.example.myapplication.mvvm.playlist.PlaylistViewModel
import com.example.myapplication.mvvm.quiz.QuizViewModel

class QuizDetailsFragment : Fragment() {

    private lateinit var binding: FragmentQuizDetailsBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private val playlistViewModel: PlaylistViewModel by viewModels()

    private var quizId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            quizId = it.getLong(ARG_QUIZ_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeQuiz()
        setupPlayButton()
        observeErrors()
    }
    private fun observeErrors() {
        quizViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    private fun observeQuiz() {
        quizId?.let {
            quizViewModel.getQuizWithQuestions(it)
            quizViewModel.quiz.observe(viewLifecycleOwner){ quiz ->
                if(quiz != null){
                    binding.quizNameTextView.text = quiz.quiz.name
                    playlistViewModel.getPlaylistWithTracks(quiz.quiz.playlistId)
                    playlistViewModel.playlist.observe(viewLifecycleOwner){ playlist ->
                        if (playlist != null){
                            binding.playlistNameTextView.text = playlist.playlist.name
                        }
                    }

                }
            }
        }

    }
    private fun setupPlayButton() {
        binding.playQuizButton.setOnClickListener {
            quizId?.let {
                showQuizPlayFragment(it)
            }

        }
    }
    private fun showQuizPlayFragment(quizId:Long){
        val quizPlayFragment = QuizPlayFragment.newInstance(quizId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, quizPlayFragment)
            .addToBackStack(null)
            .commit()
    }
    companion object {
        private const val ARG_QUIZ_ID = "quiz_id"

        fun newInstance(quizId: Long) = QuizDetailsFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_QUIZ_ID, quizId)
            }
        }
    }
}