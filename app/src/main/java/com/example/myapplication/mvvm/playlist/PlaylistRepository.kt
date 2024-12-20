package com.example.myapplication.mvvm.playlist

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.myapplication.data.db.AppDatabase
import com.example.myapplication.data.db.playlist.Playlist
import com.example.myapplication.data.db.playlist.PlaylistTrack

class PlaylistRepository(context: Context) {
    private val playlistDao = AppDatabase.getDatabase(context).playlistDao()

    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long) {
        withContext(Dispatchers.IO){
            playlistDao.insertPlaylistTrack(PlaylistTrack(playlistId = playlistId, trackId = trackId))
        }
    }
    suspend fun insertPlaylist(playlist: Playlist): Long{
        return withContext(Dispatchers.IO){
            playlistDao.insertPlaylist(playlist)
        }
    }
    suspend fun getAllPlaylists() = withContext(Dispatchers.IO){
        playlistDao.getAllPlaylists()
    }
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        withContext(Dispatchers.IO){
            playlistDao.removePlaylistTrack(playlistId, trackId)
        }
    }
    suspend fun getPlaylistWithTracks(playlistId: Long) = withContext(Dispatchers.IO){
        playlistDao.getPlaylistWithTracks(playlistId)
    }
    suspend fun deletePlaylist(playlistId: Long) {
        withContext(Dispatchers.IO){
            playlistDao.deletePlaylist(playlistId)
        }
    }
}