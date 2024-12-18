package com.example.musicalquiz.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicalquiz.data.RetrofitClient
import com.example.musicalquiz.data.models.AlbumResponse
import com.example.musicalquiz.data.models.TrackResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel:ViewModel() {
    private val _trackResponse = MutableStateFlow<TrackResponse?>(null)
    val trackResponse: StateFlow<TrackResponse?> = _trackResponse

    private val _albumResponse = MutableStateFlow<AlbumResponse?>(null)
    val albumResponse: StateFlow<AlbumResponse?> = _albumResponse
    private val deezerApi = RetrofitClient.api
    fun searchTracks(query: String) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                deezerApi.searchTracks(query)
            }
            _trackResponse.value = response
        }
    }
    fun searchAlbums(query: String) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                deezerApi.searchAlbums(query)
            }
            _albumResponse.value = response
        }
    }

}