package com.example.musicalquiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicalquiz.data.models.Artist
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artist: Artist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArtists(artist: List<Artist>)

    @Query("SELECT * FROM artists WHERE artistId = :artistId")
    fun getArtistById(artistId: Long): Flow<Artist>

    @Query("SELECT * FROM artists")
    fun getAllArtists(): Flow<List<Artist>>
}