package com.example.musicalquiz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.musicalquiz.data.AppDatabase
import com.example.musicalquiz.data.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "musical_quiz_db"
    ).build()
    private val playlistDao = db.playlistDao()

    fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists()
    }
    fun getPlaylistById(playlistId: Int): Flow<Playlist>{
        return playlistDao.getPlaylistById(playlistId)
    }
    fun insertPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                playlistDao.insertPlaylist(playlist)
            }
        }
    }
}