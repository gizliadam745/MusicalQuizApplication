package com.example.musicalquiz.ui.details
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.musicalquiz.data.RetrofitClient
import com.example.musicalquiz.databinding.FragmentDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private  var trackId:Long = -1
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        private const val ARG_TRACK_ID = "trackId"

        fun newInstance(trackId: Long): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle()
            args.putLong(ARG_TRACK_ID, trackId)
            fragment.arguments = args
            return fragment
        }
    }
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
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadTrackDetails()
        binding.playPreviewButton.setOnClickListener {
            playPreview()
        }
    }
    private fun loadTrackDetails(){
        lifecycleScope.launch {
            val track = withContext(Dispatchers.IO){
                RetrofitClient.api.getTrackDetails(trackId)
            }
            binding.trackTitleTextView.text = track.title
            binding.artistNameTextView.text = track.artistInfo.name
            Glide.with(requireContext())
                .load(track.albumInfo.coverMedium)
                .into(binding.albumCoverImageView)
        }
    }
    private fun playPreview() {
        lifecycleScope.launch {
            val track = withContext(Dispatchers.IO){
                RetrofitClient.api.getTrackDetails(trackId)
            }
            val previewUrl = track.preview
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
                    }
                    setOnErrorListener{ _,_,_ ->
                        Toast.makeText(requireContext(),"Unable to play preview", Toast.LENGTH_SHORT).show()
                        true
                    }
                }
            } else {
                Toast.makeText(requireContext(),"No preview available", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }
}