package com.example.musicalquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey
    val id: Long,
    val title: String,
    @SerializedName("artist")
    val artistInfo: Artist,
    @SerializedName("album")
    val albumInfo: Album,
    val preview:String
)

data class Artist(
    val name:String
)
data class Album(
    val title:String,
    @SerializedName("coverMedium")
    val coverMedium:String
)