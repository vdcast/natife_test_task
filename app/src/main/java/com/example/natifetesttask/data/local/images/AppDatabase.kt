package com.example.natifetesttask.data.local.images

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ImageCached::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}