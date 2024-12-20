package com.example.myapplication.deezerapi

import com.example.myapplication.data.models.Album
import com.example.myapplication.data.models.AlbumResponse
import com.example.myapplication.data.models.Track
import com.example.myapplication.data.models.TrackResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private const val BASE_URL = "https://api.deezer.com/"

interface DeezerApiService {
    @GET("track/{id}")
    suspend fun getTrack(@Path("id") id: Long): Track

    @GET("album/{id}")
    suspend fun getAlbum(@Path("id") id: Long): Album

    @GET("search/track")
    suspend fun searchTracks(@Query("q") query: String): TrackResponse

    @GET("search/album")
    suspend fun searchAlbums(@Query("q") query: String): AlbumResponse
}

object DeezerApi{

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    val retrofitService: DeezerApiService by lazy {
        retrofit.create(DeezerApiService::class.java)
    }
}