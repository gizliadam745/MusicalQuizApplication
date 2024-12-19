package com.example.musicalquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "tracks",
    foreignKeys = [
        ForeignKey(
            entity = Artist::class,
            parentColumns = ["artistId"],
            childColumns = ["artistId"]
        ),
        ForeignKey(
            entity = Album::class,
            parentColumns = ["albumId"],
            childColumns = ["albumId"]
        )
    ],
    indices = [
        Index("artistId"),
        Index("albumId")
    ]
)
data class Track(
    @PrimaryKey
    val id: Long,
    val title: String,
    val artistId: Long,
    val albumId: Long,
    val preview: String
)