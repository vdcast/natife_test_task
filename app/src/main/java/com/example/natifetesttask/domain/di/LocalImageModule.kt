package com.example.natifetesttask.domain.di

import android.content.Context
import androidx.room.Room
import com.example.natifetesttask.data.local.images.AppDatabase
import com.example.natifetesttask.data.local.images.ImageDao
import com.example.natifetesttask.domain.local.LocalImageRepository
import com.example.natifetesttask.domain.local.LocalImageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object LocalImageModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .build()
    }

    @Singleton
    @Provides
    fun provideImageDao(database: AppDatabase): ImageDao = database.imageDao()

    @Singleton
    @Provides
    fun provideImageRepository(imageDao: ImageDao): LocalImageRepository = LocalImageRepositoryImpl(imageDao)

}