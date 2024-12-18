package com.example.musicalquiz.ui.quiz

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicalquiz.R
import com.example.musicalquiz.data.RetrofitClient
import com.example.musicalquiz.data.models.Quiz
import com.example.musicalquiz.data.models.Track
import com.example.musicalquiz.databinding.FragmentQuizGameBinding
import com.example.musicalquiz.viewmodel.QuizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizGameFragment : Fragment() {
    private var _binding: FragmentQuizGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuizViewModel by viewModels()
    private lateinit var quizChoiceAdapter: QuizChoiceAdapter
    private var quizId: Int = -1
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var quiz: Quiz
    private lateinit var tracks: List<Track>
    private var currentQuestionIndex: Int = 0

    companion object {
        private const val ARG_QUIZ_ID = "quizId"
        fun newInstance(quizId: Int): QuizGameFragment {
            val fragment = QuizGameFragment()
            val args = Bundle()
            args.putInt(ARG_QUIZ_ID, quizId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            quizId = it.getInt(ARG_QUIZ_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadQuizDetails()
    }

    private fun loadQuizDetails() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getQuizById(quizId).collect {
                    quiz = it
                    loadTracksForQuiz()
                }
            }
        }
    }

    private fun loadTracksForQuiz() {
        lifecycleScope.launch {
            tracks = withContext(Dispatchers.IO) {
                quiz.questions.map {
                    RetrofitClient.api.getTrackDetails(it)
                }
            }
            startQuiz()
        }
    }

    private fun startQuiz() {
        currentQuestionIndex = 0
        binding.quizQuestionTextView.text = getString(R.string.guess_song)
        setupRecyclerView()
        playPreview()
    }

    private fun playPreview() {
        if (currentQuestionIndex >= tracks.size) {
            Toast.makeText(requireContext(), "Quiz finished!", Toast.LENGTH_SHORT).show()
            return
        }
        val previewUrl = tracks[currentQuestionIndex].preview
        if (previewUrl.isNotBlank()) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                setDataSource(previewUrl)
                prepareAsync()
                setOnPreparedListener {
                    start()
                }
                setOnCompletionListener {
                    release()
                    mediaPlayer = null
                    onPreviewFinished()
                }
                setOnErrorListener { _, _, _ ->
                    Toast.makeText(requireContext(), "Unable to play preview", Toast.LENGTH_SHORT)
                        .show()
                    true
                }
            }
        } else {
            Toast.makeText(requireContext(), "No preview available", Toast.LENGTH_SHORT).show()
            onPreviewFinished() //Move to next question even if no preview
        }
    }

    private fun onPreviewFinished(){
        currentQuestionIndex++
        if (currentQuestionIndex < tracks.size){
            playPreview()
        } else {
            Toast.makeText(requireContext(), "Quiz finished!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupRecyclerView() {
        quizChoiceAdapter = QuizChoiceAdapter(tracks) {
            onChoiceSelected(it)
        }
        binding.choicesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = quizChoiceAdapter
        }
    }
    private fun onChoiceSelected(track:Track) {
        Toast.makeText(requireContext(),"Correct answer: ${track.title}", Toast.LENGTH_SHORT).show()
        onPreviewFinished()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }
}