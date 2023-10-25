package com.example.natifetesttask.domain.network

import android.content.Context
import com.example.natifetesttask.data.local.images.ImageCached
import com.example.natifetesttask.data.local.images.ImageDao
import com.example.natifetesttask.data.remote.GiphyResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import java.io.File
import java.util.UUID

interface NetworkImageRepository {
    fun closeClient()
    suspend fun getGiphyResponse(
        search: String,
        offset: Int,
        limit: Int
    ): GiphyResponse

    suspend fun downloadGifAndSave(
        pathOriginal: String,
        pathSmall: String,
        context: Context
    )
}

class NetworkImageRepositoryImpl(
    private val httpClient: HttpClient,
    private val imageDao: ImageDao
) : NetworkImageRepository {
    override fun closeClient() {
        httpClient.close()
    }

    private val API_KEY = "DOdqj0cV0EKTKH9hkQvHxqfloK4WfVlB"
    override suspend fun getGiphyResponse(
        search: String,
        offset: Int,
        limit: Int
    ): GiphyResponse {
        return httpClient
            .get("https://api.giphy.com/v1/gifs/search?api_key=$API_KEY&q=$search&limit=$limit&offset=$offset&rating=g&lang=en&bundle=messaging_non_clips")
            .body()
    }

    override suspend fun downloadGifAndSave(
        pathOriginal: String,
        pathSmall: String,
        context: Context
    ) {
        val httpClient = HttpClient()
        val byteArraySmall = httpClient.get(pathSmall).readBytes()

        val uniqueId = UUID.randomUUID().toString()
        val fileSmall = File(context.cacheDir, "${uniqueId}_small.gif")

        fileSmall.writeBytes(byteArraySmall)

        val imageEntity = ImageCached(
            pathNetworkOriginal = pathOriginal,
            pathNetworkSmall = pathSmall,
            pathLocalOriginal = "",
            pathLocalSmall = fileSmall.path,
        )
        imageDao.insert(imageEntity)
    }
}