package com.example.natifetesttask.data.local.images_deleted

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images_deleted")
data class ImageDeleted(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pathNetworkOriginal: String,
    val pathLocalSmall: String,
    val timeDeleted: String
)
