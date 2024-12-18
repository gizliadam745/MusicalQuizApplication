package com.example.musicalquiz.data
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromTrackList(tracks: List<Long>?): String? {
        return Gson().toJson(tracks)
    }

    @TypeConverter
    fun toTrackList(trackListString: String?): List<Long>? {
        if (trackListString == null){
            return null
        }
        val type = object : TypeToken<List<Long>>(){}.type
        return Gson().fromJson(trackListString, type)
    }
}