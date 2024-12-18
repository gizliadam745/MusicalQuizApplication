package com.example.musicalquiz.ui.quiz

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
import com.example.musicalquiz.data.models.Playlist
import com.example.musicalquiz.data.models.Quiz
import com.example.musicalquiz.databinding.FragmentQuizBinding
import com.example.musicalquiz.viewmodel.PlaylistViewModel
import com.example.musicalquiz.viewmodel.QuizViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val quizViewModel: QuizViewModel by viewModels()
    private val playlistViewModel: PlaylistViewModel by viewModels()
    private lateinit var quizAdapter: QuizAdapter
    private lateinit var playlists: List<Playlist>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.createQuizButton.setOnClickListener {
            showCreateQuizDialog()
        }
        observeQuizzes()
        observePlaylists()

    }
    private fun observeQuizzes(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                quizViewModel.getAllQuizzes().collect{quizzes ->
                    quizAdapter.submitList(quizzes)
                }
            }
        }
    }
    private fun observePlaylists(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                playlistViewModel.getAllPlaylists().collect{
                    playlists = it
                }
            }
        }
    }
    private fun setupRecyclerView() {
        quizAdapter = QuizAdapter{quiz ->
            val quizGameFragment = QuizGameFragment.newInstance(quiz.id)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,quizGameFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.quizRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = quizAdapter
        }
    }
    private fun showCreateQuizDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_quiz, null)
        val quizNameEditText = dialogView.findViewById<TextInputEditText>(R.id.quiz_name_edit_text)
        val playlistNameEditText = dialogView.findViewById<TextInputEditText>(R.id.playlist_name_edit_text)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Create Quiz")
            .setView(dialogView)
            .setPositiveButton("Create") { _, _ ->
                val quizName = quizNameEditText.text.toString()
                val playlistName = playlistNameEditText.text.toString()
                if (quizName.isNotBlank() && playlistName.isNotBlank()){
                    val playlist = playlists.find { it.name == playlistName }
                    if(playlist != null){
                        val quiz = Quiz(name = quizName, playlistId = playlist.id, questions = playlist.tracks)
                        quizViewModel.insertQuiz(quiz)
                    } else {
                        Toast.makeText(requireContext(),"No such playlist $playlistName", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(),"Please enter a quiz name and a playlist name", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}