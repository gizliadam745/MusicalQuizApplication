package com.example.myapplication.mvvm.playlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.db.playlist.Playlist
import com.example.myapplication.data.db.playlist.PlaylistWithTracks
import kotlinx.coroutines.launch

class PlaylistViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PlaylistRepository(application)
    private val _playlists = MutableLiveData<List<PlaylistWithTracks>>()
    val playlists: LiveData<List<PlaylistWithTracks>> = _playlists
    private val _playlist = MutableLiveData<PlaylistWithTracks?>()
    val playlist: MutableLiveData<PlaylistWithTracks?> = _playlist
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun insertPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            try {
                repository.insertPlaylist(playlist)
//                getPlaylistWithTracks(playlistId)
            } catch (e: Exception) {
                _error.value = "Error when adding playlist: ${e.message}"
            }
        }
    }

    fun getAllPlaylists() {
        viewModelScope.launch {
            try {
                val allPlaylists = repository.getAllPlaylists()
                _playlists.value = allPlaylists
            } catch (e: Exception) {
                _error.value = "Error when fetching playlists: ${e.message}"
            }
        }
    }
    fun addTrackToPlaylist(playlistId: Long, trackId: Long){
        viewModelScope.launch {
            try {
                repository.addTrackToPlaylist(playlistId, trackId)
            } catch (e: Exception){
                _error.value = "Error adding track to playlist: ${e.message}"
            }
        }
    }
    fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        viewModelScope.launch {
            try {
                repository.removeTrackFromPlaylist(playlistId, trackId)
            } catch (e: Exception){
                _error.value = "Error removing track from playlist: ${e.message}"
            }
        }
    }
    fun getPlaylistWithTracks(playlistId:Long){
        viewModelScope.launch {
            try {
                val playlist = repository.getPlaylistWithTracks(playlistId)
                _playlist.value = playlist
            } catch (e: Exception){
                _error.value = "Error when fetching playlist: ${e.message}"
            }
        }
    }
    fun deletePlaylist(playlistId: Long){
        viewModelScope.launch {
            try {
                repository.deletePlaylist(playlistId)
            } catch (e: Exception){
                _error.value = "Error when deleting playlist: ${e.message}"
            }
        }
    }
}