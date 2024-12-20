package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapters.PlaylistAdapter
import com.example.myapplication.R
import com.example.myapplication.data.db.playlist.Playlist
import com.example.myapplication.data.db.playlist.PlaylistWithTracks
import com.example.myapplication.mvvm.playlist.PlaylistViewModel
import com.example.myapplication.databinding.FragmentPlaylistBinding
import com.example.myapplication.mvvm.search.SearchViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class PlaylistFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val playlistViewModel: PlaylistViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupAddPlaylistButton()
        observePlaylists()
        observeErrors()
        playlistViewModel.getAllPlaylists()
    }

    private fun observeErrors() {
        searchViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }

        playlistViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupRecyclerView() {
        playlistAdapter = PlaylistAdapter(
            onItemClicked = { playlist ->
                showPlaylistDetails(playlist.playlist.id)
            },
            onItemRemoved = { item ->
                removePlaylist(item)
            }
        )
        binding.playlistRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playlistAdapter
        }
    }
    private fun observePlaylists(){
        playlistViewModel.playlists.observe(viewLifecycleOwner){ playlists ->
            playlistAdapter.submitList(playlists)
        }
    }
    private fun setupAddPlaylistButton(){
        binding.addPlaylistButton.setOnClickListener{
            showAddPlaylistDialog()
        }
    }
    private fun showAddPlaylistDialog(){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_playlist, null)
        val playlistNameEditText = dialogView.findViewById<TextInputEditText>(R.id.playlistNameEditText)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add New Playlist")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val playlistName = playlistNameEditText.text.toString()
                if (playlistName.isNotBlank()) {
                    playlistViewModel.insertPlaylist(Playlist(name = playlistName))
                    playlistViewModel.getAllPlaylists()
//                    playlistViewModel.playlists.observe(viewLifecycleOwner){ playlists ->
//                        val playlist = playlists.firstOrNull{it.playlist.name == playlistName}
//                        if (playlist != null){
//                            showPlaylistDetails(playlist.playlist.id)
//                        }
//                    }
                } else {
                    Toast.makeText(context, "Playlist name cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun showPlaylistDetails(playlistId: Long){
        val playlistDetailsFragment = PlaylistDetailsFragment.newInstance(playlistId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, playlistDetailsFragment)
            .addToBackStack(null)
            .commit()
    }
    private fun removePlaylist(item: PlaylistWithTracks){
        playlistViewModel.deletePlaylist(item.playlist.id)
        playlistViewModel.getAllPlaylists()
    }
}