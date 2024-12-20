package com.example.myapplication.ui

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myapplication.data.db.playlist.PlaylistTrack
import com.example.myapplication.data.db.quiz.QuizQuestion
import com.example.myapplication.data.models.Track
import com.example.myapplication.databinding.FragmentQuizPlayBinding
import com.example.myapplication.mvvm.playlist.PlaylistViewModel
import com.example.myapplication.mvvm.quiz.QuizViewModel
import com.example.myapplication.mvvm.search.SearchViewModel
import kotlin.math.min

class QuizPlayFragment : Fragment() {

    private lateinit var binding: FragmentQuizPlayBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val playlistViewModel: PlaylistViewModel by viewModels()
    private var quizId: Long? = null
    private var currentQuestionIndex = 0
    private var questions: List<QuizQuestion> = emptyList()
    private var mediaPlayer: MediaPlayer? = null
    private var timer: CountDownTimer? = null
    private var isPlaying = false
    private var correctAnswers = 0
    private val timeLimit = 10000L //10 seconds for each question

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
        binding = FragmentQuizPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeQuiz()
        observeErrors()
        setupNextButton()
    }
    private fun observeErrors() {
        searchViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
        quizViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }
    private fun observeQuiz() {
        quizId?.let {
            quizViewModel.getQuizWithQuestions(it)
            quizViewModel.quiz.observe(viewLifecycleOwner) { quiz ->
                if (quiz != null) {
                    playlistViewModel.getPlaylistWithTracks(quiz.quiz.playlistId)
                    playlistViewModel.playlist.observe(viewLifecycleOwner){ playlist ->
                        if(playlist != null){
                            val tracks = playlist.tracks
                            if (quiz.questions.isNotEmpty()){
                                this.questions = quiz.questions
                            }
                            else{
                                this.questions =  generateRandomQuizQuestions(tracks, quiz.quiz.id)
                            }
                            if (questions.isNotEmpty()){
                                loadQuestion()
                            }
                            else{
                                Toast.makeText(context, "No tracks to quiz with", Toast.LENGTH_LONG).show()
                            }
                        }

                    }
                }

            }
        }
    }
    private fun generateRandomQuizQuestions(tracks: List<PlaylistTrack>, quizId: Long): List<QuizQuestion> {
        val numQuestions = min(5, tracks.size)
        return tracks.shuffled().take(numQuestions).map { track ->
            QuizQuestion(quizId = quizId, trackId = track.trackId, isRandom = true)
        }

    }
    private fun loadQuestion() {
        if (currentQuestionIndex < questions.size){
            val currentQuestion = questions[currentQuestionIndex]
            searchViewModel.fetchTrack(currentQuestion.trackId)
            searchViewModel.track.observe(viewLifecycleOwner){ track ->
                if (track != null){
                    playTrack(track)
                    binding.trackTitleTextView.text = track.title
                    binding.artistNameTextView.text = track.artist.name
                    setupMultipleChoices(track)
                    startTimer()
                }
            }
        } else{
            showResults()
        }
    }
    private fun playTrack(track: Track) {
        releaseMediaPlayer()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            try {
                setDataSource(track.preview)
                prepareAsync()
                setOnPreparedListener {
                    start()
                    this@QuizPlayFragment.isPlaying = true
                }
                setOnCompletionListener {
                    releaseMediaPlayer()
                    this@QuizPlayFragment.isPlaying = false
                }


            } catch (e: Exception) {
                Toast.makeText(context, "Error playing preview: ${e.message}", Toast.LENGTH_SHORT).show()
                releaseMediaPlayer()
                this@QuizPlayFragment.isPlaying = false
            }
        }
    }
    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(timeLimit, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timerTextView.text = "${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                releaseMediaPlayer()
                binding.timerTextView.text = "Time Over!"
                checkAnswer()
            }
        }
        timer?.start()
    }
    private fun setupMultipleChoices(track:Track){
        val options = mutableListOf(track.title)
        val usedTrackIds = mutableListOf<Long>()
        usedTrackIds.add(track.id)
        while (options.size < 4){
            searchViewModel.searchItems("random", com.example.myapplication.SearchType.TRACK)
            searchViewModel.searchResults.observe(viewLifecycleOwner){ searchResults ->
                if(searchResults != null){
                    val resultTrack = searchResults.firstOrNull{ it is Track && !usedTrackIds.contains((it.id))} as? Track
                    if (resultTrack?.title != null){
                        options.add(resultTrack.title)
                        usedTrackIds.add(resultTrack.id)
                    }

                }
            }
        }
        options.shuffle()
        binding.option1Button.text = options[0]
        binding.option2Button.text = options[1]
        binding.option3Button.text = options[2]
        binding.option4Button.text = options[3]
    }

    private fun setupNextButton(){
        binding.option1Button.setOnClickListener {
            checkAnswer(binding.option1Button.text.toString())
        }
        binding.option2Button.setOnClickListener {
            checkAnswer(binding.option2Button.text.toString())
        }
        binding.option3Button.setOnClickListener {
            checkAnswer(binding.option3Button.text.toString())
        }
        binding.option4Button.setOnClickListener {
            checkAnswer(binding.option4Button.text.toString())
        }
    }
    private fun checkAnswer(selectedAnswer: String? = null) {
        timer?.cancel()
        val currentQuestion = questions[currentQuestionIndex]
        searchViewModel.fetchTrack(currentQuestion.trackId)
        searchViewModel.track.observe(viewLifecycleOwner){ track ->
            if (track != null){
                if (selectedAnswer == track.title){
                    correctAnswers++
                }
            }
            currentQuestionIndex++
            loadQuestion()
        }


    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
    }
    private fun showResults() {
        releaseMediaPlayer()
        binding.timerTextView.text = "Quiz completed!"
        Toast.makeText(context, "Quiz completed, you scored: $correctAnswers", Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        releaseMediaPlayer()
        timer?.cancel()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        releaseMediaPlayer()
        timer?.cancel()
    }
    companion object {
        private const val ARG_QUIZ_ID = "quiz_id"

        fun newInstance(quizId: Long) = QuizPlayFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_QUIZ_ID, quizId)
            }
        }
    }
}