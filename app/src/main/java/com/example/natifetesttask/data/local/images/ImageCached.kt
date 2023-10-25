package com.example.natifetesttask.data.local.images

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images_cached")
data class ImageCached(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pathNetworkOriginal: String,
    val pathNetworkSmall: String,
    val pathLocalOriginal: String,
    val pathLocalSmall: String
)
