package com.example.musicalquiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicalquiz.data.models.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAlbums(albums: List<Album>)

    @Query("SELECT * FROM albums WHERE albumId = :albumId")
    fun getAlbumById(albumId: Long): Flow<Album>

    @Query("SELECT * FROM albums")
    fun getAllAlbums(): Flow<List<Album>>
}