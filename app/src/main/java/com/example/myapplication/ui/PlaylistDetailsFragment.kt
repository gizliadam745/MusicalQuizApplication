package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapters.HorizontalTrackAdapter
import com.example.myapplication.R
import com.example.myapplication.data.db.playlist.PlaylistTrack
import com.example.myapplication.data.models.Track
import com.example.myapplication.mvvm.playlist.PlaylistViewModel
import com.example.myapplication.databinding.FragmentPlaylistDetailsBinding
import com.example.myapplication.mvvm.search.SearchViewModel

class PlaylistDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistDetailsBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val playlistViewModel: PlaylistViewModel by viewModels()
    private var playlistId: Long? = null
    private lateinit var trackAdapter: HorizontalTrackAdapter
    private val trackDetailsMap = mutableMapOf<Long, Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlistId = it.getLong(ARG_PLAYLIST_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observePlaylists()
        observeErrors()
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
        trackAdapter = HorizontalTrackAdapter(
            onTrackClicked = { track ->
                showTrackDetails(track)
            },
            onItemRemoved = { item ->
                removeTrackFromPlaylist(item)
            }
        )
        binding.trackRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = trackAdapter
        }
    }

    private fun observePlaylists() {
        playlistViewModel.playlists.observe(viewLifecycleOwner){ playlists ->
            val currentPlaylist = playlists.firstOrNull{ it.playlist.id == playlistId}
            currentPlaylist?.let{
                binding.playlistName.text = it.playlist.name
                val tracks = it.tracks
                val tracksWithDetails = tracks.map{ track ->
                    track to trackDetailsMap[track.trackId]
                }
                trackAdapter.submitList(tracksWithDetails)
                fetchTrackDetails(tracks)
            }
        }
        playlistViewModel.getAllPlaylists()

    }

    private fun fetchTrackDetails(tracks:List<PlaylistTrack>){
        for (track in tracks){
            searchViewModel.fetchTrack(track.trackId)
//            searchViewModel.track.observe(viewLifecycleOwner){ trackDetails ->
//                if(trackDetails != null) {
//                    updateAdapter(track, trackDetails)
//                }
//            }
            searchViewModel.track.observe(viewLifecycleOwner){ trackDetails ->
                if(trackDetails != null) {
                    trackDetailsMap[trackDetails.id] = trackDetails
                    val tracksWithDetails = tracks.map{ track ->
                        track to trackDetailsMap[track.trackId]
                    }
                    trackAdapter.submitList(tracksWithDetails)
                }
            }

        }
    }
    private fun updateAdapter(track: PlaylistTrack, trackDetails: Track) {
        val currentList = trackAdapter.currentList.toMutableList()
        val index = currentList.indexOfFirst { it.first.trackId == track.trackId }

        if(index != -1) {
            currentList[index] = track to trackDetails
            trackAdapter.submitList(currentList.toList())
        }
    }
    private fun showTrackDetails(track:Track){
        val detailsFragment = TrackDetailsFragment.newInstance(track.id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailsFragment)
            .addToBackStack(null)
            .commit()
    }
    private fun removeTrackFromPlaylist(track: PlaylistTrack) {
        Toast.makeText(context, "Remove track: ${track.trackId}", Toast.LENGTH_SHORT).show()
        playlistViewModel.removeTrackFromPlaylist(track.playlistId, track.trackId)
        updatePlaylistData()
    }
    private fun updatePlaylistData(){
        playlistId?.let {
            playlistViewModel.getPlaylistWithTracks(it)
            playlistViewModel.playlist.observe(viewLifecycleOwner){ playlist ->
                if (playlist != null){
                    binding.playlistName.text = playlist.playlist.name
                    val tracks = playlist.tracks
                    val tracksWithDetails = tracks.map{ track ->
                        track to null
                    }
                    trackAdapter.submitList(tracksWithDetails)
                    fetchTrackDetails(tracks)
                }
            }
        }
    }
    companion object {
        private const val ARG_PLAYLIST_ID = "playlist_id"

        fun newInstance(playlistId: Long) = PlaylistDetailsFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_PLAYLIST_ID, playlistId)
            }
        }
    }
}