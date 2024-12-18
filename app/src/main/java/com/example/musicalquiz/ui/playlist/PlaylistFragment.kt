package com.example.musicalquiz.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicalquiz.R
import com.example.musicalquiz.data.models.Playlist
import com.example.musicalquiz.databinding.FragmentPlaylistBinding
import com.example.musicalquiz.viewmodel.PlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModels()
    private lateinit var playlistAdapter: PlaylistAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.createPlaylistButton.setOnClickListener {
            showCreatePlaylistDialog()
        }
        observePlaylists()
    }
    private fun observePlaylists(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.getAllPlaylists().collect{playlists ->
                    playlistAdapter.submitList(playlists)
                }
            }
        }
    }
    private fun setupRecyclerView() {
        playlistAdapter = PlaylistAdapter{playlist ->
            showPlaylistDetailsDialog(playlist)
        }
        binding.playlistRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playlistAdapter
        }
    }
    private fun showCreatePlaylistDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_playlist, null)
        val playlistNameEditText = dialogView.findViewById<TextInputEditText>(R.id.playlist_name_edit_text)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Create Playlist")
            .setView(dialogView)
            .setPositiveButton("Create") { _, _ ->
                val playlistName = playlistNameEditText.text.toString()
                if(playlistName.isNotBlank()){
                    val playlist = Playlist(name = playlistName, tracks = emptyList())
                    viewModel.insertPlaylist(playlist)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun showPlaylistDetailsDialog(playlist: Playlist) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(playlist.name)
            .setMessage("Playlist tracks: \n${playlist.tracks}")
            .setPositiveButton("OK", null)
            .show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}