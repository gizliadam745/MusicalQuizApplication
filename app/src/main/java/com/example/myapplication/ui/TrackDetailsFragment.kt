package com.example.myapplication.ui

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.myapplication.mvvm.search.SearchViewModel
import com.example.myapplication.databinding.FragmentTrackDetailsBinding


class TrackDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTrackDetailsBinding
    private val viewModel: SearchViewModel by viewModels()
    private var trackId: Long? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trackId = it.getLong(ARG_TRACK_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TrackID", "Track ID: $trackId")
        trackId?.let {
            viewModel.fetchTrack(it)
        }

        observeTrack()
        observeErrors()
        setupPreviewButton()
    }
    private fun observeErrors() {
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    private fun observeTrack() {
        viewModel.track.observe(viewLifecycleOwner) { track ->
            binding.apply {
                trackTitle.text = track.title
                artistName.text = track.artist.name
                albumName.text = track.album.title
                duration.text = "Duration: ${track.duration}"
                bpm.text = "BPM: ${track.bpm}"
                releaseDate.text = "Release Date: ${track.release_date}"
                explicitLyrics.text = "Explicit: ${if (track.explicit_lyrics) "Yes" else "No"}"
                contributors.text = "Contributors: ${track.contributors.joinToString { it.name }}"

                Glide.with(this@TrackDetailsFragment)
                    .load(track.album.cover)
                    .into(trackCover)
            }
        }
    }
    private fun setupPreviewButton() {
        binding.playPreviewButton.setOnClickListener {
            viewModel.track.value?.let{ track ->
                playPreview(track.preview)
            }
        }
    }
    private fun playPreview(previewUrl: String) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            try {
                setDataSource(previewUrl)
                prepareAsync()
                setOnPreparedListener {
                    start()
                }
                setOnCompletionListener {
                    releaseMediaPlayer()
                }

            } catch (e: Exception) {
                Toast.makeText(context, "Error playing preview: ${e.message}", Toast.LENGTH_SHORT).show()
                releaseMediaPlayer()
            }
        }
    }
    override fun onPause() {
        super.onPause()
        releaseMediaPlayer()
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
    override fun onDestroyView() {
        super.onDestroyView()
        releaseMediaPlayer()
    }
    companion object {
        private const val ARG_TRACK_ID = "track_id"

        fun newInstance(trackId: Long) = TrackDetailsFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_TRACK_ID, trackId)
            }
        }
    }
}