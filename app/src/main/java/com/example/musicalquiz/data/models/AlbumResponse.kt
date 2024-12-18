package com.example.musicalquiz.data.models

data class AlbumResponse(
    val data: List<AlbumItem>
)
data class AlbumItem(
    val id: Long,
    val title: String,
    val coverMedium:String,
    val artist:Artist
)