package com.example.myapplication.adapters

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.myapplication.data.db.playlist.PlaylistTrack

fun Date.formatDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(this)
}

fun PlaylistTrack.formatDate(): String {
    val date = Date(id)
    return date.formatDate()
}