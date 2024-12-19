package com.example.musicalquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artists")
data class Artist(
    @PrimaryKey(autoGenerate = true)
    val artistId: Long = 0, //Unique ID
    val name: String
)