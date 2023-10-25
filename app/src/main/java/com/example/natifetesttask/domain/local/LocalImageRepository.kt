package com.example.natifetesttask.domain.local

import com.example.natifetesttask.data.local.images.ImageCached
import com.example.natifetesttask.data.local.images.ImageDao
import com.example.natifetesttask.data.local.images_deleted.ImageDeleted
import com.example.natifetesttask.data.local.images_deleted.ImageDeletedDao
import kotlinx.coroutines.flow.Flow

interface LocalImageRepository {
    // images cached
    suspend fun insert(imageCached: ImageCached)
    suspend fun update(imageCached: ImageCached)
    suspend fun delete(imageCached: ImageCached)
    fun getAllImagesFromLocal(): Flow<List<ImageCached>>
    fun getGifs(offset: Int, limit: Int): List<ImageCached>
    suspend fun deleteAll()
    suspend fun deleteImageByPathNetworkOriginal(imagePath: String)
    suspend fun getImageByPathNetworkOriginal(imagePath: String): ImageCached?

    // images deleted
    suspend fun insertImageDeleted(imageDeleted: ImageDeleted)
    suspend fun updateImageDeleted(imageDeleted: ImageDeleted)
    suspend fun deleteImageDeleted(imageDeleted: ImageDeleted)
    fun getAllImageDeleted(): Flow<List<ImageDeleted>>
    suspend fun deleteAllImageDeleted()
}

class LocalImageRepositoryImpl(
    private val imageDao: ImageDao,
    private val imageDeletedDao: ImageDeletedDao
) : LocalImageRepository {
    // images cached
    override suspend fun insert(imageCached: ImageCached) = imageDao.insert(imageCached)
    override suspend fun update(imageCached: ImageCached) = imageDao.update(imageCached)
    override suspend fun delete(imageCached: ImageCached) = imageDao.delete(imageCached)
    override fun getAllImagesFromLocal(): Flow<List<ImageCached>> = imageDao.getAllImagesFromLocal()
    override fun getGifs(offset: Int, limit: Int): List<ImageCached> =
        imageDao.getGifs(offset, limit)

    override suspend fun deleteAll() = imageDao.deleteAll()
    override suspend fun deleteImageByPathNetworkOriginal(imagePath: String) =
        imageDao.deleteImageByPathNetworkOriginal(imagePath)

    override suspend fun getImageByPathNetworkOriginal(imagePath: String): ImageCached? =
        imageDao.getImageByPathNetworkOriginal(imagePath)

    // images deleted
    override suspend fun insertImageDeleted(imageDeleted: ImageDeleted) =
        imageDeletedDao.insertImageDeleted(imageDeleted)

    override suspend fun updateImageDeleted(imageDeleted: ImageDeleted) =
        imageDeletedDao.updateImageDeleted(imageDeleted)

    override suspend fun deleteImageDeleted(imageDeleted: ImageDeleted) =
        imageDeletedDao.deleteImageDeleted(imageDeleted)

    override fun getAllImageDeleted(): Flow<List<ImageDeleted>> =
        imageDeletedDao.getAllImageDeleted()

    override suspend fun deleteAllImageDeleted() =
        imageDeletedDao.deleteAllImageDeleted()
}