package com.example.natifetesttask.data.local.images_deleted

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.natifetesttask.data.local.images.ImageCached
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDeletedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImageDeleted(imageDeleted: ImageDeleted)
    @Update
    suspend fun updateImageDeleted(imageDeleted: ImageDeleted)
    @Delete
    suspend fun deleteImageDeleted(imageDeleted: ImageDeleted)
    @Query("SELECT * FROM images_deleted ORDER BY id ASC")
    fun getAllImageDeleted(): Flow<List<ImageDeleted>>
    @Query("DELETE FROM images_deleted")
    suspend fun deleteAllImageDeleted()
}