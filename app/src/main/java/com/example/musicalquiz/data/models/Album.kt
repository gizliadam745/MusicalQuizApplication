package com.example.musicalquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class Album(
    @PrimaryKey(autoGenerate = true)
    val albumId:Long = 0, //Unique ID
    val title: String,
    val coverMedium: String
)