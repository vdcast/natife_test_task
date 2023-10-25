package com.example.natifetesttask.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.natifetesttask.data.local.images.ImageCached
import com.example.natifetesttask.data.local.images.ImageDao
import com.example.natifetesttask.data.local.images_deleted.ImageDeleted
import com.example.natifetesttask.data.local.images_deleted.ImageDeletedDao

@Database(
    entities = [ImageCached::class, ImageDeleted::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun imageDeletedDao(): ImageDeletedDao
}