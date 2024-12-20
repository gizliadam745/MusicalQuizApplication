package com.example.myapplication.data.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class PlaylistWithTracks(
    @Embedded
    val playlist: Playlist,
    @Relation(
        parentColumn = "id",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistTrack::class)
    )
    val tracks: List<PlaylistTrack>
)