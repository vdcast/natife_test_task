package com.example.natifetesttask.domain.local

import com.example.natifetesttask.data.local.images.ImageCached
import com.example.natifetesttask.data.local.images.ImageDao
import kotlinx.coroutines.flow.Flow

interface LocalImageRepository {
    suspend fun insert(imageCached: ImageCached)
    suspend fun update(imageCached: ImageCached)
    suspend fun delete(imageCached: ImageCached)
    fun getAllImagesFromLocal(): Flow<List<ImageCached>>
    fun getGifs(offset: Int, limit: Int): List<ImageCached>
    suspend fun deleteAll()
}

class LocalImageRepositoryImpl(private val imageDao: ImageDao) : LocalImageRepository {
    override suspend fun insert(imageCached: ImageCached) = imageDao.insert(imageCached)
    override suspend fun update(imageCached: ImageCached) = imageDao.update(imageCached)
    override suspend fun delete(imageCached: ImageCached) = imageDao.delete(imageCached)
    override fun getAllImagesFromLocal(): Flow<List<ImageCached>> = imageDao.getAllImagesFromLocal()
    override fun getGifs(offset: Int, limit: Int): List<ImageCached> =
        imageDao.getGifs(offset, limit)
    override suspend fun deleteAll() = imageDao.deleteAll()
}