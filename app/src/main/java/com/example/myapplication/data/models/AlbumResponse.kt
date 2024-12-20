package com.example.myapplication.data.models

import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("data")
    val albums: List<Album>
)