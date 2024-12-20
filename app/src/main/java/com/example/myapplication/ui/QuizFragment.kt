package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapters.QuizAdapter
import com.example.myapplication.data.db.quiz.Quiz
import com.example.myapplication.data.db.quiz.QuizWithQuestions
import com.example.myapplication.databinding.FragmentQuizBinding
import com.example.myapplication.mvvm.playlist.PlaylistViewModel
import com.example.myapplication.mvvm.quiz.QuizViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private val playlistViewModel: PlaylistViewModel by viewModels()
    private lateinit var quizAdapter: QuizAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupAddQuizButton()
        observeQuizzes()
        observeErrors()
        quizViewModel.getAllQuizzes()
        playlistViewModel.getAllPlaylists()
    }
    private fun observeErrors() {
        quizViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupRecyclerView() {
        quizAdapter = QuizAdapter(
            onItemClicked = { quiz ->
                showQuizDetails(quiz.quiz.id)
            },
            onItemRemoved = { item ->
                removeQuiz(item)
            }
        )
        binding.quizRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = quizAdapter
        }
    }

    private fun observeQuizzes() {
        quizViewModel.quizzes.observe(viewLifecycleOwner){ quizzes ->
            quizAdapter.submitList(quizzes)
        }
    }
    private fun setupAddQuizButton() {
        binding.addQuizButton.setOnClickListener {
            showAddQuizDialog()
        }
    }
    private fun showAddQuizDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_quiz, null)
        val quizNameEditText = dialogView.findViewById<TextInputEditText>(R.id.quizNameEditText)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add New Quiz")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val quizName = quizNameEditText.text.toString()
                if (quizName.isNotBlank()) {
                    showPlaylistSelectionDialog(quizName)
                } else {
                    Toast.makeText(context, "Quiz name cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun showPlaylistSelectionDialog(quizName:String){
        playlistViewModel.playlists.observe(viewLifecycleOwner){ playlists ->
            if (playlists.isEmpty()){
                Toast.makeText(context, "No playlists available, please create a playlist", Toast.LENGTH_SHORT).show()
            } else{
                val playlistNames = playlists.map { it.playlist.name }.toTypedArray()
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Select a Playlist")
                    .setItems(playlistNames) { _, which ->
                        val selectedPlaylist = playlists[which]
                        createQuiz(quizName, selectedPlaylist.playlist.id)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
        playlistViewModel.getAllPlaylists()
    }
    private fun createQuiz(quizName: String, playlistId: Long){
        quizViewModel.insertQuiz(Quiz(name = quizName, playlistId = playlistId, mode = "multiple_choices"))
        quizViewModel.quizzes.observe(viewLifecycleOwner){ quizzes ->
            val quiz = quizzes.firstOrNull { it.quiz.name == quizName && it.quiz.playlistId == playlistId }
            if(quiz != null){
                showQuizDetails(quiz.quiz.id)
            }
        }
    }
    private fun showQuizDetails(quizId: Long) {
        val quizDetailsFragment = QuizDetailsFragment.newInstance(quizId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, quizDetailsFragment)
            .addToBackStack(null)
            .commit()
    }
    private fun removeQuiz(item: QuizWithQuestions){
        quizViewModel.deleteQuiz(item.quiz.id)
        quizViewModel.getAllQuizzes()
    }

}