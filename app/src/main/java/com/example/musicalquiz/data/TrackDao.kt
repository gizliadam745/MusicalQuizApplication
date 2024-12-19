package com.example.musicalquiz.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.musicalquiz.data.models.Track
import com.example.musicalquiz.data.models.TrackWithArtistAndAlbum
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks:List<Track>)

    @Query("SELECT * FROM tracks WHERE id IN (:trackIds)")
    fun getTracksByIds(trackIds: List<Long>): Flow<List<Track>>

    @Transaction
    @Query("SELECT * FROM tracks WHERE id = :trackId")
    fun getTrackWithArtistAndAlbumById(trackId: Long): Flow<TrackWithArtistAndAlbum>
}