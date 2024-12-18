package com.example.musicalquiz.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicalquiz.data.models.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks:List<Track>)

    @Query("SELECT * FROM tracks WHERE id IN (:trackIds)")
    fun getTracksByIds(trackIds: List<Long>): Flow<List<Track>>
}