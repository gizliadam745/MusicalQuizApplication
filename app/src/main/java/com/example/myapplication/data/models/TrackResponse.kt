package com.example.myapplication.data.models

import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName("data")
    val tracks: List<Track>
)