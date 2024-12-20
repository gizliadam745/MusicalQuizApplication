package com.example.myapplication.mvvm.search

import android.util.Log
import com.example.myapplication.data.models.Album
import com.example.myapplication.data.models.Track
import com.example.myapplication.deezerapi.DeezerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository {
    private val deezerService = DeezerApi.retrofitService

    suspend fun searchTracks(query: String): List<Track> {
        return withContext(Dispatchers.IO) {
            val response =  deezerService.searchTracks(query)
            response.tracks
        }
    }

    suspend fun searchAlbums(query: String): List<Album> {
        return withContext(Dispatchers.IO) {
            val response = deezerService.searchAlbums(query)
            Log.d("SearchRepository", "searchAlbums: response.albums = ${response.albums}")
            response.albums
        }
    }

    suspend fun getTrack(id: Long): Track {
        return withContext(Dispatchers.IO) {
            deezerService.getTrack(id)
        }
    }

    suspend fun getAlbum(id: Long): Album {
        return withContext(Dispatchers.IO) {
            deezerService.getAlbum(id)
        }
    }
}