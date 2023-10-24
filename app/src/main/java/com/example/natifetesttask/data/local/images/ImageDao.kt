package com.example.natifetesttask.data.local.images

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(imageCached: ImageCached)
    @Update
    suspend fun update(imageCached: ImageCached)
    @Delete
    suspend fun delete(imageCached: ImageCached)
    @Query("SELECT * FROM images_cached ORDER BY id ASC")
    fun getAllImagesFromLocal(): Flow<List<ImageCached>>
    @Query("SELECT * FROM images_cached WHERE pathNetworkOriginal IS NOT NULL LIMIT :limit OFFSET :offset")
    fun getGifs(offset: Int, limit: Int): List<ImageCached>
    @Query("DELETE FROM images_cached")
    suspend fun deleteAll()
}