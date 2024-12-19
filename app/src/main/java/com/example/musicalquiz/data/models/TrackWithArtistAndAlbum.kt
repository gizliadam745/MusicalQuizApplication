package com.example.musicalquiz.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class TrackWithArtistAndAlbum (
    @Embedded val track:Track,
    @Relation(
        parentColumn = "artistId",
        entityColumn = "artistId"
    )
    val artist: Artist,
    @Relation(
        parentColumn = "albumId",
        entityColumn = "albumId"
    )
    val album:Album
)