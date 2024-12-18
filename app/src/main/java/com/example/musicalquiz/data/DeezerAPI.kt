package com.example.musicalquiz.data

import com.example.musicalquiz.data.models.AlbumResponse
import com.example.musicalquiz.data.models.Track
import com.example.musicalquiz.data.models.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerApi {
    @GET("search/track")
    suspend fun searchTracks(@Query("q") query: String): TrackResponse

    @GET("search/album")
    suspend fun searchAlbums(@Query("q") query: String): AlbumResponse

    @GET("track/{id}")
    suspend fun getTrackDetails(@Path("id") trackId:Long): Track
}