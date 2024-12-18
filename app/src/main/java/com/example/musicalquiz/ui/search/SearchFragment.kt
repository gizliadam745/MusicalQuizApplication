package com.example.musicalquiz.ui.search
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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.musicalquiz.R
import com.example.musicalquiz.databinding.FragmentSearchBinding
import com.example.musicalquiz.ui.details.DetailsFragment
import com.example.musicalquiz.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.searchButton.setOnClickListener {
            val query = binding.searchBar.text.toString()
            if (query.isNotBlank()) {
                if(binding.searchTrackOption.isChecked){
                    viewModel.searchTracks(query)
                } else if(binding.searchAlbumOption.isChecked){
                    viewModel.searchAlbums(query)
                }
            } else {
                Toast.makeText(requireContext(), "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }
        observeSearchResults()
    }
    private fun observeSearchResults() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.trackResponse.collect { trackResponse ->
                        trackResponse?.let {
                            searchAdapter.submitList(it.data)
                        }
                    }
                }
                launch {
                    viewModel.albumResponse.collect { albumResponse ->
                        albumResponse?.let {
                            searchAdapter.submitList(it.data)
                        }
                    }
                }
            }
        }
    }
    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter{track ->
            val detailsFragment = DetailsFragment.newInstance(track.id)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,detailsFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.searchResultsRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = searchAdapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}