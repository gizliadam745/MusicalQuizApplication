package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.myapplication.mvvm.search.SearchViewModel
import com.example.myapplication.databinding.FragmentAlbumDetailsBinding

class AlbumDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAlbumDetailsBinding
    private val viewModel: SearchViewModel by viewModels()
    private var albumId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            albumId = it.getLong(ARG_ALBUM_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumId?.let {
            viewModel.fetchAlbum(it)
        }

        observeAlbum()
        observeErrors()
    }
    private fun observeErrors() {
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    private fun observeAlbum(){
        viewModel.album.observe(viewLifecycleOwner){ album ->
            binding.apply {
                albumTitle.text = album.title
                artistName.text = album.artist.name
                releaseDate.text = "Release Date: ${album.release_date}"
                explicitLyrics.text = "Explicit: ${if (album.explicit_lyrics) "Yes" else "No"}"
                contributors.text = "Contributors: ${album.contributors.joinToString { it.name }}"
                nbTracks.text = "Number of Tracks: ${album.nb_tracks}"
                genre.text = "Genre: ${album.genres.data.joinToString { it.name }}"

                Glide.with(this@AlbumDetailsFragment)
                    .load(album.cover_medium)
                    .into(albumCover)

            }
        }
    }

    companion object {
        private const val ARG_ALBUM_ID = "album_id"

        fun newInstance(albumId: Long) = AlbumDetailsFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_ALBUM_ID, albumId)
            }
        }
    }
}